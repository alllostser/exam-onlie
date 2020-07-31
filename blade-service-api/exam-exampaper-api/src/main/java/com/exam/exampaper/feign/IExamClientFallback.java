package com.exam.exampaper.feign;

import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class IExamClientFallback implements IExamClient{
	@Override
	public TableDataInfo findExamListForStu(@RequestParam("l") Long l, @RequestParam String examJson, Query query) {
		return TableDataInfo.ResponseByFail(500,"服务器异常(┬＿┬)！！！");
	}

	@Override
	public ServerResponse getExamForStudentByExamId(@RequestParam Integer examId, @RequestParam Long StudentId) {
		return ServerResponse.serverResponseByFail(500,"服务器异常(┬＿┬)！！！");
	}

	@Override
	public TableDataInfo findExamListToReview(@RequestParam("teacherId") Long teacherId, @RequestParam("examJson") String examJson, Query query) {
		return TableDataInfo.ResponseByFail(500,"服务器异常(┬＿┬)！！！");
	}

	@Override
	public ServerResponse findExamDetailToReview(@RequestParam("examId") Integer examId,@RequestParam("studentId") Long studentId) {
		return ServerResponse.serverResponseByFail(500,"服务器异常(┬＿┬)！！！");
	}
}
