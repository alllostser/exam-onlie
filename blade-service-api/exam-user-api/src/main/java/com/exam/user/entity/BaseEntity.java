package com.exam.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建者
	 */
	protected Integer createBy;

	/**
	 * 更新者
	 */
	protected Integer updateBy;

	/**
	 * 删除标记（Y：正常；N：删除；A：审核；）
	 */
	protected Boolean delFlag;

	/**
	 * 备注
	 */
	protected String remark;

	/**
	 * 创建者
	 */
	protected SysUser createUser;

	/**
	 * 修改者
	 */
	protected SysUser updateUser;

	/**
	 * 请求参数
	 */
	private Map<String, Object> params;

	public Map<String, Object> getParams() {
		if (params == null) {
			params = new HashMap<>();
		}
		return params;
	}
}
