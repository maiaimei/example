package cn.maiaimei.example.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("sys_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;
    
    @TableField
    String nickname;

    @TableField
    String username;

    @TableField
    String password;
}
