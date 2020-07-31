package com.exam.question.feign;

import com.exam.question.entity.Question;
import com.exam.question.mapper.QuestionMapper;
import com.exam.question.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.Consts;
import org.springblade.common.response.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class QuestionClient implements IQuestionClient{
	private QuestionMapper questionMapper;
	private QuestionService questionService;
	@Override
	@GetMapping(API_PREFIX+"/selectById")
	public ServerResponse<Question> selectOneById(Integer id) {
		if (id == null){
			return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
		}
		//操作数据库
		Question question = questionMapper.selectOneById(id);
		return ServerResponse.serverResponseBySucess(question);
	}

	@Override
	@GetMapping(API_PREFIX+"/findQuestionListByExamId")
	public List<Question> findQuestionListByExamId(List<Integer> questionIds) {
		List<Question> questions = questionService.listByIds(questionIds);
		return questions;
	}
}
