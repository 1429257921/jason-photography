package com.jason.common.service.exception;

import com.jason.common.service.enums.ApiErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义业务运行时异常
 *
 * @author gzc
 * @since 2023/6/12
 **/
@Data
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    protected Integer errCode;
    /**
     * 错误信息
     */
    protected String errMsg;

    public BusinessException() {
        super();
    }

    public BusinessException(ApiErrorEnum errorEnum) {
        super(errorEnum.getMsg());
        this.errCode = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
    }

    public BusinessException(String errMsg) {
        super(errMsg);
        this.errCode = ApiErrorEnum.ERROR.getCode();
        this.errMsg = errMsg;
    }

    public BusinessException(String errMsg, Throwable cause) {
        super(errMsg, cause);
        this.errCode = ApiErrorEnum.ERROR.getCode();
        this.errMsg = errMsg;
    }

    public BusinessException(ApiErrorEnum errorEnum, Throwable cause) {
        super(errorEnum.getMsg(), cause);
        this.errCode = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
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
