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

import java.math.BigDecimal;

@TableName("t_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @TableId(type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @TableField
    private BigDecimal totalAmount;
}
