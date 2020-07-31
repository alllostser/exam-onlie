package com.exam.teacher.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.teacher.entity.ExamTeacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ExamTeacherMapper extends BaseMapper<ExamTeacher> {

    int insert(ExamTeacher record);

    List<Long> selectByExamId(Integer examId);

    /**
     * 通过试卷id删除数据
     * */
    int deleteByExamId(Integer examId);
}
