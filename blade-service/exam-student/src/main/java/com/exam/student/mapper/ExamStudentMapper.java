package com.exam.student.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.student.entity.ExamStudent;
import com.exam.student.vo.ScoreVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
@Mapper
public interface ExamStudentMapper extends BaseMapper<ExamStudent> {
    /**
     * 插入数据
     * */
    int insert(ExamStudent record);

    /**
     * 查询考生id集合
     * */
    List<Long> findStudentIdsByExamIds(Integer examId);

    /**
     * 通过试卷id删除数据
     * */
    int deleteByExamId(Integer examId);

    /**
     * 根据exam的id和stu的id获取单条记录
     */
    ExamStudent selectByExamIdAndStuId(@Param("exmaId")Integer examId, @Param("studentId")Long studentId);

    /**
     * 根据试卷的id和学生的id，更换它的状态
     * */
    int updateStatusByExamIdAndStuId(@Param("examId") Integer examId, @Param("stuId") Long stuId, @Param("status") String status);

    /**
     * 根据studentId和exam的id更新成绩
     * */
    int updateReadingAndTotalScoreByStuIdAndExamId(@Param("stuId") Long stuId, @Param("examId") Integer examId, @Param("score") BigDecimal score, @Param("reading") String reading);

    /**
     * 根据examId获取ExamStudent实体类的信息
     * */
    List<ExamStudent> selectByExamId(Integer examId);

    /**
     * 根据studentId和exam的id更新成绩和添加阅卷人id
     * */
    int updateReadingAndTotalScoreAndReviewerIdByStuIdAndExamId(@Param("stuId") Long stuId, @Param("examId") Integer examId, @Param("score") BigDecimal score, @Param("reading") String reading, @Param("reviewerId") Long reviewerId);

    /**
     * 查询指定试卷的成绩的集合
     */
    List<ScoreVo> selectScoreList(@Param("scoreVo") ScoreVo scoreVo, @Param("examId") Integer examId);

    /**
     * 查询指定试卷的成绩的集合的条数
     * @param examId
     */
    Long getCountAllByexamId(Integer examId);
}
