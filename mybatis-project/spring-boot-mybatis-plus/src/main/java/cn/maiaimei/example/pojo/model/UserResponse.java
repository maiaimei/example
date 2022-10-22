package cn.maiaimei.example.pojo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String nickname;

    private String username;

    private Integer version;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
