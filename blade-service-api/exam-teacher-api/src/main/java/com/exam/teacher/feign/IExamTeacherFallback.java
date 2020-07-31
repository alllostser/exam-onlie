package com.exam.teacher.feign;

import com.exam.teacher.entity.ExamTeacher;
import org.springblade.common.response.ServerResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
@Component
public class IExamTeacherFallback implements IExamTeacherClient{
	@Override
	public ServerResponse insert(ExamTeacher examTeacher) {
		return ServerResponse.serverResponseByFail(500,"服务器繁忙或异常(┬＿┬)-----~");
	}

	@Override
	public List<Long> selectByExamId(@RequestParam("examId") Integer examId) {
		return null;
	}

	@Override
	public int deleteByExamId(@RequestParam("examId") Integer examId) {
		return 0;
	}
}
