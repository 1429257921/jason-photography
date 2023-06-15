package ${packageEntity};

import com.baomidou.mybatisplus.annotation.*;
<#list columnDefinitionList as field>
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
<#list columnDefinitionList as field>
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
	private ${field.javaTypeName} status = ${field.javaTypeName}.qy0;
        <#continue>
    </#if>
    <#-- 逻辑删除注解 -->
    <#if field.columnName == "del_flag">
	@TableField("${field.columnName}")
	@TableLogic
	private ${field.javaTypeName} deleted = ${field.javaTypeName}.zc0;
		<#continue>
    </#if>
	private ${field.javaTypeName} ${field.javaFieldName};
</#list>
<#------------  END 字段循环遍历  ---------->
}