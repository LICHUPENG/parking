package com.park.common;

import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Func {

    /**
     * 一般的接口响应结构
     */
    private static Map<String, Object> resultMap(Boolean success, int statusCode, String message, Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", success);
        result.put("status", statusCode);
        result.put("msg", message);
        if (data != null) {
            String dataKey = success ? "result" : "error";
            result.put(dataKey, data);
        }
        return result;
    }

    public static Map<String, Object> successMap() {
        return resultMap(true, 200, "success", null);
    }

    public static Map<String, Object> successMap(String msg) {
        return resultMap(true, 200, msg, null);
    }

    public static Map<String, Object> successMap(Object data) {
        return resultMap(true, 200, "success", data);
    }

    public static Map<String, Object> successMap(String msg, Object data) {
        return resultMap(true, 200, msg, data);
    }

    public static Map<String, Object> failingMap(String msg) {
        return resultMap(false, 200, msg, null);
    }

    public static Map<String, Object> failingMap(String msg, Object data) {
        return resultMap(false, 200, msg, data);
    }

    public static Map<String, Object> failingMap(int statusCode, String msg, Object data) {
        return resultMap(false, statusCode, msg, data);
    }

    public static Map<String, Object> failingMap(int statusCode, String msg) {
        return resultMap(false, statusCode, msg, null);
    }

    /**
     * 取得当前时间戳(10位)
     *
     * @return
     */
    public static int getTime() {
        return (int) (Func.getMillisTime() / 1000L);
    }

    /**
     * 取得当前时间戳(13位)
     */
    public static long getMillisTime() {
        return Calendar.getInstance().getTime().getTime();
    }

    /**
     * 时间日期转换
     *
     * @param dateTime 基于秒的int类型时间
     * @param pattern  日期格式，缺省值yyyy-MM-dd HH:mm:ss
     * @return 格式化后的日期时间
     */
    public static String timeFormatted(int dateTime, String pattern) {
        return timeFormatted(new BigDecimal(dateTime).movePointRight(3).longValue(), pattern);
    }

    public static String timeFormatted(int dateTime) {
        return timeFormatted(new BigDecimal(dateTime).movePointRight(3).longValue(), null);
    }

    /**
     * 时间日期转换
     *
     * @param dateTime 基于毫秒的long类型时间
     * @param pattern  日期格式，缺省值yyyy-MM-dd HH:mm:ss
     */
    public static String timeFormatted(long dateTime, String pattern) {
        if (StringUtils.isEmpty(pattern))
            pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = new Date(dateTime);
        return sdf.format(date);
    }

    /**
     * Bean对象转换成Map
     */
    public static Map<String, Object> beanToMap(Object obj) {
        return Func.beanToMap(obj, false);
    }

    /**
     * Bean对象转换成Map
     *
     * @param isExcludeEmpty 当检测到空字符串，是否将其变为null
     */
    public static Map<String, Object> beanToMap(Object obj, boolean isExcludeEmpty) {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] proDesc = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : proDesc) {
                String key = pd.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }

                Method getter = pd.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : null;
                map.put(key, value);
                if (isExcludeEmpty)
                    if (value != null && value instanceof String && "".equals((value + "").trim()))
                        map.put(key, null);
            }
        } catch (Exception e) {
            throw new AppException("数据转换失败!", AppException.SYSTEM);
        }

        return map;
    }
}
