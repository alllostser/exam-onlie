package com.exam.student.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ScoreReturnVo
 * @Description //返回前端的成绩实体类
 * @Author GuXinYu
 * @Date 2020/5/31 0:27
 * @Version 1.0
 **/
@Data
public class ScoreReturnVo implements Serializable {
    /**
     * 学生的id
     */
    private Long stuId;
    /**
     * 学生姓名
     */
    private String stuName;

    /**
     * 阅卷人姓名
     * */
    private String reviewerName;
    /**
     * 试卷的id
     */
    private int examId;
    /**
     * 试卷名称
     */
    private String examName;
    /**
     * 总成绩
     */
    private Double totalScore;
    /**
     * 试卷是否已经批阅
     */
    private String reading;
    /**
     * 试卷状态
     */
    private String status;
}
