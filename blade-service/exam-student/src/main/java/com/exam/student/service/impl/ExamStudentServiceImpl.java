package com.exam.student.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.exam.exampaper.feign.IExamClient;
import com.exam.examquestion.entity.ExamQuestion;
import com.exam.examquestion.feign.IExamQuestionClient;
import com.exam.question.entity.Question;
import com.exam.question.feign.IQuestionClient;
import com.exam.record.entity.ExamRecord;
import com.exam.record.feign.IExamRecordClient;
import com.exam.student.entity.ExamStudent;
import com.exam.student.mapper.ExamStudentMapper;
import com.exam.student.service.ExamStudentService;
import com.exam.student.vo.ScoreReturnVo;
import com.exam.student.vo.ScoreVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.Consts;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.common.tool.BigDecimalUtil;
import org.springblade.common.tool.GuavaCacheUtils;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.system.user.feign.IUserClient;
import org.springblade.system.user.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 试卷考生关联表service层
 * */
@Service
@AllArgsConstructor
@Slf4j
public class ExamStudentServiceImpl implements ExamStudentService {
    private ExamStudentMapper examStudentMapper;
    private IExamQuestionClient examQuestionClient;
    private IQuestionClient questionClient;
    private IExamRecordClient examRecordClient;
	private IExamClient examClient;

    /**
     * 查询考生id集合
     * */
    @Override
    public List<Long> findStudentIdsByExamIds(Integer examId) {
        List<Long> studentIds = examStudentMapper.findStudentIdsByExamIds(examId);
        return studentIds;
    }

    /**
     * 通过试卷id删除数据
     * */
    @Override
	@Transactional
    public int deleteByExamId(Integer examId) {
        int result = examStudentMapper.deleteByExamId(examId);
        return result;
    }


    @Override
    public ServerResponse finishExam(Integer examId, Long userId) {
        //阅卷客观题
        ReadingExamObjective(examId, userId);
        int result = examStudentMapper.updateStatusByExamIdAndStuId(examId, userId, Consts.ExamStatusEnum.TAKE_THE_EXAN_YES);
        if (result <=0 ) {
            return ServerResponse.serverResponseByFail(500, "数据异常，请联系管理员");
        }
        return ServerResponse.serverResponseBySucess(result);
    }
    /**
     * 批阅所有的客观题
     *
     * @param examId
     * @param stuId
     */
    private void ReadingExamObjective(Integer examId, Long stuId) {
        //查询出所有的question id
        List<ExamQuestion> examQuestions = examQuestionClient.selectExamQuestionListByExamId(examId);
        //获取所有的题的数量
        int count = examQuestions.size();
        BigDecimal totalScore =new BigDecimal("0") ;//试卷考试总分
        int index = 0;//试题数量
        for (ExamQuestion examQuestion : examQuestions) {
            //获取对应的question的信息
            ServerResponse questionResponse = questionClient.selectOneById(examQuestion.getQuestionId());
            if (!questionResponse.isSucess()){
                continue;
            }
            Question question = (Question) questionResponse.getData();
            ExamRecord examRecord = examRecordClient.selectRecordByExamIdAndQuestionIdAndStuId(examId, question.getId(), stuId);
            if (StringUtil.isEmpty(examRecord)){
            	examRecord = new ExamRecord();
            	examRecord.setStuId(stuId);
            	examRecord.setExamId(examId);
            	examRecord.setQuestionId(question.getId());
            	examRecord.setFinalScore(new BigDecimal("0"));
				ServerResponse serverResponse = examRecordClient.insertOrUpdateRecord(examRecord);
				if (!serverResponse.isSucess()){
					log.info("***********插入失败**********");
					continue;
				}
			}
            switch (question.getType()) {
                //单选和多选,判断
                case "1":
                case "2":
                case "4":
//                    if (examRecord.getAnswer()!=null)
                    if (question.getAnswer().equals(examRecord.getAnswer())) {
                        examRecord.setFinalScore(question.getScore());
                        totalScore = BigDecimalUtil.add(totalScore.doubleValue(),question.getScore().doubleValue()) ;
                    } else {
                        examRecord.setFinalScore(new BigDecimal("0"));
                    }
                    index++;
                    break;
            }
            int result = examRecordClient.updateRecordFinalScore(examRecord);
        }
        //如果这两者相等，说明只有客观题，不需要老师来review
        if (index == count) {
            //更新试卷的状态为已阅
            examStudentMapper.updateReadingAndTotalScoreByStuIdAndExamId(stuId, examId, totalScore, Consts.ExamStatusEnum.CHECK_THE_TEST_PAPER_YES);
        }
    }


	@Override
	public TableDataInfo findExamListForStu(Long l, Map<String, Object> exam, Query query) {
		String examJson = JSONObject.toJSONString(exam);
		TableDataInfo examListForStu = examClient.findExamListForStu(l,examJson, query);
		return examListForStu;
	}
}
