package cn.maiaimei.example.service;

import cn.maiaimei.example.mapper.OrderMapper;
import cn.maiaimei.example.model.Order;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {
}
