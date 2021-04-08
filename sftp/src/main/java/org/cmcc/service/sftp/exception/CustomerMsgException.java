package org.cmcc.service.sftp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaolei
 * @date 2021-01-06 17:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerMsgException extends Exception {

    private String respCode;
    private String respDesc;

    public CustomerMsgException(String respCode, String respDesc) {
        this.respCode = respCode;
        this.respDesc = respDesc;
    }

    public CustomerMsgException() {
        super();
    }

    public CustomerMsgException(String message) {
        super(message);
    }

    public CustomerMsgException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerMsgException(Throwable cause) {
        super(cause);
    }

    protected CustomerMsgException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

