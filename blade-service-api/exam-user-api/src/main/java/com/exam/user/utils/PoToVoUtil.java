package com.exam.user.utils;


import com.exam.user.entity.SysUser;
import com.exam.user.vo.UserVo;
import org.springblade.common.tool.TimeUtils;

public class PoToVoUtil {

    public static UserVo SysUserToVo(SysUser user){
        if (user == null){
            return null;
        }
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setLoginName(user.getLoginName());
        userVo.setNickName(user.getNickName());
        userVo.setTel(user.getTel());
        userVo.setEmail(user.getEmail());
        userVo.setIcon(user.getIcon());
        userVo.setLocked(user.getLocked());
        userVo.setCreateDate(TimeUtils.dateToStr(user.getCreateDate()));
        userVo.setUpdateDate(TimeUtils.dateToStr(user.getUpdateDate()));
        userVo.setUserType(user.getUserType()==1?"管理员":user.getUserType()==2?"教师":"考生");
        userVo.setCreateBy(user.getCreateBy());
        userVo.setUpdateBy(user.getUpdateBy());
        return userVo;
    }

}
