package com.exam.question.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exam.question.entity.Question;
import com.exam.question.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.annotation.PreAuth;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/question")
@AllArgsConstructor
@Api(value = "试题模块", tags = "试题接口")
public class QuestionController {

	private QuestionService questionService;

	/**
	 * 通过实体作为筛选条件查询试题列表
	 *
	 * @return
	 */
	@GetMapping("/list")
	@PreAuth("hasRole('teacher')")
	@Cacheable(cacheNames = "question-list")
	@ApiOperation(value = "获取试题列表", notes = "无参数 ")
	public TableDataInfo<IPage<Question>> list(
		@RequestParam Map<String, Object> question, Query query) {
		IPage<Question> pages = questionService.page(Condition.getPage(query), Condition.getQueryWrapper(question, Question.class));
		return TableDataInfo.ResponseBySucess("成功", pages.getTotal(), pages.getRecords());
	}

	/**
	 * 添加试题
	 *
	 * @param question
	 * @return
	 */
	@PostMapping("/add")
	@PreAuth("hasRole('teacher')")
	@CacheEvict(cacheNames = "question-list")
	public ServerResponse addQuestion(@RequestBody Question question) {
		ServerResponse response = questionService.insert(question);
		return response;
	}

	/**
	 * 修改试题
	 *
	 * @param question
	 * @return
	 */
	@PutMapping("/update")
	@PreAuth("hasRole('teacher')")
	@CacheEvict(cacheNames = "question-list")
	public R updateQuestion(@RequestBody Question question) {
		boolean update = questionService.updateById(question);
		return R.status(update);
	}

	/**
	 * 删除试题
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping("/delete")
	@PreAuth("hasRole('teacher')")
	@CacheEvict(cacheNames = "question-list")
	public R deleteQuestion(String ids) {
		boolean response = questionService.removeByIds(Func.toIntList(ids));
		return R.status(response);
	}
}
