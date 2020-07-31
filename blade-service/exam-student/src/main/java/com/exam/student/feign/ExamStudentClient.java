package com.exam.student.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.student.entity.ExamStudent;
import com.exam.student.mapper.ExamStudentMapper;
import com.exam.student.service.ExamStudentService;
import com.exam.student.vo.ScoreReturnVo;
import com.exam.student.vo.ScoreVo;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.Consts;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.common.tool.GuavaCacheUtils;
import org.springblade.core.mp.support.Query;
import org.springblade.system.user.feign.IUserClient;
import org.springblade.system.user.vo.UserVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ExamStudentClient implements IExamStudentClient{
	private ExamStudentMapper examStudentMapper;
	private ExamStudentService examStudentService;
	private IUserClient userClient;
	@Override
	@GetMapping(API_PREFIX+"/update-score")
	@Transactional
	public int updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(Long stuId, Integer examId, BigDecimal score, String reading, Long reviewerId) {
		int result = examStudentMapper.updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(stuId, examId, score, reading,reviewerId);
		return result;
	}

	@Override
	@GetMapping(API_PREFIX+"/getbyexamId")
	public List<ExamStudent> selectByExamId(Integer examId) {
		List<ExamStudent> examStudents = examStudentMapper.selectByExamId(examId);
		return examStudents;
	}

	@Override
	@GetMapping(API_PREFIX+"/getbyexamIdAndStudentId")
	public ExamStudent selectByExamIdAndStuId(Integer examId, Long id) {
		ExamStudent examStudent = examStudentMapper.selectByExamIdAndStuId(examId,id);
		return examStudent;
	}

	@Override
	@GetMapping(API_PREFIX+"/save")
	@Transactional
	public ServerResponse insert(ExamStudent examStudent) {
		if (examStudent == null){
			return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(),Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
		}
		//操作数据库
		int insert = examStudentMapper.insert(examStudent);
		if (insert<=0){
			return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
		}
		return ServerResponse.serverResponseBySucess(insert);
	}

	@Override
	@GetMapping(API_PREFIX+"/deletebyexamId")
	@Transactional
	public int deleteByExamId(Integer examId) {
		int i = examStudentService.deleteByExamId(examId);
		return i;
	}

	@Override
	@GetMapping(API_PREFIX+"/findStudentIdsByExamIds")
	public List<Long> findStudentIdsByExamIds(Integer examId) {
		List<Long> studentIdsByExamIds = examStudentService.findStudentIdsByExamIds(examId);
		return studentIdsByExamIds;
	}

}
