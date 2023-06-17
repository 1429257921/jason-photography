package com.jason.photography.dao.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.jason.photography.dao.enums.db.DelFlagEnum;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理端角色权限关联表
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
@TableName("t_admin_role_permission")
public class AdminRolePermission implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 角色主键ID
	 */
	private Integer roleId;
	/**
	 * 权限主键ID
	 */
	private Integer permissionId;
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