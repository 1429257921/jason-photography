package com.jason.photography.admin.controller;

import com.jason.photography.admin.service.AdminAdminAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端用户表控制层
 *
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class AdminAdminAccountController {

    private final AdminAdminAccountService adminAccountService;



}
