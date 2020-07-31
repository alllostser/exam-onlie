package com.exam.question.utils;


import com.exam.question.entity.Question;
import com.exam.question.vo.QuestionVo;
import org.springblade.common.tool.TimeUtils;

public class PoToVoUtil {


    public static QuestionVo questionPoToVO(Question question){
        QuestionVo questionVo = new QuestionVo();
        questionVo.setAnalyse(question.getAnalyse());
        questionVo.setAnswer(question.getAnswer());
        questionVo.setCreateBy(question.getCreateBy());
        questionVo.setCreateDate(TimeUtils.dateToStr(question.getCreateDate(),"yyyy-MM-dd"));
        questionVo.setScore(question.getScore());
        questionVo.setId(question.getId());
        questionVo.setOptionA(question.getOptionA());
        questionVo.setOptionB(question.getOptionB());
        questionVo.setOptionC(question.getOptionC());
        questionVo.setOptionD(question.getOptionD());
        questionVo.setTitle(question.getTitle());
        questionVo.setType(question.getType());
        questionVo.setUpdateBy(question.getUpdateBy());
        questionVo.setUpdateDate(question.getUpdateDate()!=null?TimeUtils.dateToStr(question.getUpdateDate(),"yyyy-MM-dd"):null);
        return questionVo;
    }

}
