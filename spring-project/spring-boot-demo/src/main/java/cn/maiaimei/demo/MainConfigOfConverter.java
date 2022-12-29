package cn.maiaimei.demo;

import cn.maiaimei.demo.converter.StringToFemaleConditionalConverter;
import cn.maiaimei.demo.converter.StringToMaleGenericConverter;
import cn.maiaimei.demo.converter.StringToPeopleConverter;
import cn.maiaimei.demo.converter.TeacherToBookConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

@Configuration
public class MainConfigOfConverter {
    /**
     * 1:1
     */
    @Bean
    public StringToPeopleConverter stringToPeopleConverter() {
        return new StringToPeopleConverter();
    }

    /**
     * 1:N
     */
    @Bean
    public StringToMaleGenericConverter stringToMaleGenericConverter() {
        return new StringToMaleGenericConverter();
    }

    /**
     * 1:N with condition
     */
    @Bean
    public StringToFemaleConditionalConverter stringToFemaleConditionalConverter() {
        return new StringToFemaleConditionalConverter();
    }

    /**
     * N:N
     */
    @Bean
    public TeacherToBookConverterFactory teacherToBookConverterFactory() {
        return new TeacherToBookConverterFactory();
    }

    /**
     * 如果想要 {@link ConditionalConverter#matches(org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)} 方法起作用，
     * 需要将自定义的Converter添加到 {@link GenericConversionService} 中
     */
    @Bean
    public DefaultConversionService defaultConversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(stringToPeopleConverter());
        service.addConverter(stringToMaleGenericConverter());
        service.addConverter(stringToFemaleConditionalConverter());
        return service;
    }
}
