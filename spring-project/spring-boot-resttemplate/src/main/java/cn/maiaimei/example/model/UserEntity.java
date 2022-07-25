package cn.maiaimei.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    /**
     * 局部解决前端Long字段精度丢失问题
     */
    // @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String nickname;
    private String username;
    // @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
}
