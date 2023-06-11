package com.jason.common.service.vo;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 接口响应VO对象
 *
 * @author gzc
 * @since 2023/6/12
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JpResult<T> extends JpResultUtil implements Serializable {
    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 响应结果
     */
    private T data;
}
