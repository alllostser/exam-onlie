package com.exam.student.feign;

import com.exam.student.entity.ExamStudent;
import com.exam.student.vo.ScoreVo;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = CommonConstant.APPLICATION_STUDENT_NAME,
	fallback = ExamStudentFallback.class)
public interface IExamStudentClient {
	String API_PREFIX = "/record";
	/**
	 * 根据studentId和exam的id更新成绩
	 * */
	@GetMapping(API_PREFIX+"/update-score")
	int updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(@RequestParam("stuId") Long stuId, @RequestParam("examId") Integer examId,@RequestParam("score") BigDecimal score, @RequestParam("reading") String reading,@RequestParam("reviewerId") Long reviewerId);

	/**
	 * 根据examId获取ExamStudent实体类的信息
	 * */
	@GetMapping(API_PREFIX+"/getbyexamId")
	List<ExamStudent> selectByExamId(@RequestParam("examId") Integer examId);

	/**
	 * 通过examId和studentId查询数据
	 * */
	@GetMapping(API_PREFIX+"/getbyexamIdAndStudentId")
	ExamStudent selectByExamIdAndStuId(@RequestParam("examId") Integer examId, @RequestParam("id")Long id);

	/**
	 * 插入数据
	 * */
	@GetMapping(API_PREFIX+"/save")
	ServerResponse insert(ExamStudent examStudent);

	/**
	 * 通过试卷id删除数据
	 * */
	@GetMapping(API_PREFIX+"/deletebyexamId")
	int deleteByExamId(@RequestParam("examId") Integer examId);

	/**
	 * 查询考生id集合
	 *
	 * @return*/
	@GetMapping(API_PREFIX+"/findStudentIdsByExamIds")
	List<Long> findStudentIdsByExamIds(@RequestParam("examId") Integer examId);

}
