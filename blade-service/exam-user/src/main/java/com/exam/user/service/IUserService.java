package com.exam.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.user.entity.SysUser;
import com.exam.user.entity.UserInfo;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;

import java.util.List;

public interface IUserService extends IService<SysUser> {
	/**
	 * 添加用户
	 * */
	ServerResponse addUser(SysUser user);

	/**
	 * 根据id查找用户
	 * */
	ServerResponse findUserById(Integer id);

	/**
	 * 根据id更新用户
	 * */
	ServerResponse updateUser(SysUser sysUser);

	/**
	 * 获取用户列表
	 *
	 * @return*/
	TableDataInfo userList(Integer userType, String keyword, Integer pageNum, Integer pageSize, String orderBy);

	/**
	 * 删除用户
	 * */
	ServerResponse deleteSysUserByIds(String ids);

	/**
	 *登录
	 */
	SysUser findSysUserByLoginName(String username);

	/**
	 * 根据id查找教师集合
	 * */
	List<SysUser> findUserByIds(List<Integer> teacherids);

	/**
	 * 修改密码
	 * */
	ServerResponse changePass(String oldPassword, String newPassword, String loginName);

	/**
	 * 用户信息
	 *
	 * @param tenantId
	 * @param account
	 * @param password
	 * @return
	 */
	UserInfo userInfo(String tenantId, String account, String password);
}
