package com.jason.common.service.service;

import com.jason.common.service.dto.CommonLogDTO;

/**
 * 日志业务(需要将接口请求响应信息入库则需要自行实现此接口)
 *
 * @author guozhongcheng
 * @since 2023/6/12
 */
public interface ComLogService {

    /**
     * 日志入库(入库失败会抛出异常)
     *
     * @param logDTO 通用日志DTO对象
     */
    void insert(CommonLogDTO logDTO);
}
