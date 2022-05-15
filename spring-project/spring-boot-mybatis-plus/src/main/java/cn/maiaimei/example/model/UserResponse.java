package cn.maiaimei.example.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String username;

    private String password;

    private String nickname;

}
