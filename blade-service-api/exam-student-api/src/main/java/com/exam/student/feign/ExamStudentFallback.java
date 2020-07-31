package com.exam.student.feign;

import com.exam.student.entity.ExamStudent;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ExamStudentFallback implements IExamStudentClient{

	@Override
	public int updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(@RequestParam("stuId") Long stuId, @RequestParam("examId") Integer examId, @RequestParam("score") BigDecimal score, @RequestParam("reading") String reading, @RequestParam("reviewerId") Long reviewerId) {
		return 0;
	}

	@Override
	public List<ExamStudent> selectByExamId(@RequestParam("examId") Integer examId) {
		return null;
	}

	@Override
	public ExamStudent selectByExamIdAndStuId(@RequestParam("examId") Integer examId, @RequestParam("id")Long id) {
		return null;
	}

	@Override
	public ServerResponse insert(ExamStudent examStudent) {
		return ServerResponse.serverResponseByFail(500,"服务器繁忙或异常(┬＿┬)-----~");
	}

	@Override
	public int deleteByExamId(@RequestParam("examId") Integer examId) {
		return 0;
	}

	@Override
	public List<Long> findStudentIdsByExamIds(@RequestParam("examId") Integer examId) {
		return null;
	}

}
