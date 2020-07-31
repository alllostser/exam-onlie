package com.exam.score.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.student.vo.ScoreVo;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;

public interface ScoreService extends IService<ScoreVo> {
	/**
	 * 成绩详情
	 * */
	TableDataInfo findScoreList(ScoreVo scoreVo,Integer examId,Query query);
}
