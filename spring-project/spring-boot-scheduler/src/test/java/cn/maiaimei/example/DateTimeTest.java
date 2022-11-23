package cn.maiaimei.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

@Slf4j
public class DateTimeTest {
    @Test
    void test() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        log.info("year={}", year);
        log.info("month={}", month + 1);
        log.info("date={}", date);
        log.info("hour={}", hour);
        log.info("minute={}", minute);
    }
}
