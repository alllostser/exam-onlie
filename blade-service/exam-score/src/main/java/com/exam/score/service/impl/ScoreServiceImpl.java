package com.exam.score.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.score.mapper.ExamScoreMapper;
import com.exam.score.service.ScoreService;
import com.exam.student.vo.ScoreReturnVo;
import com.exam.student.vo.ScoreVo;
import lombok.AllArgsConstructor;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;
import org.springblade.system.user.entity.User;
import org.springblade.system.user.feign.IUserClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class ScoreServiceImpl extends ServiceImpl<ExamScoreMapper,ScoreVo> implements ScoreService  {
	private IUserClient userClient;

	/**
	 * 成绩详情
	 * */
	@Override
	public TableDataInfo findScoreList(ScoreVo scoreVo, Integer examId,Query query) {
		Page<ScoreVo> page = new Page<>(query.getCurrent(),query.getSize());
		List<ScoreVo> scoreVos = this.baseMapper.selectScoreList(page, examId,scoreVo);
		if (scoreVos.isEmpty()){
			return TableDataInfo.ResponseByFail(-1,"查询无果");
		}
		page.setRecords(scoreVos);
		List<ScoreReturnVo> scoreReturnVos = new ArrayList<>();
		for (ScoreVo score : scoreVos) {
			ScoreReturnVo scoreReturnVo = new ScoreReturnVo();
			scoreReturnVo.setExamId(score.getExamId());
			scoreReturnVo.setExamName(score.getExamName());
			scoreReturnVo.setReading(score.getReading());
			scoreReturnVo.setStatus(score.getStatus());
			scoreReturnVo.setStuId(score.getStuId());
			scoreReturnVo.setStuName(score.getStuName());
			scoreReturnVo.setTotalScore(score.getTotalScore());
			if (score.getReviewerId() != null) {
				ServerResponse userByIdResponse = userClient.findUserById(score.getReviewerId());
				if (userByIdResponse.isSucess()) {
					User user = (User) userByIdResponse.getData();
					scoreReturnVo.setReviewerName(user.getName());
				}
			}
			scoreReturnVos.add(scoreReturnVo);
		}
		return TableDataInfo.ResponseBySucess("sucess",page.getTotal(),scoreReturnVos);
	}
}
