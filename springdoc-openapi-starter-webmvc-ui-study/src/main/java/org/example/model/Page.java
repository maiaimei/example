package org.example.model;

import java.util.List;
import lombok.Data;

@Data
public class Page<T> {

  private long total;
  private long size;
  private long current;
  private long pages;
  private List<T> records;
}
