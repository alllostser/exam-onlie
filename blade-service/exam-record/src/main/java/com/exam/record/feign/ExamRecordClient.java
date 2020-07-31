package com.exam.record.feign;

import com.exam.record.mapper.ExamRecordMapper;
import com.exam.student.feign.IExamStudentClient;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.Consts;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.tool.BigDecimalUtil;
import org.springblade.core.secure.utils.SecureUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.exam.record.entity.ExamRecord;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
public class ExamRecordClient implements IExamRecordClient {
	@Resource
	private ExamRecordMapper examRecordMapper;
	@Resource
	private IExamStudentClient examStudentClient;

	/**
	 * 根据examId,questionId,StudentId获取考试记录
	 *
	 * @param examId
	 * @param questionId
	 * @param studentId
	 * @return
	 */
	@Override
	@GetMapping(API_PREFIX + "/record-info-by-examId-questionId-studentId")
	public ExamRecord selectRecordByExamIdAndQuestionIdAndStuId(Integer examId, Integer questionId, Long studentId) {
		ExamRecord examRecord = examRecordMapper.selectRecordByExamIdAndQuestionIdAndStuId(examId, questionId, studentId);
		return examRecord;
	}

	/**
	 * 考试记录,考生每次答题后保存答题记录
	 *
	 * @param record
	 * @return
	 */
	@Override
	@GetMapping(API_PREFIX + "/save")
	@Transactional
	public ServerResponse insertOrUpdateRecord(ExamRecord record) {
		//非空判断
		if (record == null || record.getExamId() == null || record.getQuestionId() == null) {
			return ServerResponse.serverResponseByFail(500, "数据异常，请刷新页面重试或者联系管理员");
		}
		int result = examRecordMapper.insertOrUpdateRecord(record);
		if (result <= 0) {
			return ServerResponse.serverResponseByFail(500, "数据异常，请刷新页面重试或者联系管理员");
		} else if (result == 1) {
			return ServerResponse.serverResponseBySucess("当前答案已保存");
		} else if (result == 2) {
			return ServerResponse.serverResponseBySucess("当前答案已更新");
		} else {
			return ServerResponse.serverResponseBySucess(result);
		}
	}

	/**
	 * 更新其最终成绩
	 */
	@Override
	@GetMapping(API_PREFIX + "/final-score")
	@Transactional
	public int updateRecordFinalScore(ExamRecord examRecord) {
		int i = examRecordMapper.updateRecordFinalScore(examRecord);
		return i;
	}

	/**
	 * 教师更新最终成绩
	 *
	 * @param examRecord
	 * @return
	 */
	@Override
	@GetMapping(API_PREFIX + "/teacher-final-score")
	@Transactional
	public ServerResponse teacherReviewRecord(ExamRecord examRecord) {
		int i = updateRecordFinalScore(examRecord);
		return ServerResponse.serverResponseBySucess(i);
	}

	/**
	 * 设置完成试卷的review
	 *
	 * @param examId
	 * @param stuId
	 * @return
	 */
	@Override
	@GetMapping(API_PREFIX + "/review")
	public ServerResponse finishReview(Integer examId, Long stuId) {
		//根据Exam的id和stud的id查询出所有试题的记录，计算总得分
		List<ExamRecord> records = examRecordMapper.selectRecordByExamIdAndStuId(examId, stuId);
		//遍历record
		BigDecimal score = new BigDecimal("0");
		for (ExamRecord record : records) {
			score = BigDecimalUtil.add(score.doubleValue(), record.getFinalScore().doubleValue());
		}
		//获取登录用户id
		Long reviewerId = SecureUtil.getUserId();
		int result = examStudentClient.updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(stuId, examId, score, Consts.ExamStatusEnum.CHECK_THE_TEST_PAPER_YES, reviewerId);
		if (result <= 0) {
			return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
		}
		return ServerResponse.serverResponseBySucess(result);
	}
}
