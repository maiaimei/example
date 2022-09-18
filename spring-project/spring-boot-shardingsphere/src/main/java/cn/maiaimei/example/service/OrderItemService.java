package cn.maiaimei.example.service;

import cn.maiaimei.example.mapper.OrderItemMapper;
import cn.maiaimei.example.model.OrderItem;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService extends ServiceImpl<OrderItemMapper, OrderItem> {
}
