package ${packageEntity};

import com.baomidou.mybatisplus.annotation.*;
<#assign isGenEnum = "false">
<#list columnDefinitionList as field>
    <#if field.enumType>
		<#if isGenEnum == "false">
import ${field.javaTypePackage}.*;
        <#assign isGenEnum = "true">
        </#if>
    </#if>
</#list>

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
<#assign isGenBigDecimal = "false">
<#assign isGenBigLocalDate = "false">
<#assign isGenLocalDateTime = "false">
<#list columnDefinitionList as field>
    <#if field.javaTypeName == "BigDecimal">
		<#if isGenBigDecimal == "false">
import ${field.javaTypePackage};
            <#assign isGenBigDecimal = "true">
		</#if>
	<#elseif field.javaTypeName == "LocalDateTime">
		<#if isGenBigLocalDate == "false">
import ${field.javaTypePackage};
		</#if>
        <#assign isGenBigLocalDate = "true">
	<#elseif field.javaTypeName == "LocalDate">
		<#if isGenLocalDateTime == "false">
import ${field.javaTypePackage};
        <#assign isGenLocalDateTime = "true">
        </#if>
    </#if>
</#list>

/**
 * <p>
 * ${tableComment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@TableName("${tableName}")
public class ${entityClassName} implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columnDefinitionList as field>
	/**
	 * ${field.columnComment}
	 */
    <#if field.keyFlag>
    <#-- 主键 -->
	@TableId(value = "${field.columnName}", type = IdType.AUTO)
	<#-- 普通属性 -->
	<#else>
	    <#if field.columnName == "create_time">
	@TableField(fill = FieldFill.INSERT)
	    <#elseif field.columnName == "update_time">
	@TableField(fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_EMPTY)
	    </#if>
    </#if>
	<#-- 启用状态 -->
    <#if field.columnName == "status">
		<#if field.defaultValue== "" || field.defaultValue!>
	private ${field.javaTypeName} ${field.javaFieldName};
            <#continue>
		</#if>
	private ${field.javaTypeName} ${field.javaFieldName} = ${field.defaultValue};
        <#continue>
    </#if>
    <#-- 逻辑删除注解 -->
    <#if field.columnName == "del_flag">
	@TableField("${field.columnName}")
	@TableLogic
		<#if field.defaultValue!?? || field.defaultValue?string =="">
	private ${field.javaTypeName} deleted;
            <#continue>
        </#if>
	private ${field.javaTypeName} deleted = ${field.defaultValue};
		<#continue>
    </#if>
	private ${field.javaTypeName} ${field.javaFieldName};
</#list>
<#------------  END 字段循环遍历  ---------->
}