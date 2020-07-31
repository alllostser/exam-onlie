package com.exam.student.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ExamStudent implements Serializable {
    private static final long serialVersionUID = -57626389942460266L;

    private Integer esId;
    //学生的id
    private Long studentId;
    //试卷的id
    private Integer examId;
    //阅卷人id
    private Long reviewerId;
    //0表示未参加考试，1表示已经参加考试
    private String status;

    //是否已经阅卷
    private String reading;

    //最终成绩
    private BigDecimal totalScore;
}
