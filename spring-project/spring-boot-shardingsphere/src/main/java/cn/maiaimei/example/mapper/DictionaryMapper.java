package cn.maiaimei.example.mapper;

import cn.maiaimei.example.model.Dictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {
}
