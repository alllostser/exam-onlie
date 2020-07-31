package com.exam.examquestion.feign;

import com.exam.examquestion.entity.ExamQuestion;
import org.springblade.common.response.ServerResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Component
public class IExamQuestionFallback implements IExamQuestionClient{

	@Override
	public ServerResponse addExamQuestion(ExamQuestion examQuestion) {
		return ServerResponse.serverResponseByFail(500,"服务器异常(┬＿┬)---！");
	}

	@Override
	public List<Integer> findQuestionIdsByExamId(@RequestParam("examId") Integer examId) {
		return null;
	}

	@Override
	public int deleteByExamId(@RequestParam("examId") Integer examId) {
		return 0;
	}

	@Override
	public List<ExamQuestion> selectExamQuestionListByExamId(@RequestParam("examId") Integer examId) {
		return null;
	}
}
