package com.exam.exampaper.entity;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Exam implements Serializable {
    private Integer examId;

    private String examName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examStartDate;

    private Long examLastTime;

    private Integer reviewerId;

    private Integer createBy;

    private Integer updateBy;

    private Date createDate;

    private Date updateDate;

    private BigDecimal score;

}
