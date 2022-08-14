package cn.maiaimei.example.component;

import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class RequestWrapper extends HttpServletRequestWrapper {
    private Map<String, String> headers = new HashMap<>();
    private final byte[] bytes;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        bytes = IOUtils.toByteArray(request.getInputStream());
        // bytes = StreamUtils.copyToByteArray(request.getInputStream());
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (headers.containsKey(name)) {
            value = headers.get(name);
        }
        return value;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> list = Collections.list(super.getHeaders(name));
        if (headers.containsKey(name)) {
            list.add(headers.get(name));
        }
        return Collections.enumeration(list);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        names.addAll(headers.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private int lastIndexRetrieved = -1;
            private ReadListener readListener = null;

            @Override
            public boolean isFinished() {
                return (lastIndexRetrieved == bytes.length - 1);
            }

            @Override
            public boolean isReady() {
                // This implementation will never block
                // We also never need to call the readListener from this method, as this method will never return false
                return isFinished();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                this.readListener = readListener;
                if (!isFinished()) {
                    try {
                        readListener.onDataAvailable();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                } else {
                    try {
                        readListener.onAllDataRead();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                }
            }

            @Override
            public int read() throws IOException {
                int i;
                if (!isFinished()) {
                    i = bytes[lastIndexRetrieved + 1];
                    lastIndexRetrieved++;
                    if (isFinished() && (readListener != null)) {
                        try {
                            readListener.onAllDataRead();
                        } catch (IOException ex) {
                            readListener.onError(ex);
                            throw ex;
                        }
                    }
                    return i;
                } else {
                    return -1;
                }
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        return new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : "UTF-8");
    }

    @SneakyThrows
    public String getPayload() {
        if (null == bytes || bytes.length == 0) {
            return StringUtils.EMPTY;
        }
        return new String(bytes, getCharacterEncoding());
    }
}