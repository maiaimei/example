package cn.maiaimei.example.pojo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {
    private Long id;
    private String nickname;
    private String username;
    private String password;
}
