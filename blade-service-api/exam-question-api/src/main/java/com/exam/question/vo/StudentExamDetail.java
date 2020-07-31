package com.exam.question.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 给前端传递考试数据的载体
 */
@Data
public class StudentExamDetail implements Serializable {
    private Integer examId;
    //试卷名称
    private String examName;
    //考生id
    private Long studentId;
    //考试持续时间
    private Long lastTime;
    //考试开始时间
    private String startDate;
    //试卷的总分
    private BigDecimal score;
    //学生考试的总分
    private BigDecimal totalScore;

    private List<QuestionVo> radioQuestion;
    private List<QuestionVo> checkboxQuestion;
    private List<QuestionVo> judgeQuestion;
    private List<QuestionVo> shortQuestion;
    private List<QuestionVo> balckQuestion;
}
