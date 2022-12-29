package cn.maiaimei.demo.converter;

import cn.maiaimei.demo.model.People;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToPeopleConverter implements Converter<String, People> {
    @Override
    public People convert(String source) {
        if (StringUtils.isBlank(source) || !source.contains(":")) {
            return null;
        }
        String[] arr = source.split(":");
        People people = new People();
        people.setId(arr[0]);
        people.setName(arr[1]);
        return people;
    }
}
