package com.exam.record.feign;

import com.exam.record.entity.ExamRecord;
import org.springblade.common.response.ServerResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class IExamRecordFallback implements IExamRecordClient{
	@Override
	public ExamRecord selectRecordByExamIdAndQuestionIdAndStuId(@RequestParam Integer examId, @RequestParam Integer questionId, @RequestParam Long studentId) {
		return null;
	}

	@Override
	public ServerResponse insertOrUpdateRecord(ExamRecord record) {
		return ServerResponse.serverResponseByFail(500,"服务器繁忙或异常(┬＿┬)-----~");
	}

	@Override
	public int updateRecordFinalScore(ExamRecord examRecord) {
		return 0;
	}

	@Override
	public ServerResponse teacherReviewRecord(ExamRecord examRecord) {
		return ServerResponse.serverResponseByFail(500,"服务器繁忙或异常(┬＿┬)-----~");
	}

	@Override
	public ServerResponse finishReview(@RequestParam("examId")Integer examId, @RequestParam("stuId") Long stuId) {
		return ServerResponse.serverResponseByFail(500,"服务器繁忙或异常(┬＿┬)-----~");
	}
}
