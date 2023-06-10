<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
	<#--<!-- 通用查询结果列 &ndash;&gt;
	<sql id="BaseSql">
        <#list table.commonFields as field>
            ${field.name},
        </#list>
        ${table.fieldNames}
	</sql>-->


</mapper>