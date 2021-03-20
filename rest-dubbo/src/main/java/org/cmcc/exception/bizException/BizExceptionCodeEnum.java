package org.cmcc.exception.bizException;

/** @Description : 异常消息的枚举类(此类属于业务异常枚举类) @Param : @Return : @Author : cmcc */
public enum BizExceptionCodeEnum implements BizExceptionCode {

  // 已指明的异常,在异常使用时message并不返回前端，返回前端的为throw新的异常时指定的message
  SPECIFIED("-1", "系统发生异常,请稍后重试"),
  SFTPPARAM("-2","sftp服务连接异常，请确定连接参数"),
  MIKDIR("-3", "创建远程目录异常，请确定创建目录");

  private final String code;

  private final String message;

  /** @Description : @Param : [code, message] @Return : @Author : cmcc */
  BizExceptionCodeEnum(String code, String message) {

    this.code = code;
    this.message = message;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
