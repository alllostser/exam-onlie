package com.exam.record.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ExamRecord implements Serializable {
    private static final long serialVersionUID = -44611580440370702L;

    /**
     * 试卷的id
     */
    private Integer examId;
    /**
     * 学生的id
     */
    private Long stuId;
    /**
     * 试题的id
     */
    private Integer questionId;
    /**
     * 学生端考试传来的答案
     */
    private String answer;
    /**
     * 该道题的最终成绩
     */
    private BigDecimal finalScore = new BigDecimal("0");
}
