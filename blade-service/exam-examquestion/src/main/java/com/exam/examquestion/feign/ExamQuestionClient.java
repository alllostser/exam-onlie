package com.exam.examquestion.feign;

import com.exam.examquestion.entity.ExamQuestion;
import com.exam.examquestion.mapper.ExamQuestionMapper;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.Consts;
import org.springblade.common.response.ServerResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@AllArgsConstructor
public class ExamQuestionClient implements IExamQuestionClient {
	private ExamQuestionMapper examQuestionMapper;

	/**
	 * 添加试卷试题关联表
	 *
	 * @param examQuestion
	 * @return
	 */
	@Override
	@GetMapping(API_PREFIX+"/add")
	@Transactional
	public ServerResponse addExamQuestion(ExamQuestion examQuestion) {
		//1.非空判断
		if (examQuestion == null) {
			return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
		}
		//2.操作数据库
		int result = examQuestionMapper.insert(examQuestion);
		if (result <= 0) {
			return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
		}
		return ServerResponse.serverResponseBySucess(result);
	}

	/**
	 * 通过试卷id查找试题id
	 *
	 * @param examId
	 * @return
	 */
	@Override
	@GetMapping(API_PREFIX+"/selectById")
	public List<Integer> findQuestionIdsByExamId(Integer examId) {
		List<Integer> questionIds = examQuestionMapper.findQuestionIdsByExamId(examId);
		return questionIds;
	}

	/**
	 * 通过试卷id删除数据
	 */
	@Override
	@GetMapping(API_PREFIX+"/removeByexamId")
	@Transactional
	public int deleteByExamId(Integer examId) {
		int result = examQuestionMapper.deleteByExamId(examId);
		return result;
	}

	/**
	 * 通过试卷id查找试题
	 *
	 * @param examId
	 * @return
	 */
	@Override
	@GetMapping(API_PREFIX+"/selectByexamId")
	public List<ExamQuestion> selectExamQuestionListByExamId(Integer examId) {
		List<ExamQuestion> examQuestions = examQuestionMapper.selectExamQuestionListByExamId(examId);
		return examQuestions;
	}
}
