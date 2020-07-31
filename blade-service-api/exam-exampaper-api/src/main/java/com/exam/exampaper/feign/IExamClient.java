package com.exam.exampaper.feign;

import org.springblade.common.constant.CommonConstant;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = CommonConstant.APPLICATION_EXAM_PAPER_NAME,
	fallback = IExamClientFallback.class)
public interface IExamClient {
	String API_PREFIX = "/exam-paper";
	/**
	 * 考生获取考试列表
	 *
	 * @return*/
	@RequestMapping(value = API_PREFIX+"/getlist",method = RequestMethod.GET)
	TableDataInfo findExamListForStu(@RequestParam("l") Long l,@RequestParam("examJson") String examJson, Query query);

	/**
	 *  根据examId获取学生的考试的试卷信息
	 * */
	@GetMapping(API_PREFIX+"/getdetail")
	ServerResponse getExamForStudentByExamId(@RequestParam Integer examId,@RequestParam Long StudentId);

	/**
	 * 查找需要review的试卷
	 *
	 * @return*/
	@GetMapping(API_PREFIX+"/getreview")
	TableDataInfo findExamListToReview(@RequestParam("teacherId") Long teacherId,@RequestParam("examJson") String examJson, Query query);

	/**
	 * 获取exam的详细信息，方便老师review
	 * */
	@GetMapping(API_PREFIX+"/getexamtoreview")
	ServerResponse findExamDetailToReview(@RequestParam("examId")Integer examId, @RequestParam("studentId")Long studentId);
}
