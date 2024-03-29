package cn.maiaimei.example.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String nickname;
    private String username;
    private String password;
}