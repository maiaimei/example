package cn.maiaimei.example.model;

import lombok.Data;

@Data
public class UserModel {
    private Long id;
    private String nickname;
    private String username;
    private String password;
}
