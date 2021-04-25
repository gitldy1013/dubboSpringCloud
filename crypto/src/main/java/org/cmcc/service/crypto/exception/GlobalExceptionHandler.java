package org.cmcc.service.crypto.exception;

import lombok.extern.slf4j.Slf4j;
import org.cmcc.service.common.uitl.Result;
import org.cmcc.service.common.uitl.ResultEnum;
import org.cmcc.service.crypto.entity.valid.InvalidArgumentInfo;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaolei
 * @date 2021-03-16 09:21
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {



    /**
     * 参数校验的异常处理
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<ArrayList<InvalidArgumentInfo>> handlerException(MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        ArrayList<InvalidArgumentInfo> invalidArgList = new ArrayList<>();
        for (FieldError error:fieldErrors){
            InvalidArgumentInfo invalidArgumentInfo = new InvalidArgumentInfo();
            invalidArgumentInfo.setField(error.getField());
            invalidArgumentInfo.setDefaultMessage(error.getDefaultMessage());
            invalidArgumentInfo.setRejectedValue(error.getRejectedValue());
            invalidArgList.add(invalidArgumentInfo);
        }
        log.error("参数校验全局异常错误信息:"+e.getMessage());
        return new Result<ArrayList<InvalidArgumentInfo>>(ResultEnum.PARAM_VALID_FALSE.getCode(),
                ResultEnum.PARAM_VALID_FALSE.getMessage(),invalidArgList);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> handlerException(Exception e){
        log.error("错误信息:"+e.getMessage());
//        这里应该返回错误码和错误描述。
        return new Result<Object>(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMessage());
    }

}
