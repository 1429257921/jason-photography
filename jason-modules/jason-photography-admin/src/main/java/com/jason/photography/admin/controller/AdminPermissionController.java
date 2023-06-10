package com.jason.photography.admin.controller;

import com.jason.photography.admin.service.AdminPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端权限表控制层
 *
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class AdminPermissionController {

    private final AdminPermissionService adminPermissionService;



}
