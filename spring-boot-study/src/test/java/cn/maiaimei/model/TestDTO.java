package cn.maiaimei.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import org.example.util.IdGenerator;

@Getter
public class TestDTO {

  private final BigDecimal id;
  private final String value;
  private final LocalDateTime createdAt;

  public TestDTO(String value) {
    this.id = IdGenerator.nextId();
    this.value = value;
    this.createdAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    TestDTO testDto = (TestDTO) obj;
    return value.equals(testDto.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}