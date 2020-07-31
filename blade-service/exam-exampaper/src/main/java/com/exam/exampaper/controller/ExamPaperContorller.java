package com.exam.exampaper.controller;


import com.exam.exampaper.service.ExamService;
import com.exam.exampaper.vo.ExamVo;
import lombok.AllArgsConstructor;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.annotation.PreAuth;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @ClassName ExaminationPaperContorller
 * @Description //TODO
 * @Author GuXinYu
 * @Date 2020/5/18 18:22
 * @Version 1.0
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/exam/paper")
public class ExamPaperContorller {

	private ExamService examService;

	/**
	 * 试卷列表
	 *
	 * @param exam
	 * @return
	 */
	@GetMapping("/list")
	@Cacheable(cacheNames = "paper-list")
	@PreAuth("hasRole('teacher')")
	public TableDataInfo list(@RequestParam Map<String, Object> exam, Query query) {
		TableDataInfo response = examService.findExamList(exam,query);
		return response;
	}

	/**
	 * 添加试卷
	 *
	 * @param examVo
	 * @return
	 */
	@PostMapping("/add")
	@CacheEvict(cacheNames = "paper-list")
	@PreAuth("hasRole('teacher')")
	public ServerResponse addExam(@RequestBody ExamVo examVo) {
		ServerResponse response = examService.addExam(examVo);
		return response;

	}

	/**
	 * 根据id获取examVo
	 *
	 * @param examId
	 * @return
	 */
	@GetMapping("/get")
	public ServerResponse getExamPoById(Integer examId) {
		ServerResponse response = examService.getExamPoById(examId);
		return response;

	}

	/**
	 * 修改试卷
	 *
	 * @param examVo
	 * @return
	 */
	@PutMapping("/update")
	@CacheEvict(cacheNames = "paper-list")
	@PreAuth("hasRole('teacher')")
	public ServerResponse updateExam(@RequestBody ExamVo examVo) {
		ServerResponse response = examService.updateExam(examVo);
		return response;

	}

	@DeleteMapping("/delete")
	@CacheEvict(cacheNames = "paper-list")
	@PreAuth("hasRole('teacher')")
	public ServerResponse delete(String ids) {
		ServerResponse response = examService.delectExam(ids);
		return response;
	}
}
