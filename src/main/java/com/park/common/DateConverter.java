package com.park.common;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component("dateConverter")
public class DateConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String source) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(source);
            int time = (int) (date.getTime()/1000);
            return Integer.valueOf(time);
        } catch (ParseException e) {
            throw new RuntimeException("日期格式不正确");
        }
    }
}
