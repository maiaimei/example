package cn.maiaimei.example.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Fee {

  private String column01;
  private String column02;
  private String column03;
  private String column04;
  private String column05;
  private String column06;
  private String column07;
  private String column08;
  private String column09;
  private String column10;
  private List<Tax> taxes;
}
