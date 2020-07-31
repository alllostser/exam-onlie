package com.exam.teacher.feign;

import com.exam.teacher.entity.ExamTeacher;
import com.exam.teacher.mapper.ExamTeacherMapper;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.Consts;
import org.springblade.common.response.ServerResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class ExamTeacherClient implements IExamTeacherClient{
	@Resource
	private ExamTeacherMapper examTeacherMapper;
	@Override
	@GetMapping(API_PREFIX+"/insert")
	@Transactional
	public ServerResponse insert(ExamTeacher examTeacher) {
		if (examTeacher !=null){
			int insert = examTeacherMapper.insert(examTeacher);
			if (insert<=0){
				return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(),Consts.StatusEnum.UPDATA_FAILED.getDesc());
			}
			return ServerResponse.serverResponseBySucess(insert);
		}
		return ServerResponse.serverResponseByFail(1,"参数不能为空");
	}

	@Override
	@GetMapping(API_PREFIX+"/selectByexamId")
	public List<Long> selectByExamId(Integer examId) {
		if (examId != null){
			List<Long> teacherIds = examTeacherMapper.selectByExamId(examId);
			return teacherIds;
		}
		return new ArrayList<Long>();
	}

	/**
	 * 通过试卷id删除数据
	 * @param examId
	 * @return
	 */
	@Override
	@GetMapping(API_PREFIX+"/deleteByexamId")
	@Transactional
	public int deleteByExamId(Integer examId) {
		int result = examTeacherMapper.deleteByExamId(examId);
		return result;
	}
}
