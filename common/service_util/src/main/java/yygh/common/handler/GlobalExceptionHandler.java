package yygh.common.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import yygh.common.exception.YyghException;
import yygh.common.result.Result;

/**
 * @create 2022-10-01 10:22
 * 全局异常处理类
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result Handler(Exception e){
        e.printStackTrace();
        return Result.fail();

    }

    @ExceptionHandler(YyghException.class)
    public Result Handler(YyghException e){
        e.printStackTrace();
        return Result.build(e.getCode(),e.getMessage());

    }


}
