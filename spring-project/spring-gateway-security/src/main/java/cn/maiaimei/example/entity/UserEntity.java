package cn.maiaimei.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class UserEntity {
    @TableId
    private Long id;
    @TableField
    private String username;
    @TableField
    private String password;
    @TableField
    private String nickname;
    @TableField
    private LocalDateTime gmtCreate;
    @TableField
    private LocalDateTime gmtModified;
}