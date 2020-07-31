package com.exam.question.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.exam.user.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 试题实体类
 * */
@Data
public class Question implements Serializable {
	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String type;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private BigDecimal score;

    private Integer createBy;

    private Date createDate;

    private Integer updateBy;

    private Date updateDate;

    private String title;

    private String answer;

    private String analyse;
}
