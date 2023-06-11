package ${package.Entity};

import com.baomidou.mybatisplus.annotation.*;
<#list table.fields as field>
<#if field.annotationColumnName ? contains("status") || field.annotationColumnName ? contains("type")>
	<#if field.comment ? contains("状态") || field.comment ? contains("类型")>
	    <#if field.comment ? contains("（") && field.comment ? contains("）")>
import com.jason.photography.dao.enums.db.*;
	    </#if>
	</#if>
</#if>
</#list>
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
<#list table.fields as field>
    <#if field.propertyType = "BigDecimal">
import java.math.BigDecimal;
	<#elseif field.propertyType = "LocalDateTime">
import java.time.LocalDateTime;
	<#elseif field.propertyType = "LocalDate">
import java.time.LocalDate;
    </#if>
</#list>

/**
 * <p>
 * ${table.comment!}
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
@TableName("${table.name}")
public class ${entity} implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
	<#--/**
	* field.propertyName -> ${field.propertyName}
	* field.annotationColumnName -> ${field.annotationColumnName}
	* field.propertyType -> ${field.propertyType}
	* field.keyFlag -> ${field.keyFlag?string('true', 'false')}
	* field.name -> ${field.name}
	* field.fill -> ${field.fill???string('true', 'false')}
	* field.convert -> ${field.convert?string('true', 'false')}
	* field.keyIdentityFlag -> ${field.keyIdentityFlag?string('true', 'false')}
	*/-->
	/**
	 * ${field.comment}
	 */
    <#if field.keyFlag>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
			<#if field.annotationColumnName == field.propertyName>
	@TableId(type = IdType.AUTO)
	        <#else>
	@TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
	        </#if>
        </#if>
	<#-- 普通属性 -->
	<#elseif field.annotationColumnName != field.name>
	@TableField("${field.annotationColumnName}")
	<#else>
	    <#if field.annotationColumnName == "create_time">
	@TableField(fill = FieldFill.INSERT)
	    <#elseif field.annotationColumnName == "update_time">
	@TableField(fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_EMPTY)
	    </#if>
    </#if>
<#-- 启用状态 -->
    <#if field.annotationColumnName == "status">
	private ${field.propertyType} status = ${field.propertyType}.qy0;
        <#continue>
    </#if>
    <#-- 逻辑删除注解 -->
    <#if field.annotationColumnName == "del_flag">
	@TableField("${field.annotationColumnName}")
	@TableLogic
	private ${field.propertyType} deleted = ${field.propertyType}.zc0;
		<#continue>
    </#if>
	private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->
}