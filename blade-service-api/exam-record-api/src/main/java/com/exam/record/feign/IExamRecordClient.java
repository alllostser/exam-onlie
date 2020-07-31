package com.exam.record.feign;


import com.exam.record.entity.ExamRecord;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.response.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = CommonConstant.APPLICATION_RECORD_NAME,
	fallback = IExamRecordFallback.class)
public interface IExamRecordClient {
	String API_PREFIX = "/record";

    /**
     * 根据examId,questionId,StudentId获取考试记录
     * */
	@GetMapping(API_PREFIX + "/record-info-by-examId-questionId-studentId")
	ExamRecord selectRecordByExamIdAndQuestionIdAndStuId(@RequestParam Integer examId, @RequestParam Integer questionId, @RequestParam Long studentId);

    /**
     * 考试记录,考生每次答题后保存答题记录
     */
	@GetMapping(API_PREFIX + "/save")
    ServerResponse insertOrUpdateRecord(ExamRecord record);

    /**
     * 更新其最终成绩
     * */
	@GetMapping(API_PREFIX + "/final-score")
    int updateRecordFinalScore(ExamRecord examRecord);

    /**
     *教师更新最终成绩
     * */
	@GetMapping(API_PREFIX + "/teacher-final-score")
    ServerResponse teacherReviewRecord(ExamRecord examRecord);

    /**
     * 设置完成试卷的review
     * */
	@GetMapping(API_PREFIX + "/review")
    ServerResponse finishReview(@RequestParam("examId") Integer examId,@RequestParam("stuId") Long stuId);
}
