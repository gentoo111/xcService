package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常捕获类
 */
@ControllerAdvice
public class ExceptionCatch {


    //不可预知的异常在map中配置异常所对应的错误代码及信息
    //ImmutableMap是不可变的map,线程安全
    private ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //用于构建ImmutableMap的数据
    private static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder=ImmutableMap.builder();

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVLIDATE);
    }

    //捕获不可预知的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult catchException(Exception e) {
        if (EXCEPTIONS == null) {
            //异常类型和错误代码的map构建成功
            EXCEPTIONS=builder.build();
        }
        //从map中找到异常类型对应的错误代码
        ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if (resultCode!=null) {
            return new ResponseResult(resultCode);
        }
        //统一抛出99999服务器异常
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }

    //捕获预知的异常
    //ControllerAdvice ExceptionHandler配合实现异常的捕获
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e) {
        //处理异常
        ResultCode resultCode = e.getResultCode();
        //给用户返回异常信息码,以json处理
        return new ResponseResult(resultCode);
    }
}
