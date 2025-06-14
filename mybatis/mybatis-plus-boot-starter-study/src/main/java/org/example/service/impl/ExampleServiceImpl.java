package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mapper.ExampleMapper;
import org.example.model.domain.Example;
import org.example.service.ExampleService;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl extends ServiceImpl<ExampleMapper, Example> implements ExampleService {

}
