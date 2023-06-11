package com.jason.photography.dao.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.jason.photography.dao.enums.db.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理端角色表
 * </p>
 *
 * @author guozhongcheng
 * @since 2023-06-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@TableName("t_admin_role")
public class AdminRole implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 排序
	 */
	private String roleFlag;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 启用状态（0、正常，1、禁用）
	 */
	private StatusEnum status = StatusEnum.qy0;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 删除标志（0、正常，1、删除）
	 */
	@TableField("del_flag")
	@TableLogic
	private DelFlagEnum deleted = DelFlagEnum.zc0;
	/**
	 * 创建者ID
	 */
	private Integer createId;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;
	/**
	 * 更新者ID
	 */
	private Integer updateId;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_EMPTY)
	private LocalDateTime updateTime;
}