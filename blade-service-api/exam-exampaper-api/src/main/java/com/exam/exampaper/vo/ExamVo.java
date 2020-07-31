package com.exam.exampaper.vo;



import com.exam.user.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExamVo extends BaseEntity {
    private static final long serialVersionUID = 922283745093904434L;

    private Integer examId;
    //试卷名
    private String examName;
    //考试开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examStartDate;
    //考试持续的时间，单位为分钟
    private Long examLastTime;
    //批阅者的id
    private String reviewerIds;
    //试卷的总分
    private BigDecimal score;

    //学生考试总分
    private BigDecimal totalScore;

    /**
     * 试题的id
     */
    private String ids;

    /**
     * 选择参加考试的学生的id
     */
    private String studentIds;

    /**
     * 当前考试是否参加
     */
    private Boolean accessed;

    /**
     * 当前试卷的学生的id
     */
    private Integer studentId;
    /**
     * 学生的姓名
     */
    private String studentName;
    /**
     * 是否已经批阅
     */
    private String reading;
}
