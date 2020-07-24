package com.exam.user.controller;

import com.exam.user.entity.SysUser;
import com.exam.user.service.IUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	//注入service层
	IUserService sysUserService;

	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@PostMapping("/add.do")
	public ServerResponse addUser(@RequestBody SysUser user){
			ServerResponse response = sysUserService.addUser(user);
			return response;

	}


	@RequestMapping("/select.do")
	public ServerResponse findUserById(Integer id){
			ServerResponse response = sysUserService.findUserById(id);
			return response;
	}

	/**
	 *更新用户信息
	 * @param sysUser
	 * @return
	 */
	@PostMapping("/update.do")
	public ServerResponse updateUser(@RequestBody SysUser sysUser) {

			ServerResponse response = sysUserService.updateUser(sysUser);
			return response;
	}

	/**
	 * 用户列表搜索+动态排序
	 * @param keyword  关键字
	 * @param pageNum 第几页
	 * @param pageSize 一页多少条数据
	 * @param orderBy 排序字段filedname_desc/filedname_asc
	 * @return
	 */
	@GetMapping("/list.do")
	@CrossOrigin
	public TableDataInfo list(
		@RequestParam(required = false)Integer userType,
		@RequestParam(required = false,defaultValue = "")String keyword,
		@RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
		@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
		@RequestParam(required = false,defaultValue = "")String orderBy
	) {
			TableDataInfo sysUsers = sysUserService.userList(userType,keyword,pageNum,pageSize,orderBy);
			return sysUsers;
	}

	/**
	 * 删除用户
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete.do")
	public ServerResponse delete(String ids) {
			ServerResponse response = sysUserService.deleteSysUserByIds(ids);
			return response;
	}

//	/**
//	 * 修改密码
//	 * @param oldPassword
//	 * @param newPassword
//	 * @return
//	 */
//	@PostMapping("changePass.do")
//	public ServerResponse changePass(String oldPassword,String newPassword){
//		SysUser loginUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
//		ServerResponse response = sysUserService.changePass(oldPassword,newPassword,loginUser.getLoginName());
//		return response;
//	}

}
