package org.cmcc.service.common.uitl;


import com.google.gson.Gson;

/**
 * @ClassName ResultUtil
 * @Description TODO
 * @Author cmcc
 * @Date 2019/1/1
 * Version  1.0
 */
public class ResultUtil<T> {

    public static <T> String success() {
        Result<T> result = new Result<T>(ResultEnum.SUCCESS);
        return new Gson().toJson(result);
    }

    public static <T> String success(T obj) {
        Result<T> result = new Result<T>(ResultEnum.SUCCESS, obj);
        return new Gson().toJson(result);
    }

    public static <T> String fail() {
        Result<T> result = new Result<T>(ResultEnum.FAIL);
        return new Gson().toJson(result);
    }

    public static <T> String success(Integer code, String message) {
        Result<T> result = new Result<T>(code, message);
        return new Gson().toJson(result);
    }

}
