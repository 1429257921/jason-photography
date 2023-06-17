package com.jason.photography.admin;

import com.jason.photography.dao.enums.db.LoginStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 启动类
 *
 * @author gzc
 * @since 2022/9/30 21:57
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.jason.photography.dao.mapper")
public class JasonPhotographyAdminApplication {

    public static void main(String[] args) {
        Integer aaa = LoginStatusEnum.getValue("拉手机登录卡加斯");
        SpringApplication springApplication = new SpringApplication(JasonPhotographyAdminApplication.class);
        printConfigInfo(springApplication.run(args));
    }

    /**
     * 日志打印参数
     */
    private static void printConfigInfo(ConfigurableApplicationContext applicationContext) {
        // 获取当前部署的环境
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        log.info("部署环境->{}", StringUtils.arrayToCommaDelimitedString(activeProfiles));
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  jason摄影展管理端启动成功   ლ(´ڡ`ლ)ﾞ \s
                   _                                   _           _                              _                           _           _      \s
                  (_)                                 | |         | |                            | |                         | |         (_)     \s
                   _  __ _ ___  ___  _ __ ______ _ __ | |__   ___ | |_ ___   __ _ _ __ __ _ _ __ | |__  _   _ ______ __ _  __| |_ __ ___  _ _ __ \s
                  | |/ _` / __|/ _ \\| '_ \\______| '_ \\| '_ \\ / _ \\| __/ _ \\ / _` | '__/ _` | '_ \\| '_ \\| | | |______/ _` |/ _` | '_ ` _ \\| | '_ \\\s
                  | | (_| \\__ \\ (_) | | | |     | |_) | | | | (_) | || (_) | (_| | | | (_| | |_) | | | | |_| |     | (_| | (_| | | | | | | | | | |
                  | |\\__,_|___/\\___/|_| |_|     | .__/|_| |_|\\___/ \\__\\___/ \\__, |_|  \\__,_| .__/|_| |_|\\__, |      \\__,_|\\__,_|_| |_| |_|_|_| |_|
                 _/ |                           | |                          __/ |         | |           __/ |                                   \s
                |__/                            |_|                         |___/          |_|          |___/                                    \s
                """
        );
    }

}
