package cn.maiaimei.demo.converter;

import cn.maiaimei.demo.model.Girl;
import cn.maiaimei.demo.model.Woman;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Set;

public class StringToFemaleConditionalConverter implements ConditionalGenericConverter {
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return Girl.class.equals(targetType.getType()) || Woman.class.equals(targetType.getType());
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return null;
    }

    @Override

    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        String string = (String) source;
        String[] arr = string.split(":");
        if (Girl.class.equals(targetType.getType())) {
            Girl girl = new Girl();
            girl.setId(arr[0]);
            girl.setName(arr[1]);
            return girl;
        } else if (Woman.class.equals(targetType.getType())) {
            Woman woman = new Woman();
            woman.setId(arr[0]);
            woman.setName(arr[1]);
            return woman;
        }
        return null;
    }
}
