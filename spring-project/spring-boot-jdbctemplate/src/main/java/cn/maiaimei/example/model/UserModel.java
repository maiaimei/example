package cn.maiaimei.example.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UserModel {
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;
    String nickname;
    String username;
    String password;
}
