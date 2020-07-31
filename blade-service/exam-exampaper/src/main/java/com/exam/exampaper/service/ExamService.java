package com.exam.exampaper.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.exampaper.entity.Exam;
import com.exam.exampaper.vo.ExamVo;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;

import java.util.Map;

public interface ExamService extends IService<Exam> {
    /**
     * 试卷列表
     * */

    TableDataInfo findExamList(Map<String, Object> exam, Query query);

    /**
     * 添加试卷
     * */
    ServerResponse addExam(ExamVo examVo);

    /**
     * 修改试卷
     * */
    ServerResponse updateExam(ExamVo examVo);

    /**
     * 根据id获取examVo
     * */
    ServerResponse getExamPoById(Integer examId);

    /**
     * 删除试卷
     * */
    ServerResponse delectExam(String ids);

    /**
     * 考生获取考试列表
     *
     * @return*/
    TableDataInfo findExamListForStu(Long id, Map<String, Object> exam, Query query);

    /**
     *  根据examId获取学生的考试的试卷信息
     * */
    ServerResponse getExamForStudentByExamId(Integer examId,Long StudentId);

    /**
     * 查找需要review的试卷
     *
     * @return*/
    //TableDataInfo findExamListToReview(Exam exam, Integer teacherId,Integer pageNum, Integer pageSize, String orderBy);

    /**
     * 获取exam的详细信息，方便老师review
     * */
    ServerResponse findExamDetailToReview(Integer examId, Long studentId);
}
