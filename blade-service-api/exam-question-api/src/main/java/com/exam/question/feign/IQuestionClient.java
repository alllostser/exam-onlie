package com.exam.question.feign;

import com.exam.question.entity.Question;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.response.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = CommonConstant.APPLICATION_QUESTION_NAME,
	fallback = QuestionFallback.class)
public interface IQuestionClient {

	String API_PREFIX = "/question";
	/**
	 * 通过id查找一条试题
	 * */
	@GetMapping(API_PREFIX+"/selectById")
	ServerResponse<Question> selectOneById(@RequestParam("id") Integer id);

	/**
	 * 通过ids查询试题集合
	 * */
	@GetMapping(API_PREFIX+"/findQuestionListByExamId")
	List<Question> findQuestionListByExamId(@RequestParam("questionIds") List<Integer> questionIds);

}
