package ${packageEntity};

import com.baomidou.mybatisplus.annotation.*;
<#list tableDefinition.columnDefinitionsList as field>
<#if field.columnName ? contains("status") || field.columnName ? contains("type")>
	<#if field.columnComment ? contains("状态") || field.columnComment ? contains("类型")>
	    <#if field.columnComment ? contains("（") && field.columnComment ? contains("）")>
import com.jason.photography.dao.enums.db.*;
	    </#if>
	</#if>
</#if>
</#list>
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
<#list tableDefinition.columnDefinitionsList as field>
    <#if field.javaTypeName = "BigDecimal">
import java.math.BigDecimal;
	<#elseif field.javaTypeName = "LocalDateTime">
import java.time.LocalDateTime;
	<#elseif field.javaTypeName = "LocalDate">
import java.time.LocalDate;
    </#if>
</#list>

/**
 * <p>
 * ${tableName!}
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
public class ${outputFileName} implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list tableDefinition.columnDefinitionsList as field>
	<#--/**
	* field.propertyName -> ${field.propertyName}
	* field.annotationColumnName -> ${field.annotationColumnName}
	* field.propertyType -> ${field.propertyType}
	* field.keyFlag -> ${field.keyFlag?string('true', 'false')}
	* field.name -> ${field.name}
	* field.fill -> ${field.fill???string('true', 'false')}
	* field.convertData -> ${field.convertData?string('true', 'false')}
	* field.keyIdentityFlag -> ${field.keyIdentityFlag?string('true', 'false')}
	*/-->
	/**
	 * ${field.columnComment}
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