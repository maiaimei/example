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

@TableName("t_order_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    @TableId(type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId;

    @TableField
    private BigDecimal productAmount;

    @TableField
    private Integer productCount;
}
