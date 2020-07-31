package com.exam.score.controller;


import com.exam.score.service.ScoreService;
import com.exam.student.feign.IExamStudentClient;
import com.exam.student.vo.ScoreVo;
import lombok.AllArgsConstructor;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ScoreController
 * @Description //成绩管理Controller
 * @Author GuXinYu
 * @Date 2020/5/22 17:49
 * @Version 1.0
 **/
@RestController
@RequestMapping("/exam/score")
@AllArgsConstructor
public class ScoreController {
	private IExamStudentClient examStudentClient;
	private ScoreService scoreService;

    @GetMapping("/list.do")
    public TableDataInfo list(
		ScoreVo scoreVo,Integer examId,Query query)
    {
        TableDataInfo scoreResponse  = scoreService.findScoreList(scoreVo, examId, query);
        return scoreResponse;
    }
}
