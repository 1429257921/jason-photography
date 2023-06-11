package com.jason.common.core.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义基础运行时异常
 *
 * @author gzc
 * @since 2023/6/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {
    /**
     * 错误码
     */
    protected Integer errCode;
    /**
     * 错误信息
     */
    protected String errMsg;

    public BizException() {
        super();
    }

    public BizException(String msg, Throwable cause) {
        super(msg, cause);
        this.errMsg = msg;

    }

    public BizException(String errMsg) {
        super(errMsg);
        this.errCode = 4010;
        this.errMsg = errMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String getMessage() {
        return this.errMsg;
    }
}
