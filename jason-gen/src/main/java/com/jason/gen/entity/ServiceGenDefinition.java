package com.jason.gen.entity;

import lombok.Data;

/**
 * 服务生成配置定义信息
 *
 * @author guozhongcheng
 * @since 2023/6/19
 **/
@Data
public class ServiceGenDefinition {
    /**
     * 类型名称（Controller、Service、ServiceImpl、Entity、Mapper、MapperXml、Enum）
     */
    private String typeName;
    /**
     * 是否生成文件
     */
    private Boolean isGen = false;
    /**
     * 生成的包路径
     */
    private String packagePath;
    /**
     * 生成的本地磁盘路径
     */
    private String localPath;
}
