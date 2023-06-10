package com.jason.common.database.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jason.common.database.service.BaseService;
import lombok.extern.slf4j.Slf4j;

/**
 * 公共service实现类
 *
 * @author guozhongcheng
 * @since 2023/6/9 11:04
 */
@Slf4j
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

}
