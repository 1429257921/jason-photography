package com.jason.photography.admin.service.impl;

import com.jason.photography.dao.entity.po.AdminPermission;
import com.jason.photography.dao.mapper.AdminPermissionMapper;
import com.jason.photography.admin.service.AdminPermissionService;
import com.jason.common.database.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AdminPermissionServiceImpl extends BaseServiceImpl<AdminPermissionMapper, AdminPermission> implements AdminPermissionService {


}
