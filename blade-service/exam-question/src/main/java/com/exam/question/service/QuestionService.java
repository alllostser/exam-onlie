package com.exam.question.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.question.entity.Question;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;

import java.util.List;

public interface QuestionService extends IService<Question> {
    /**
     * 获取试题列表
     * */
    TableDataInfo findQuestionList(Question question, Integer pageNum, Integer pageSize, String orderBy);

    /**
     * 添加试题
     * */
    ServerResponse insert(Question question);

    /**
     * 修改试题
     * */
    ServerResponse updateQuestion(Question question);

    /**
     * 删除试题
     * */
    ServerResponse deleteQuestion(Integer id);

    /**
     * 批量删除
     * */
    ServerResponse deleteQuestion(String ids);

    /**
     * 通过id查找一条试题
     * */
    ServerResponse selectOneById(Integer id);

    /**
     * 通过ids查询试题集合
     * */
    List<Question> findQuestionListByExamId(List<Integer> questionIds);
}
