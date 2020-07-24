package com.exam.user.vo;



import com.exam.user.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserVO对象", description = "UserVO对象")
public class UserVo extends BaseEntity implements Serializable {
	@JsonSerialize(using = ToStringSerializer.class)
	private Integer id;

    private String loginName;

    private String nickName;

    private String icon;

    private String tel;

    private String email;

    private Byte locked;

    private String createDate;

    private String updateDate;

    private String userType;

    //在考试管理的时候确定学生是否选中
    private Boolean studentCheckFlag = false;

}
