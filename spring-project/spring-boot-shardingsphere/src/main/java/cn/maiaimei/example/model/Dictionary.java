package cn.maiaimei.example.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("t_dictionary")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dictionary {
    @TableId(type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;

    @TableId(type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    Long pid;

    @TableField
    String dicKey;

    @TableField
    String dicValue;
}
