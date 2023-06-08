package com.jason.photography.admin;

import lombok.extern.slf4j.Slf4j;
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
public class JasonPhotographyAdminApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(JasonPhotographyAdminApplication.class);
        // 允许循环依赖
        springApplication.setAllowCircularReferences(true);
        printConfigInfo(springApplication.run(args));
    }

    /**
     * 日志打印参数
     */
    private static void printConfigInfo(ConfigurableApplicationContext applicationContext) {
        // 获取当前部署的环境
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        log.info("部署环境->{}", StringUtils.arrayToCommaDelimitedString(activeProfiles));
        System.out.println("(♥◠‿◠)ﾉﾞ  坤坤博客系统启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " _                      _     _             \n" +
                "| |                    | |   | |            \n" +
                "| | ___   _ _ __ ______| |__ | | ___   __ _ \n" +
                "| |/ / | | | '_ \\______| '_ \\| |/ _ \\ / _` |\n" +
                "|   <| |_| | | | |     | |_) | | (_) | (_| |\n" +
                "|_|\\_\\\\__,_|_| |_|     |_.__/|_|\\___/ \\__, |\n" +
                "                                       __/ |\n" +
                "                                      |___/ \n" +
                "\n"
        );
    }

}
