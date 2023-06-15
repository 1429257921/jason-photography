package com.jason.gen.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO
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
    private String genControllerPath;
    /**
     * 是否生成Service
     */
    private Boolean genService;
    private String genServicePath;
    /**
     * 是否生成ServiceImpl
     */
    private Boolean genServiceImpl;
    private String genServiceImplPath;
    /**
     * 是否生成表实体类
     */
    private Boolean genEntity;
    private String genEntityPath;
    /**
     * 是否生成mapper接口
     */
    private Boolean genMapper;
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
    private String genEnumPath;
}
