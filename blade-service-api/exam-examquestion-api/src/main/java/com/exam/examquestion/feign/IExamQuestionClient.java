package com.exam.examquestion.feign;

import com.exam.examquestion.entity.ExamQuestion;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.response.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(value = CommonConstant.APPLICATION_EXAM_QUESTION_NAME,
fallback = IExamQuestionFallback.class)
public interface IExamQuestionClient {
	String API_PREFIX = "/exam-question";
	/**
	 * 添加试卷试题关联表
	 */
	@GetMapping(API_PREFIX+"/add")
	ServerResponse addExamQuestion(ExamQuestion examQuestion);

	/**
	 * 通过试卷id查找试题id
	 */
	@GetMapping(API_PREFIX+"/selectById")
	List<Integer> findQuestionIdsByExamId(@RequestParam("examId") Integer examId);

	/**
	 * 通过试卷id删除数据
	 */
	@GetMapping(API_PREFIX+"/removeByexamId")
	int deleteByExamId(@RequestParam("examId") Integer examId);

	/**
	 * 通过试卷id查找试题
	 */
	@GetMapping(API_PREFIX+"/selectByexamId")
	List<ExamQuestion> selectExamQuestionListByExamId(@RequestParam("examId") Integer examId);
}
