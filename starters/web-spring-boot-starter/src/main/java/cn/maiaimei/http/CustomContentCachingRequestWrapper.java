package cn.maiaimei.http;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.Part;
import java.io.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

@Slf4j
public class CustomContentCachingRequestWrapper extends HttpServletRequestWrapper {

  private BufferedReader reader;
  private byte[] cachedBody;
  private Map<String, String[]> parameterMap;
  private Collection<Part> cachedParts;
  private final boolean isMultipart;

  public CustomContentCachingRequestWrapper(HttpServletRequest request) throws IOException, ServletException {
    super(request);
    cacheParameters();
    this.isMultipart = isMultipartRequest(request);
    if (this.isMultipart) {
      cacheMultipartContent();
    } else {
      cacheBody();
    }
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    if (this.isMultipart) {
      return super.getInputStream();
    }
    return new ContentCachingInputStream(this.cachedBody);
  }

  @Override
  public String getCharacterEncoding() {
    String enc = super.getCharacterEncoding();
    return (Objects.nonNull(enc) ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
  }

  @Override
  public BufferedReader getReader() throws IOException {
    if (this.reader == null) {
      this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }
    return this.reader;
  }

  @Override
  public String getParameter(String name) {
    if (isMultipart) {
      return getMultipartParameter(name).orElse(super.getParameter(name));
    }
    return super.getParameter(name);
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    if (isMultipart) {
      Map<String, List<String>> multipartParams = extractMultipartParameters();
      Map<String, String[]> mergedParams = new HashMap<>(parameterMap);
      multipartParams.forEach((key, values) -> mergedParams.put(key, values.toArray(new String[0])));
      return Collections.unmodifiableMap(mergedParams);
    }
    return Collections.unmodifiableMap(parameterMap);
  }

  @Override
  public Enumeration<String> getParameterNames() {
    if (isMultipart) {
      Set<String> parameterNames = new HashSet<>(Collections.list(super.getParameterNames()));
      cachedParts.stream()
          .filter(part -> part.getSubmittedFileName() == null)
          .map(Part::getName)
          .forEach(parameterNames::add);
      return Collections.enumeration(parameterNames);
    }
    return super.getParameterNames();
  }

  @Override
  public String[] getParameterValues(String name) {
    if (isMultipart) {
      List<String> values = cachedParts.stream()
          .filter(part -> part.getName().equals(name) && part.getSubmittedFileName() == null)
          .map(this::readPartContent)
          .toList();
      return values.isEmpty() ? super.getParameterValues(name) : values.toArray(new String[0]);
    }
    return super.getParameterValues(name);
  }

  @Override
  public Part getPart(String name) throws IOException, ServletException {
    if (isMultipart) {
      return cachedParts.stream()
          .filter(p -> p.getName().equals(name))
          .findFirst()
          .orElse(null);
    }
    return super.getPart(name);
  }

  @Override
  public Collection<Part> getParts() throws IOException, ServletException {
    if (isMultipart) {
      return Collections.unmodifiableCollection(cachedParts);
    }
    return super.getParts();
  }

  public byte[] getContentAsByteArray() {
    return this.cachedBody;
  }

  public void cleanup() {
    if (!CollectionUtils.isEmpty(cachedParts)) {
      cachedParts.forEach(part -> {
        try {
          part.delete();
        } catch (IOException e) {
          log.error("Failed to delete part: {}", part.getName(), e);
        }
      });
    }
  }

  private boolean isMultipartRequest(HttpServletRequest request) {
    String contentType = request.getContentType();
    return (Objects.nonNull(contentType) && contentType.toLowerCase(Locale.ROOT).startsWith("multipart/"))
        || request instanceof MultipartHttpServletRequest;
  }

  private void cacheParameters() {
    this.parameterMap = new HashMap<>(getRequest().getParameterMap());
  }

  private void cacheMultipartContent() throws ServletException, IOException {
    this.cachedParts = new ArrayList<>(super.getParts());
  }

  private void cacheBody() throws IOException {
    try (InputStream requestInputStream = super.getInputStream()) {
      this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
    }
  }

  private Optional<String> getMultipartParameter(String name) {
    return cachedParts.stream()
        .filter(part -> part.getName().equals(name) && part.getSubmittedFileName() == null)
        .map(this::readPartContent)
        .findFirst();
  }

  private Map<String, List<String>> extractMultipartParameters() {
    Map<String, List<String>> multipartParams = new HashMap<>();
    cachedParts.stream()
        .filter(part -> part.getSubmittedFileName() == null)
        .forEach(part -> multipartParams
            .computeIfAbsent(part.getName(), k -> new ArrayList<>())
            .add(readPartContent(part)));
    return multipartParams;
  }

  private String readPartContent(Part part) {
    try (InputStream inputStream = part.getInputStream()) {
      return new String(StreamUtils.copyToByteArray(inputStream));
    } catch (IOException e) {
      log.error("Failed to read part content for part: {}", part.getName(), e);
      return "";
    }
  }

  private static class ContentCachingInputStream extends ServletInputStream {

    private final ByteArrayInputStream buffer;

    public ContentCachingInputStream(byte[] contents) {
      this.buffer = new ByteArrayInputStream(contents);
    }

    @Override
    public int read() {
      return buffer.read();
    }

    @Override
    public boolean isFinished() {
      return buffer.available() == 0;
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
      throw new UnsupportedOperationException();
    }
  }
}
