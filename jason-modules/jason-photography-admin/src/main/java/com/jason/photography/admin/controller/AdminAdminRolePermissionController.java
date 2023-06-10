package com.jason.photography.admin.controller;

import com.jason.photography.admin.service.AdminAdminRolePermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端角色权限关联表控制层
 *
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class AdminAdminRolePermissionController {

    private final AdminAdminRolePermissionService adminRolePermissionService;



}
