package com.exam.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysUser implements Serializable{
    private Integer id;

    private String loginName;

    private String nickName;

    private String icon;

    private String password;

    private String salt;

    private String tel;

    private String email;

    private Byte locked;

    private Date createDate;

    private Integer createBy;

    private Date updateDate;

    private Integer updateBy;

    private String remark;

    private Byte delFlag;

    private Integer userType;
}
