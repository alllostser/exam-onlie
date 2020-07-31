package com.exam.exampaper.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.exampaper.entity.Exam;
import com.exam.exampaper.vo.ExamVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ExamMapper extends BaseMapper<Exam> {
    /**
     * 查找一条数据byid
     * */
    Exam selectOneById(Integer examId);

    /**
     *试卷列表
     **/
    List<Exam> queryAll(Exam exam);

    /**
     * 添加试卷
     * */
    int addExam(ExamVo examVo);

    /**
     * 更新一条数据byid
     * */
    int updateExamById(ExamVo examVo);

    /**
     * 批量删除通过examid
     * */
    int delectExamByExamId(Integer[] integers);

    /**
     * 获取记录条数
     * */
    Long getCountAll();
}
