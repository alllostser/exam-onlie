package com.exam.student.controller;


import com.exam.exampaper.feign.IExamClient;
import com.exam.record.entity.ExamRecord;
import com.exam.record.feign.IExamRecordClient;
import com.exam.student.service.ExamStudentService;
import lombok.AllArgsConstructor;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;

import org.springblade.core.secure.utils.SecureUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 考生考试控制层
 */
@RestController
@RequestMapping("/student/exam")
@AllArgsConstructor
public class ExamStudentController {
	private ExamStudentService examStudentService;
	private IExamClient examClient;
	private IExamRecordClient examRecordClient;


	/**
	 * 考生获取考试列表
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/list")
//	@Cacheable(cacheNames = "listforstudent")
	public TableDataInfo list(@RequestParam Map<String, Object> exam, Query query) {
		//获取当前登录用户
		Long userId = SecureUtil.getUserId();
		TableDataInfo examList = examStudentService.findExamListForStu(userId ,exam,query);
		return examList;
	}

	/**
	 * 开始考试，获取试题列表/获取考试详情
	 *
	 * @param examId
	 * @return
	 */
	@GetMapping("/detail")
	public ServerResponse startExam(Integer examId) {
		//获取当前登录用户
		Long userId = SecureUtil.getUserId();
		ServerResponse examPoResponse = examClient.getExamForStudentByExamId(examId, userId);
		return examPoResponse;
	}

	/**
	 * 考试记录,考生每次答题后调用
	 *
	 * @param record
	 * @return
	 */
	@PostMapping("/record")
	public ServerResponse record(ExamRecord record) {
		//获取当前登录用户
		Long userId = SecureUtil.getUserId();
		record.setStuId(userId);
		ServerResponse response = examRecordClient.insertOrUpdateRecord(record);
		return response;
	}

	/**
	 * 结束考试,交卷接口
	 *
	 * @param examId
	 * @return
	 */
	@PostMapping("/submit")
	public ServerResponse submitPaper(Integer examId) {
		Long userId = SecureUtil.getUserId();
		ServerResponse response = examStudentService.finishExam(examId, userId);
		return response;
	}

}
