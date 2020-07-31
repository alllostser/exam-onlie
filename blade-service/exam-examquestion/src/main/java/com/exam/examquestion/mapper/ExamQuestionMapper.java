package com.exam.examquestion.mapper;

import com.exam.examquestion.entity.ExamQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExamQuestionMapper{
	int deleteByPrimaryKey(Integer eqId);

	/**
	 * 添加试卷试题关联表
	 */
	int insert(ExamQuestion record);

	int insertSelective(ExamQuestion record);

	ExamQuestion selectByPrimaryKey(Integer eqId);

	int updateByPrimaryKeySelective(ExamQuestion record);

	int updateByPrimaryKey(ExamQuestion record);

	/**
	 * 通过试卷id查找试题id
	 */
	List<Integer> findQuestionIdsByExamId(Integer examId);

	/**
	 * 通过试卷id删除数据
	 */
	int deleteByExamId(Integer examId);

	/**
	 * 根据Exam的id查询出所有的examQuestion实体
	 */
	List<ExamQuestion> selectExamQuestionListByExamId(Integer examId);
}
