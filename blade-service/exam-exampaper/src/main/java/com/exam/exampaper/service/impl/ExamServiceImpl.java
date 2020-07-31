package com.exam.exampaper.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.exampaper.bo.ExamBo;
import com.exam.exampaper.entity.Exam;
import com.exam.exampaper.mapper.ExamMapper;
import com.exam.exampaper.service.ExamService;
import com.exam.exampaper.vo.ExamReturnVo;
import com.exam.exampaper.vo.ExamVo;
import com.exam.examquestion.entity.ExamQuestion;
import com.exam.examquestion.feign.IExamQuestionClient;
import com.exam.question.entity.Question;
import com.exam.question.feign.IQuestionClient;
import com.exam.question.vo.QuestionVo;
import com.exam.question.vo.StudentExamDetail;
import com.exam.record.entity.ExamRecord;
import com.exam.record.feign.IExamRecordClient;
import com.exam.student.entity.ExamStudent;
import com.exam.student.feign.IExamStudentClient;
import com.exam.teacher.entity.ExamTeacher;
import com.exam.teacher.feign.IExamTeacherClient;
import org.springblade.common.constant.Consts;
import org.springblade.common.tool.BigDecimalUtil;
import org.springblade.common.tool.Convert;
import org.springblade.common.tool.GuavaCacheUtils;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.system.user.entity.User;
import org.springblade.system.user.vo.UserVO;
import lombok.AllArgsConstructor;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.common.tool.TimeUtils;
import org.springblade.system.user.feign.IUserClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ExamServiceImpl extends ServiceImpl<ExamMapper,Exam> implements ExamService {
    private ExamMapper examMapper;
	private IUserClient iUserClient;
    private IExamQuestionClient examQuestionClient;
    private IQuestionClient questionClient;
    private IExamStudentClient examStudentClient;
    private IExamTeacherClient examTeacherClient;
    private IExamRecordClient examRecordClient;

    @Override
    public TableDataInfo findExamList(Map<String, Object> exam, Query query) {

		IPage<Exam> examIPage = baseMapper.selectPage(Condition.getPage(query), Condition.getQueryWrapper(exam, Exam.class));
		List<Exam> exams = examIPage.getRecords();
		if (exams == null || exams.size() <= 0) {
            return TableDataInfo.ResponseByFail(404, "没有找到任何试卷信息");
        }
        //将返回exams转为examReturnVo返回
        List<ExamReturnVo> examReturnVoList = new ArrayList<>();
        for (Exam temp : exams) {
            ExamReturnVo examReturnVo = new ExamReturnVo();
            if (temp.getCreateBy()!=null){
                ServerResponse createBy = iUserClient.findUserById(temp.getCreateBy().longValue());
                if (createBy.isSucess()){
					User user = (User) createBy.getData();
					examReturnVo.setCreateBy(user.getName());
				}
            }
            if (temp.getUpdateBy()!=null){
                ServerResponse updateBy = iUserClient.findUserById(temp.getUpdateBy().longValue());
                if (updateBy.isSucess()){
					 User user = (User) updateBy.getData();
					examReturnVo.setUpdateBy(user.getName());
				}
            }

            //获取判卷人id
            List<Long> ReviewerIds = examTeacherClient.selectByExamId(temp.getExamId());
            if (ReviewerIds.size()>0){
                StringBuffer reviewerIdsBuffer = new StringBuffer();
                for (Long reviewerId : ReviewerIds) {
                    reviewerIdsBuffer.append(reviewerId+",");
                }
                String reviewerIds = reviewerIdsBuffer.toString();
                reviewerIds=reviewerIds.substring(0, reviewerIds.length() -1);
                examReturnVo.setReviewerIds(reviewerIds);
            }
            //获取考生id
            List<ExamStudent> examStudents = examStudentClient.selectByExamId(temp.getExamId());
            if (examStudents.size()>0){
                StringBuffer studentIdsBuffer = new StringBuffer();
                for (ExamStudent examStudent : examStudents) {
                    studentIdsBuffer.append(examStudent.getStudentId()+",");
                }
                String studentIds = studentIdsBuffer.toString();
                studentIds = studentIds.substring(0, studentIds.length()-1);
                examReturnVo.setStudentIds(studentIds);
            }

            //获取试题id
            List<ExamQuestion> examQuestions = examQuestionClient.selectExamQuestionListByExamId(temp.getExamId());
            if (examQuestions.size()>0){
                StringBuffer questionIdsBuffer = new StringBuffer();
                for (ExamQuestion examQuestion : examQuestions) {
                    questionIdsBuffer.append(examQuestion.getQuestionId()+",");
                }
                String questionIds = questionIdsBuffer.toString();
                questionIds = questionIds.substring(0, questionIds.length()-1);
                examReturnVo.setIds(questionIds);
            }
            examReturnVo.setExamId(temp.getExamId());
            examReturnVo.setExamName(temp.getExamName());
            examReturnVo.setExamStartDate(TimeUtils.dateToStr(temp.getExamStartDate()));
            examReturnVo.setExamLastTime(temp.getExamLastTime());
            examReturnVo.setCreateDate(TimeUtils.dateToStr(temp.getCreateDate(),"yyyy-MM-dd"));
            examReturnVo.setUpdateDate(TimeUtils.dateToStr(temp.getUpdateDate(),"yyyy-MM-dd"));
            examReturnVo.setScore(temp.getScore());
            examReturnVoList.add(examReturnVo);
        }
		return TableDataInfo.ResponseBySucess("",examIPage.getTotal(),examReturnVoList);
	}



	/**
	 * 添加考试
	 *
	 * @param examVo
	 * @return
	 */
	@Override
	@Transactional
	public ServerResponse addExam(ExamVo examVo) {
		//插入试卷
//		SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录用户
//		examVo.setCreateBy(principal.getId());
		int result = examMapper.addExam(examVo);
		if (result <= 0) {
			return ServerResponse.serverResponseByFail(Consts.ExamStatusEnum.ADD_EXAM_FAILED.getStatus(), Consts.ExamStatusEnum.ADD_EXAM_FAILED.getDesc());
		}
		Integer[] ids = Convert.toIntArray(examVo.getIds());
		;//获取所有试题id数组
		BigDecimal score = new BigDecimal(0);
		if (ids != null && ids.length > 0) {
			//设置exam和question的关联
			for (Integer id : ids) {
				ExamQuestion examQuestion = new ExamQuestion();
				examQuestion.setExamId(examVo.getExamId());
				examQuestion.setQuestionId(id);
				ServerResponse response = examQuestionClient.addExamQuestion(examQuestion);
				if (!response.isSucess()) {
					return response;
				}
				ServerResponse question = questionClient.selectOneById(id);
				if (!question.isSucess()) {
					return question;
				}
				Question data = (Question) question.getData();
				//设置试卷的总分
				score = BigDecimalUtil.add(score.doubleValue(), data.getScore().doubleValue());
			}
		}
		examVo.setScore(score);
		ServerResponse examByResponse = updateExamById(examVo);
		if (!examByResponse.isSucess()) {
			return examByResponse;
		}
		//获取参加考试的学生的信息
		Long[] studentIds = Convert.toLongArray(examVo.getStudentIds());
		if (studentIds != null) {
			for (Long studentId : studentIds) {
				ExamStudent examStudent = new ExamStudent();
				examStudent.setExamId(examVo.getExamId());
				examStudent.setStudentId(studentId);
				ServerResponse response = examStudentClient.insert(examStudent);
				if (!response.isSucess()) {
					return response;
				}
			}
		}
		//获取批卷人id
		Long[] reviewerIds = Convert.toLongArray(examVo.getReviewerIds());
		if (reviewerIds != null) {
			for (Long reviewerId : reviewerIds) {
				ExamTeacher examTeacher = new ExamTeacher();
				examTeacher.setExamId(examVo.getExamId());
				examTeacher.setTeacherId(reviewerId);
				ServerResponse response = examTeacherClient.insert(examTeacher);
				if (!response.isSucess()) {
					return response;
				}
			}
		}
		return ServerResponse.serverResponseBySucess(result);
	}

	/**
	 * 根据id更新试卷
	 */
	@Transactional
	private ServerResponse updateExamById(ExamVo examVo) {
		if (examVo.getExamId() != null) {
			int result = examMapper.updateExamById(examVo);
			if (result <= 0) {
				return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
			}
			return ServerResponse.serverResponseBySucess(result);
		} else {
			return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
		}
	}

	/**
	 * 根据id获取examBo
	 */
	@Override
	public ServerResponse getExamPoById(Integer examId) {
		if (examId != null) {
			ExamBo examBo = new ExamBo();
			Exam exam = examMapper.selectOneById(examId);
			if (exam == null) {
				return ServerResponse.serverResponseByFail(304, "没有找到任何examId为" + examId + "的试卷信息");
			}
			examBo.setExamId(exam.getExamId());
			examBo.setExamName(exam.getExamName());
			examBo.setExamStartDate(exam.getExamStartDate());
			examBo.setExamLastTime(exam.getExamLastTime());
			examBo.setCreateDate(TimeUtils.dateToStr(exam.getCreateDate()));
			examBo.setUpdateDate(TimeUtils.dateToStr(exam.getUpdateDate()));
			examBo.setCreateBy(exam.getCreateBy());
			examBo.setUpdateBy(exam.getUpdateBy());
			List<Long> teacherids = examTeacherClient.selectByExamId(exam.getExamId());
			if (teacherids.size() > 0) {
				List<User> teachers = iUserClient.findUserByIds(teacherids);
				//将查询到的教师数据转化为vo对象
				List<UserVO> userVos = new ArrayList<>();
				for (User teacher : teachers) {
					UserVO userVo = new UserVO();
					userVo.setId(teacher.getId());
					userVo.setSexName(teacher.getName());
					userVo.setName(teacher.getName());
					userVo.setRoleName(teacher.getRealName());
					userVos.add(userVo);
				}
				examBo.setReviewers(userVos);
			}

			examBo.setScore(exam.getScore());
			List<Integer> questionIds = examQuestionClient.findQuestionIdsByExamId(examId);
			if (questionIds.size() > 0) {
				//查询试题集合
				List<Question> questions = questionClient.findQuestionListByExamId(questionIds);
				examBo.setQuestions(questions);
			}
			List<Long> studentIds = examStudentClient.findStudentIdsByExamIds(exam.getExamId());
			if (studentIds.size() > 0) {
				//查询考生集合
				List<User> students = iUserClient.findUserByIds(studentIds);
				//将查询到的学生数据转化为vo对象
				List<UserVO> studentVos = new ArrayList<>();
				for (User student : students) {
					UserVO userVo = new UserVO();
					userVo.setId(student.getId());
					userVo.setSexName(student.getName());
					userVo.setName(student.getName());
					userVo.setRoleName(student.getRealName());
					studentVos.add(userVo);
				}
				examBo.setStudents(studentVos);
			}
			return ServerResponse.serverResponseBySucess(examBo);
		}

		return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
	}

	/**
	 * 删除试卷
	 *
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional
	public ServerResponse delectExam(String ids) {
		//执行增删操作，清空缓存
		GuavaCacheUtils.setKey("examCount", "null");
		Integer[] integers = Convert.toIntArray(ids);
		if (integers.length > 0) {
			for (Integer id : integers) {
				//删除与试题关联
				int row1 = examQuestionClient.deleteByExamId(id);
				//删除和学生的关联
				int row2 = examStudentClient.deleteByExamId(id);
				//删除和教师的关联
				int row3 = examTeacherClient.deleteByExamId(id);
			}
			int result = examMapper.delectExamByExamId(integers);
			if (result <= 0) {
				return ServerResponse.serverResponseByFail(Consts.StatusEnum.UPDATA_FAILED.getStatus(), Consts.StatusEnum.UPDATA_FAILED.getDesc());
			}
			return ServerResponse.serverResponseBySucess(result);//返回影响行数
		}
		return ServerResponse.serverResponseByFail(Consts.StatusEnum.PARAM_NOT_EMPTY.getStatus(), Consts.StatusEnum.PARAM_NOT_EMPTY.getDesc());
	}


	/**
	 * 修改试卷
	 *
	 * @param examVo
	 * @return
	 */
	@Override
	@Transactional
	public ServerResponse updateExam(ExamVo examVo) {
		//1.删除关联
		//删除关联，根据examid
		int row1 = examQuestionClient.deleteByExamId(examVo.getExamId());
		if (row1 <= 0) {
			System.out.println("row1" + row1);
		}
		//删除和学生的关联
		int row2 = examStudentClient.deleteByExamId(examVo.getExamId());
		if (row2 <= 0) {
			System.out.println("row2" + row2);
		}
		//删除和教师的关联
		int row3 = examTeacherClient.deleteByExamId(examVo.getExamId());
		if (row3 <= 0) {
			System.out.println("row3" + row3);
		}
		//2.重新设置关联
		BigDecimal score = new BigDecimal("0");
		Integer[] ids = Convert.toIntArray(examVo.getIds());//获取试题id数组
		if (ids != null) {
			for (Integer id : ids) {//设置试卷与试题关联
				ExamQuestion examQuestion = new ExamQuestion();
				examQuestion.setExamId(examVo.getExamId());
				examQuestion.setQuestionId(id);
				ServerResponse response = examQuestionClient.addExamQuestion(examQuestion);
				if (!response.isSucess()) {
					return response;
				}
				ServerResponse question = questionClient.selectOneById(id);
				if (!question.isSucess()) {
					return question;
				}
				Question data = (Question) question.getData();
				//设置试卷的总分
				score = BigDecimalUtil.add(score.doubleValue(), data.getScore().doubleValue());
			}
		}
		examVo.setScore(score);
		//重置和学生的关联
		if (examVo.getStudentIds() != null) {
			for (Long studentId : Convert.toLongArray(examVo.getStudentIds())) {
				ExamStudent examStudent = new ExamStudent();
				examStudent.setExamId(examVo.getExamId());
				examStudent.setStudentId(studentId);
				ServerResponse response = examStudentClient.insert(examStudent);
				if (!response.isSucess()) {
					return response;
				}
			}
		}
		//重置批卷人与试卷关联
		Long[] reviewerIds = Convert.toLongArray(examVo.getReviewerIds());
		if (reviewerIds != null) {
			for (Long reviewerId : reviewerIds) {
				ExamTeacher examTeacher = new ExamTeacher();
				examTeacher.setExamId(examVo.getExamId());
				examTeacher.setTeacherId(reviewerId);
				ServerResponse response = examTeacherClient.insert(examTeacher);
				if (!response.isSucess()) {
					return response;
				}
			}
		}
		//SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();//获取当前登录用户
		examVo.setUpdateBy(1);
		ServerResponse response = updateExamById(examVo);
		return response;
	}

	/**
	 * 考生获取考试列表
	 *
	 * @param exam
	 * @param id
	 * @return
	 */
	@Override
	public TableDataInfo findExamListForStu(Long id, Map<String, Object> exam, Query query) {
		TableDataInfo examListResponse = findExamList(exam,query);
		if (!examListResponse.isSucess()) {
			return examListResponse;
		}
		Long aLong = examListResponse.getCount();
		List<ExamReturnVo> examList = (List<ExamReturnVo>) examListResponse.getData();
		List<ExamReturnVo> returnExamVos = new ArrayList<>();
		Iterator<ExamReturnVo> iterator = examList.iterator();
		while (iterator.hasNext()) {
			ExamReturnVo examVo = iterator.next();
//            ExamVo examVo = PoToVoUtil.examPoToVo(temp);
			ExamStudent examStudent = examStudentClient.selectByExamIdAndStuId(examVo.getExamId(), id);
			//说明没有指定该学生可以考试
			if (examStudent == null) {
				//删除此条记录
				iterator.remove();
				aLong--;
				continue;
			} else if ("0".equals(examStudent.getStatus())) {
				//说明还没有做，可以做
				examVo.setAccessed(false);
			} else if ("1".equals(examStudent.getStatus())) {
				//说明已经做过了，只能显示不能做
				examVo.setAccessed(true);
			}
			examVo.setTotalScore(examStudent.getTotalScore());
			returnExamVos.add(examVo);
		}
		//构建返回数据的分页模型
//        PageInfo returnPageInfo = new PageInfo(returnExamVos);
		return TableDataInfo.ResponseBySucess("", aLong, returnExamVos);
	}


	public ServerResponse getExamForStudentByExamId(Integer examId, Long StudentId) {
		Exam exam = examMapper.selectOneById(examId);
		if (exam == null) {
			return ServerResponse.serverResponseByFail(404, "该考试不存在！");
		}
		//根据试卷id获取与试题关联信息
		List<ExamQuestion> examQuestions = examQuestionClient.selectExamQuestionListByExamId(examId);
		if (examQuestions.size() <= 0) {
			return ServerResponse.serverResponseByFail(404, "该考试无试题信息！");
		}
		//新建5中类型的List集合
		List<QuestionVo> radioQuestion = new ArrayList<>();
		List<QuestionVo> checkboxQuestion = new ArrayList<>();
		List<QuestionVo> blackQuestion = new ArrayList<>();
		List<QuestionVo> judgeQuestion = new ArrayList<>();
		List<QuestionVo> shortQuestion = new ArrayList<>();
		//创建返回模型
		StudentExamDetail studentExamDetail = new StudentExamDetail();
		//设置试卷信息
		studentExamDetail.setExamName(exam.getExamName());
		studentExamDetail.setLastTime(exam.getExamLastTime());
		studentExamDetail.setStartDate(TimeUtils.dateToStr(exam.getExamStartDate()));
		studentExamDetail.setExamId(examId);
		BigDecimal score = new BigDecimal("0");
		for (ExamQuestion examQuestion : examQuestions) {
			ServerResponse questionResponse = questionClient.selectOneById(examQuestion.getQuestionId());
			Question question = (Question) questionResponse.getData();
			QuestionVo questionVo = com.exam.question.utils.PoToVoUtil.questionPoToVO(question);
			//查询出已经保存的有的数据，方便页面回显
			ExamRecord examRecord = examRecordClient.selectRecordByExamIdAndQuestionIdAndStuId(examId, question.getId(), StudentId);
			String answer = "";
			if (examRecord == null) {
				questionVo.setFinalScore(new BigDecimal("0"));
			} else {
				questionVo.setFinalScore(examRecord.getFinalScore());
				answer = examRecord.getAnswer();
			}
			score = BigDecimalUtil.add(score.doubleValue(), question.getScore().doubleValue());

			setAnswer(answer, questionVo);//设置答案

			if ("1".equals(question.getType())) {
				//获取单选
				radioQuestion.add(questionVo);
			} else if ("2".equals(question.getType())) {
				//获取多选
				checkboxQuestion.add(questionVo);
			} else if ("3".equals(question.getType())) {
				//获取填空
				blackQuestion.add(questionVo);
			} else if ("4".equals(question.getType())) {
				//获取判断
				judgeQuestion.add(questionVo);
			} else if ("5".equals(question.getType())) {
				//获取简答
				shortQuestion.add(questionVo);
			}
		}
		studentExamDetail.setCheckboxQuestion(checkboxQuestion);
		studentExamDetail.setRadioQuestion(radioQuestion);
		studentExamDetail.setShortQuestion(shortQuestion);
		studentExamDetail.setJudgeQuestion(judgeQuestion);
		studentExamDetail.setBalckQuestion(blackQuestion);

		studentExamDetail.setScore(score);
		return ServerResponse.serverResponseBySucess(studentExamDetail);
	}

	/**
	 * 设置答案
	 *
	 * @param answer
	 * @param questionVo
	 */
	private void setAnswer(String answer, QuestionVo questionVo) {
		//单选 多选ABC
		switch (questionVo.getType()) {
			case "1":
			case "2":
				String[] split = answer.split(",");
				for (String s : split) {
					if ("A".equals(s)) {
						questionVo.setOptionACheckedStu("A");
					} else if ("B".equals(s)) {
						questionVo.setOptionBCheckedStu("B");
					} else if ("C".equals(s)) {
						questionVo.setOptionCCheckedStu("C");
					} else if ("D".equals(s)) {
						questionVo.setOptionDCheckedStu("D");
					}
				}
				break;
			//判断
			case "4":
				if ("1".equals(answer)) {
					questionVo.setJudgeAnswer1Stu("1");
				} else if ("0".equals(answer)) {
					questionVo.setJudgeAnswer0Stu("0");
				}
				break;
			case "3":
			case "5":
				questionVo.setTextAnswerStu(answer);
				break;
			default:
				break;
		}
	}


	/**
	 * 获取exam的详细信息，方便老师review
	 *
	 * @param examId
	 * @param studentId
	 * @return
	 */
	@Override
	public ServerResponse findExamDetailToReview(Integer examId, Long studentId) {
		//根据试卷id获取与试题关联信息
		List<ExamQuestion> examQuestions = examQuestionClient.selectExamQuestionListByExamId(examId);
		Exam exam = examMapper.selectOneById(examId);
		//新建5中类型的List集合
		List<QuestionVo> radioQuestion = new ArrayList<>();
		List<QuestionVo> checkboxQuestion = new ArrayList<>();
		List<QuestionVo> blackQuestion = new ArrayList<>();
		List<QuestionVo> judgeQuestion = new ArrayList<>();
		List<QuestionVo> shortQuestion = new ArrayList<>();
		//创建返回模型
		StudentExamDetail studentExamDetail = new StudentExamDetail();
		//设置试卷信息
		studentExamDetail.setExamName(exam.getExamName());
		studentExamDetail.setLastTime(exam.getExamLastTime());
		studentExamDetail.setStartDate(TimeUtils.dateToStr(exam.getExamStartDate()));
		studentExamDetail.setExamId(examId);

		//获取学生总成绩
		ExamStudent examStudent = examStudentClient.selectByExamIdAndStuId(examId, studentId);
		//设置学生总成绩
		studentExamDetail.setTotalScore(examStudent.getTotalScore());
		BigDecimal score = new BigDecimal("0");
		for (ExamQuestion examQuestion : examQuestions) {
			ServerResponse questionResponse = questionClient.selectOneById(examQuestion.getQuestionId());
			Question question = (Question) questionResponse.getData();
			QuestionVo questionVo = com.exam.question.utils.PoToVoUtil.questionPoToVO(question);
			//查询出已经保存的有的数据，方便页面回显
			ExamRecord examRecord = examRecordClient.selectRecordByExamIdAndQuestionIdAndStuId(examId, question.getId(), studentId);
			String answer = "";
			if (examRecord == null) {
				continue;
			} else {
				questionVo.setFinalScore(examRecord.getFinalScore());
				answer = examRecord.getAnswer();
			}
			score = BigDecimalUtil.add(score.doubleValue(), question.getScore().doubleValue());

			setAnswer(answer, questionVo);//设置答案

			if ("1".equals(question.getType())) {
				//获取单选
				radioQuestion.add(questionVo);
			} else if ("2".equals(question.getType())) {
				//获取多选
				checkboxQuestion.add(questionVo);
			} else if ("3".equals(question.getType())) {
				//获取填空
				blackQuestion.add(questionVo);
			} else if ("4".equals(question.getType())) {
				//获取判断
				judgeQuestion.add(questionVo);
			} else if ("5".equals(question.getType())) {
				//获取简答
				shortQuestion.add(questionVo);
			}
		}
		studentExamDetail.setCheckboxQuestion(checkboxQuestion);
		studentExamDetail.setRadioQuestion(radioQuestion);
		studentExamDetail.setShortQuestion(shortQuestion);
		studentExamDetail.setJudgeQuestion(judgeQuestion);
		studentExamDetail.setBalckQuestion(blackQuestion);
		studentExamDetail.setStudentId(studentId);
		studentExamDetail.setScore(score);
		return ServerResponse.serverResponseBySucess(studentExamDetail);
	}
}
