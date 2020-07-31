package com.exam.student.service;


import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;

import java.util.List;
import java.util.Map;

public interface ExamStudentService {

    /**
     * 查询考生id集合
     * */
    List<Long> findStudentIdsByExamIds(Integer examId);

    /**
     * 通过试卷id删除数据
     * */
    int deleteByExamId(Integer examId);

    /**
     *结束考试,交卷
     * */
    ServerResponse finishExam(Integer examId, Long userId);


	TableDataInfo findExamListForStu(Long l, Map<String, Object> exam, Query query);
}
