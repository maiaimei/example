package cn.maiaimei.demo.converter;

import cn.maiaimei.demo.model.Book;
import cn.maiaimei.demo.model.EnglishBook;
import cn.maiaimei.demo.model.MathBook;
import cn.maiaimei.demo.model.Teacher;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class TeacherToBookConverterFactory implements ConverterFactory<Teacher, Book> {
    @Override
    public <T extends Book> Converter<Teacher, T> getConverter(Class<T> targetType) {
        if (EnglishBook.class.isAssignableFrom(targetType)) {
            return new EnglishTeacherToEnglishBookConverter();
        } else if (MathBook.class.isAssignableFrom(targetType)) {
            return new MathTeacherToMathBookConverter();
        }
        return null;
    }

    private static class EnglishTeacherToEnglishBookConverter<S extends Teacher, T extends Book> implements Converter<S, T> {
        @Override
        public T convert(S source) {
            EnglishBook englishBook = new EnglishBook();
            englishBook.setRemark(source.getName() + "是英语老师，有英语书");
            return (T) englishBook;
        }
    }

    private static class MathTeacherToMathBookConverter<S extends Teacher, T extends Book> implements Converter<S, T> {
        @Override
        public T convert(S source) {
            MathBook mathBook = new MathBook();
            mathBook.setRemark(source.getName() + "是数学老师，有数学书");
            return (T) mathBook;
        }
    }
}
