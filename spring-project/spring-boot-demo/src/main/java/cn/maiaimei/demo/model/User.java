package cn.maiaimei.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private Boolean isEnabled;
    private Boolean isDeleted;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
