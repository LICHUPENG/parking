package com.park.common;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@ResponseBody
public class CustomExceptionHandler {

    /** 定制异常处理 */
    @ExceptionHandler({AppException.class})
    public Map<String, Object> appExceptionHandler(AppException exception) {
        return Func.failingMap(exception.getStatusCode(), exception.getMessage(), null);
    }

    /** 数据表单验证失败处理 */
    @ExceptionHandler({BindException.class})
    public Map<String, Object> validExceptionHandler(BindException exception) {
        List<FieldError> fes = exception.getBindingResult().getFieldErrors();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (FieldError obj : fes) {
            Map<String, Object> tmp = new HashMap<String, Object>();
            tmp.put("msg", obj.getDefaultMessage());
            tmp.put("field", obj.getField());
            result.add(tmp);
        }
        return Func.failingMap(AppException.FORM_INVALID, "表单验证失败!", result);
    }

    /** 参数类型不正确 */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public Map<String, Object> typeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("field", exception.getName());
        tmp.put("msg", "参数类型有误!");

        result.add(tmp);
        return Func.failingMap(AppException.FORM_INVALID, "请求参数类型有误!", result);
    }

    /** 缺少关键参数 */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Map<String, Object> missParamExceptionHandler(MissingServletRequestParameterException exception) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("field", exception.getParameterName());
        tmp.put("msg", "必须填写!");

        result.add(tmp);
        return Func.failingMap(AppException.FORM_INVALID, "请提供必填参数!", result);
    }


    @ExceptionHandler({MultipartException.class})
    public Map<String, Object> multipartExceptionHandler(MultipartException exception){
        return Func.failingMap(AppException.FORM_INVALID, "提交表单类型有误!");
    }

    @ExceptionHandler
    public Map<String, Object> exceptionHandler(Exception exception) {
        return Func.failingMap(AppException.SYSTEM, exception.getMessage(), null);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, Object> sysExceptionExceptionHandler(MethodArgumentNotValidException exception){
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        List<Map<String, String>> result = errors.stream().map(error -> {
            Map<String, String> map = new HashMap<>(2, 1f);
            map.put("message", error.getDefaultMessage());
            return map;
        }).collect(Collectors.toList());

        String message = result.get(0).get("message");
        return Func.failingMap(AppException.FORM_INVALID, message, result);
    }
}
