package com.exam.exampaper.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName ExamPaperVo
 * @Description //TODO
 * @Author GuXinYu
 * @Date 2020/5/22 13:20
 * @Version 1.0
 **/
@Data
public class ExamPaperVo {
    private static final long serialVersionUID = 922283745093904434L;

    private Integer examId;
    //试卷名
    private String examName;
    //考试开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examStartDate;
    //考试持续的时间，单位为分钟
    private Long examLastTime;
    //批阅者的id
    private Long reviewerId;
    //试卷的总分
    private BigDecimal score;

    //学生考试总分
    private BigDecimal totalScore;

    /**
     * 试题的id
     */
    private Integer[] ids;
    /**
     * 试题的id变成str
     */
    private String idsStr;

    /**
     * 当前考试是否参加
     */
    private Boolean accessed;

    /**
     * 当前试卷的学生的id
     */
    private Long studentId;
    /**
     * 学生的姓名
     */
    private String studentName;
    /**
     * 是否已经批阅
     */
    private String reading;
}
