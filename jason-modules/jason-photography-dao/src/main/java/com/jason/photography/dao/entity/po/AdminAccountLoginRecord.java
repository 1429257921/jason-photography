package com.jason.photography.dao.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.jason.photography.dao.enums.db.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理端用户登录记录表
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
@TableName("t_admin_account_login_record")
public class AdminAccountLoginRecord implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 管理端用户主键ID
	 */
	private Integer adminId;
	/**
	 * 登录结果类型（0、登录成功，1、登出成功，2、登录失败，3、登出失败）
	 */
	private AdminAccountLoginRecordLoginResultTypeEnum loginResultType;
	/**
	 * 客户端IP
	 */
	private String clientIp;
	/**
	 * 客户端版本号
	 */
	private String clientVersion;
	/**
	 * 客户端设备硬件地址
	 */
	private String clientMac;
	/**
	 * 客户端系统名称
	 */
	private String clientOsName;
	/**
	 * 客户端系统版本
	 */
	private String clientOsVersion;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;
}