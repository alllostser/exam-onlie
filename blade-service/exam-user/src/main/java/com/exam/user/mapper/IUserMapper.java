package com.exam.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface IUserMapper extends BaseMapper<SysUser> {
    int deleteByPrimaryKey(Integer[] id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    /**
     *更新用户信息
     * */
    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    SysUser selectByUsernameAndPassword2(String username);

    /**
     *根据id查询用户
     * */
    SysUser findUserById(Integer id);

    /**
     * 获取用户列表
     * */
    List<SysUser> selectAll();

    /**
     * 用户模糊查询
     * */
    List<SysUser> selectByUserTypeAndkeyword(@Param("userType") Integer userType, @Param("keyword") String keyword);

    /**
     * 根据id查找教师集合
     * */
    List<SysUser> findUserByIds(List<Integer> teacherids);

    /**
     * 统计共有多少条记录
     * */
    int selectAllCount();

    /**
     * 修改密码
     * */
    int changePass(@Param("id") Integer id, @Param("newPassword") String newPassword);

	SysUser getUser(String account, String password);
}
