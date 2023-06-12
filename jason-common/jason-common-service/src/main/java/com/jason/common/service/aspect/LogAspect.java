package com.jason.common.service.aspect;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jason.common.service.anno.JpApi;
import com.jason.common.service.context.JpContextUtil;
import com.jason.common.service.dto.CommonLogDTO;
import com.jason.common.service.enums.CommonLogTypeEnum;
import com.jason.common.service.exception.Assert;
import com.jason.common.service.init.InitJpApiData;
import com.jason.common.service.service.ComLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 请求响应日志切面
 *
 * @author guozhongcheng
 * @since 2023/6/12
 */
@Slf4j
@Aspect
@SuppressWarnings("unused")
public record LogAspect(ComLogService comLogService) {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 环绕通知(切入点为注解)
     */
    @Around("@annotation(com.jason.common.service.anno.JpApi)")
    public Object aroundApiLogInsertDb(ProceedingJoinPoint joinPoint) throws Throwable {
        Object response = null;
        Exception exception = null;
        // 起始时间
        DateTime beginTime = DateUtil.date();
        // 结束时间
        DateTime endTime = DateUtil.date();
        String apiMsg = "";
        try {
            apiMsg = InitJpApiData.getJpApi();
            // 请求参数输出日志
            reqLogPrint(apiMsg, joinPoint);
            log.info("开始执行{} 业务逻辑", apiMsg);
            try {
                response = joinPoint.proceed();
            } catch (Exception exp) {
                exception = exp;
            }
            // 结束时间
            endTime = DateUtil.date();

            if (exception != null) {
                throw exception;
            }
            // 获取返回值
            Object result = response;

            // 正常信息日志入库
            CommonLogDTO logDTO = getLogInfo(apiMsg, joinPoint, result, null,
                    CommonLogTypeEnum.SYSTEM_LOG.getCode(), beginTime, endTime);

            this.insertDb(apiMsg, logDTO, joinPoint);
        } catch (Throwable th) {
            // 结束时间
            endTime = DateUtil.date();
            if (th instanceof Exception) {
                exception = (Exception) th;
            }
            // 错误信息日志入库
            CommonLogDTO logDTO = getLogInfo(apiMsg, joinPoint, null, exception,
                    CommonLogTypeEnum.ERROR_LOG.getCode(), beginTime, endTime);
            this.insertDb(apiMsg, logDTO, joinPoint);
            throw th;
        } finally {
            log.info("结束执行{} 业务逻辑, 耗时->{}ms", apiMsg, DateUtil.betweenMs(beginTime, endTime));
        }
        return response;
    }

    /**
     * 日志入库
     *
     * @param apiMsg    接口描述
     * @param logDTO    日志对象
     * @param joinPoint 切入点
     */
    private void insertDb(String apiMsg, CommonLogDTO logDTO, ProceedingJoinPoint joinPoint) {
        JpApi jpApi = getJpApi(joinPoint);
        try {
            // 判断是否入库
            if (jpApi != null && jpApi.reqLogInsertDB()) {
                if (comLogService != null) {
                    comLogService.insert(logDTO);
                }
            }
        } catch (Exception e) {
            log.error("{}切面日志入库发生异常, 异常原因->{}", apiMsg, e);
        }
    }

    /**
     * 打印请求参数日志
     *
     * @param joinPoint 连接点对象
     */
    private void reqLogPrint(String apiMsg, ProceedingJoinPoint joinPoint) {
        JpApi jpApi = getJpApi(joinPoint);
        log.info("请求地址->{}", JpContextUtil.getServletPath());
        // 是否打印请求参数
        if (jpApi != null && jpApi.printReqParam()) {
            String reqParam = reqParamFilerFile(joinPoint);
            log.info("{}请求参数->\n{}", jpApi.value(), reqParam);
            Assert.notBlank(reqParam, "请求参数为空");
        }
    }

    /**
     * 请求参数过滤
     */
    private String reqParamFilerFile(ProceedingJoinPoint joinPoint) {
        String bodyParam = JpContextUtil.getBodyParam();
        String urlParam = JpContextUtil.getUrlParam();
        if (StrUtil.isAllBlank(bodyParam, urlParam)) {
            return "";
        }
        return "body请求参数->" +
                bodyParam +
                "\n" +
                "url请求参数->" +
                urlParam;
    }

    /**
     * 获取注解
     *
     * @param joinPoint 切入点
     * @return 注解信息
     */
    private JpApi getJpApi(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod().getAnnotation(JpApi.class);
    }

    /**
     * 构建Log对象
     *
     * @param joinPoint 连接点对象
     * @param result    正常返回结果
     * @param exception 错误返回结果
     * @param logType   日志类型
     * @param beginTime 起始时间
     * @param endTime   结束时间
     */
    private CommonLogDTO getLogInfo(String apiMsg, ProceedingJoinPoint joinPoint, Object result,
                                    Exception exception, Integer logType,
                                    DateTime beginTime, DateTime endTime) {
        CommonLogDTO logDTO = new CommonLogDTO();
        try {
            // 日志类型
            logDTO.setLogType(logType);
            // 起始时间
            logDTO.setBeginTime(beginTime.toString());
            // 结束时间
            logDTO.setEndTime(endTime.toString());
            // 接口调用耗时
            logDTO.setCostTime(DateUtil.betweenMs(beginTime, endTime));
            // 接口描述
            logDTO.setApiMsg(apiMsg);
            // 获取请求路径
            logDTO.setRequestUrl(JpContextUtil.getServletPath());

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取异常信息
            if (exception != null) {
                logDTO.setResponse(ExceptionUtil.stacktraceToOneLineString(exception));
            }
            // 获取接口返回结果
            else if (ObjectUtil.isNotEmpty(result)) {
                String resultJson = serial(result);
                logDTO.setResponse(resultJson);
            }
            // 获取类的包路径
            String signName = signature.getDeclaringTypeName();
            logDTO.setReqClass(signName);
            // 获取方法名
            logDTO.setMethodName(signature.getName());
            // 获取请求参数
            Object[] args = joinPoint.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                logDTO.setRequest(serial(args));
            }
        } catch (Exception e) {
            log.error("切面封装日志信息异常->", e);
        }
        return logDTO;
    }

    private static String serial(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception ex) {
            return obj.toString();
        }
    }

}
