package com.exam.score.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.student.vo.ScoreVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamScoreMapper extends BaseMapper<ScoreVo> {
	List<ScoreVo> selectScoreList(Page page,@Param("examId")Integer examId ,@Param("scoreVo") ScoreVo scoreVo );
}
