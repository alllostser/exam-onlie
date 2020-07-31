package com.exam.question.feign;

import com.exam.question.entity.Question;
import org.springblade.common.response.ServerResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class QuestionFallback implements IQuestionClient{
	@Override
	public ServerResponse<Question> selectOneById(@RequestParam("id") Integer id) {
		return ServerResponse.serverResponseByFail(500,"服务器繁忙(┬＿┬)-----~");
	}

	@Override
	public List<Question> findQuestionListByExamId(@RequestParam("questionIds") List<Integer> questionIds) {
		return null;
	}
}
