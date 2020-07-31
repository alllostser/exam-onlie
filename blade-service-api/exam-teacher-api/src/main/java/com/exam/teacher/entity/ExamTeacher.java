package com.exam.teacher.entity;

import lombok.Data;

/**
 * 教师与试卷关联实体类
 * */
@Data
public class ExamTeacher {
    private Integer id;

    private Integer examId;

    private Long teacherId;

}
