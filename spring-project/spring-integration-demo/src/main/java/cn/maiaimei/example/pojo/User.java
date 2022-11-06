package cn.maiaimei.example.pojo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class User {
    private Long id;
    private String name;
}
