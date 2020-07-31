package com.exam.teacher.feign;

import com.exam.teacher.entity.ExamTeacher;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.response.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = CommonConstant.APPLICATION_EXAM_TEACHER_NAME,
	fallback = IExamTeacherFallback.class)
public interface IExamTeacherClient {
	String API_PREFIX = "/exam-teacher";

	@GetMapping(API_PREFIX+"/insert")
	ServerResponse insert(ExamTeacher examTeacher);

	@GetMapping(API_PREFIX+"/selectByexamId")
	List<Long> selectByExamId(@RequestParam("examId") Integer examId);

	/**
	 * 通过试卷id删除数据
	 * */
	@GetMapping(API_PREFIX+"/deleteByexamId")
	int deleteByExamId(@RequestParam("examId") Integer examId);
}
