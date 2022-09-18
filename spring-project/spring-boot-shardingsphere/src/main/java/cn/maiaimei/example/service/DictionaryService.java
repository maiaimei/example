package cn.maiaimei.example.service;

import cn.maiaimei.example.mapper.DictionaryMapper;
import cn.maiaimei.example.model.Dictionary;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DictionaryService extends ServiceImpl<DictionaryMapper, Dictionary> {
}
