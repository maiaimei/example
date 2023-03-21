package cn.maiaimei.example.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class User {
    private Long id;
    private String username;
    private String password;
    private Boolean enabled;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
