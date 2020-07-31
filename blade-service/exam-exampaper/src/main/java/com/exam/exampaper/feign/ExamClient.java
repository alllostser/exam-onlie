package com.exam.exampaper.feign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exam.exampaper.entity.Exam;
import com.exam.exampaper.mapper.ExamMapper;
import com.exam.exampaper.vo.ExamPaperVo;
import com.exam.exampaper.vo.ExamReturnVo;
import com.exam.examquestion.entity.ExamQuestion;
import com.exam.examquestion.feign.IExamQuestionClient;
import com.exam.question.entity.Question;
import com.exam.question.feign.IQuestionClient;
import com.exam.question.utils.PoToVoUtil;
import com.exam.question.vo.QuestionVo;
import com.exam.question.vo.StudentExamDetail;
import com.exam.record.entity.ExamRecord;
import com.exam.record.feign.IExamRecordClient;
import com.exam.student.entity.ExamStudent;
import com.exam.student.feign.IExamStudentClient;
import com.exam.teacher.feign.IExamTeacherClient;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.Consts;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.common.tool.BigDecimalUtil;
import org.springblade.common.tool.TimeUtils;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.system.user.entity.User;
import org.springblade.system.user.feign.IUserClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

@RestController
@AllArgsConstructor
public class ExamClient implements IExamClient {
	private ExamMapper examMapper;
	private IExamStudentClient examStudentClient;
	private IExamRecordClient examRecordClient;
	private IExamQuestionClient examQuestionClient;
	private IQuestionClient questionClient;
	private IUserClient userClient;
	private IExamTeacherClient examTeacherClient;

	/**
	 * 考生获取考试列表
	 *
	 * @param examJson
	 * @return
	 */
	@Override
	@RequestMapping(value = API_PREFIX + "/getlist",method = RequestMethod.GET)
	public TableDataInfo findExamListForStu(Long l, String examJson, Query query) {
		Map exam = (Map) JSON.parse(examJson);
		exam.remove("l");
		TableDataInfo examListResponse = findExamList(exam, query);
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
			ExamStudent examStudent = examStudentClient.selectByExamIdAndStuId(examVo.getExamId(), l);
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
		return TableDataInfo.ResponseBySucess("", aLong, returnExamVos);
	}

	/**
	 * 根据examId获取学生的考试的试卷信息
	 *
	 * @param examId
	 * @return
	 */
	@Override
	@GetMapping(API_PREFIX + "/getdetail")
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
			QuestionVo questionVo = PoToVoUtil.questionPoToVO(question);
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

	@Override
	@GetMapping(API_PREFIX+"/getreview")
	public TableDataInfo findExamListToReview(Long teacherId, String examJson, Query query) {
		Map exam = (Map) JSONObject.parse(examJson);
		exam.remove("teacherId");
		TableDataInfo examListResponse = findExamList(exam,query);
		if (!examListResponse.isSucess()) {
			return examListResponse;
		}
		List<ExamReturnVo> examList = (List<ExamReturnVo>) examListResponse.getData();
		int count = examList.size();
		List<ExamPaperVo> examsReturn = new ArrayList<ExamPaperVo>();
		for (ExamReturnVo temp : examList) {
			Integer examId = temp.getExamId();
			List<Long> teacherIds = examTeacherClient.selectByExamId(examId);
			if (!teacherIds.contains(teacherId)) {//过滤掉不是当前登录教师试卷的记录
				count--;
				continue;
			}
			List<ExamStudent> examStudents = examStudentClient.selectByExamId(examId);
			for (ExamStudent examStudent : examStudents) {
				//过滤掉还未参加考试的学生
				if (Consts.ExamStatusEnum.TAKE_THE_EXAN_NO.equals(examStudent.getStatus())) {
					continue;
				}
				ExamPaperVo returnExam = new ExamPaperVo();
				returnExam.setExamId(examId);
				returnExam.setReviewerId(examStudent.getReviewerId());
				returnExam.setStudentId(examStudent.getStudentId());
				returnExam.setExamName(temp.getExamName());
				returnExam.setStudentName(( userClient.findUserById(examStudent.getStudentId()).getData()).getName());
				returnExam.setReading(examStudent.getReading());
				returnExam.setExamStartDate(TimeUtils.strToDate(temp.getExamStartDate()));
				examsReturn.add(returnExam);
			}
		}

		//构建分页模型
//        PageInfo pageInfo1 = new PageInfo(examsReturn);
		return TableDataInfo.ResponseBySucess("", (long) count, examsReturn);
	}

	@Override
	@GetMapping(API_PREFIX+"/getexamtoreview")
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

	public TableDataInfo findExamList(Map<String, Object> exam, Query query) {

		IPage<Exam> examIPage = examMapper.selectPage(Condition.getPage(query), Condition.getQueryWrapper(exam, Exam.class));
		List<Exam> exams = examIPage.getRecords();
		if (exams == null || exams.size() <= 0) {
			return TableDataInfo.ResponseByFail(404, "没有找到任何试卷信息");
		}
		//将返回exams转为examReturnVo返回
		List<ExamReturnVo> examReturnVoList = new ArrayList<>();
		for (Exam temp : exams) {
			ExamReturnVo examReturnVo = new ExamReturnVo();
			if (temp.getCreateBy()!=null){
				ServerResponse createBy = userClient.findUserById(temp.getCreateBy().longValue());
				if (createBy.isSucess()){
					User user = (User) createBy.getData();
					examReturnVo.setCreateBy(user.getName());
				}
			}
			if (temp.getUpdateBy()!=null){
				ServerResponse updateBy = userClient.findUserById(temp.getUpdateBy().longValue());
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

}
