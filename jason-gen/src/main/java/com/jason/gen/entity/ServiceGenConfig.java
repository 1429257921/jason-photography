package com.jason.gen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 模块服务生成配置信息，主要保存每个模块服务需要生成哪些文件
 *
 * @author guozhongcheng
 * @since 2023/6/14
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceGenConfig implements Serializable {
    /**
     * 是否生成Controller
     */
    private Boolean genController;
    private String packageController;
    private String genControllerPath;
    /**
     * 是否生成Service
     */
    private Boolean genService;
    private String packageService;
    private String genServicePath;
    /**
     * 是否生成ServiceImpl
     */
    private Boolean genServiceImpl;
    private String packageServiceImpl;
    private String genServiceImplPath;
    /**
     * 是否生成表实体类
     */
    private Boolean genEntity;
    private String packageEntity;
    private String genEntityPath;
    /**
     * 是否生成mapper接口
     */
    private Boolean genMapper;
    private String packageMapper;
    private String genMapperPath;
    /**
     * 是否生成mapper.xml
     */
    private Boolean genMapperXml;
    private String genMapperXmlPath;
    /**
     * 是否生成列枚举类
     */
    private Boolean genEnum;
    private String packageEnum;
    private String genEnumPath;
}
