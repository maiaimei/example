package cn.maiaimei.example.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Inventor {

  private String name;
  private Date birthday;
  private String nationality;
}
