package com.jason.photography.admin.service.impl;

import com.jason.photography.dao.entity.po.AdminRolePermission;
import com.jason.photography.dao.mapper.AdminRolePermissionMapper;
import com.jason.photography.admin.service.AdminAdminRolePermissionService;
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
public class AdminAdminRolePermissionServiceImpl extends BaseServiceImpl<AdminRolePermissionMapper, AdminRolePermission> implements AdminAdminRolePermissionService {


}
