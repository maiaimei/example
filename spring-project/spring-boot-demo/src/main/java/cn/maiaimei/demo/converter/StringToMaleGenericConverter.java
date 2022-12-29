package cn.maiaimei.demo.converter;

import cn.maiaimei.demo.model.Boy;
import cn.maiaimei.demo.model.Man;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.HashSet;
import java.util.Set;

public class StringToMaleGenericConverter implements GenericConverter {
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> convertiblePairs = new HashSet<>();
        convertiblePairs.add(new ConvertiblePair(String.class, Boy.class));
        convertiblePairs.add(new ConvertiblePair(String.class, Man.class));
        return convertiblePairs;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        String string = (String) source;
        String[] arr = string.split(":");
        if (Boy.class.equals(targetType.getType())) {
            Boy boy = new Boy();
            boy.setId(arr[0]);
            boy.setName(arr[1]);
            return boy;
        } else if (Man.class.equals(targetType.getType())) {
            Man man = new Man();
            man.setId(arr[0]);
            man.setName(arr[1]);
            return man;
        }
        return null;
    }
}
