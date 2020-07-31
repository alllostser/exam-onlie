package com.exam.teacher.controller;


import com.alibaba.fastjson.JSONObject;
import com.exam.exampaper.feign.IExamClient;
import com.exam.record.entity.ExamRecord;
import com.exam.record.feign.IExamRecordClient;
import com.exam.student.entity.ExamStudent;
import com.exam.student.feign.IExamStudentClient;
import lombok.AllArgsConstructor;
import org.springblade.common.response.ServerResponse;
import org.springblade.common.response.TableDataInfo;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.annotation.PreAuth;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.system.user.entity.User;
import org.springblade.system.user.feign.IUserClient;
import org.springblade.system.user.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TeacherReviewController
 * @Description //教师批卷控制层
 * @Author GuXinYu
 * @Date 2020/5/22 0:42
 * @Version 1.0
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/exam/teacher")
public class TeacherReviewController {
    //注入对象
	IExamClient examClient;
    IExamRecordClient examRecordClient;
    IExamStudentClient examStudentClient;
	IUserClient userClient;


    /**
     * 阅卷列表
     * @param exam
     * @return
     */
    @GetMapping("/list")
	@PreAuth("hasRole('teacher')")
    public TableDataInfo list(@RequestParam Map<String,Object> exam, Query query)
    {
		Long userId = SecureUtil.getUserId();
		String examJson = JSONObject.toJSONString(exam);
		TableDataInfo response = examClient.findExamListToReview(userId,examJson,query);
            return response;
    }

    /**
     * 批卷,获取exam的详细信息，方便老师review
     * @param examId
     * @param studentId
     * @return
     */
    @GetMapping("/reviewView")
	@PreAuth("hasRole('teacher')")
    public ServerResponse reviewStuExam(Integer examId, Long studentId) {
            ServerResponse response = examClient.findExamDetailToReview(examId, studentId);
            return response;
    }

    /**
     * 异步提交并更新单个试题的得分
     * @param examRecord
     * @return
     */
    @PostMapping("/reviewExam")
	@PreAuth("hasRole('teacher')")
    public ServerResponse reviewExam(ExamRecord examRecord) {
            ServerResponse response = examRecordClient.teacherReviewRecord(examRecord);
            return response;
    }

    /**
     * 完成阅卷，提交阅卷的结果，返回index界面，并更新exam_student中，total_Score的值
     * @param examId
     * @param stuId
     * @return
     */
    @PostMapping("/finishReview")
	@PreAuth("hasRole('teacher')")
    public ServerResponse finishReview(Integer examId, Long stuId) {
            //SysUser loginUesr = (SysUser) SecurityUtils.getSubject().getPrincipal();
            ServerResponse response = examRecordClient.finishReview(examId, stuId);
            return response;
    }

    /**
     * 展示试卷的详情，阅卷完成详情
     * @param examId
     * @param stuId
     * @return
     */
    @GetMapping("/detail")
	@PreAuth("hasRole('teacher')")
    public ServerResponse examDetail( Integer examId,  Long stuId) {
            Map<String,Object> map = new HashMap<>();
             ServerResponse examDetailToReview = examClient.findExamDetailToReview(examId, stuId);
            if (!examDetailToReview.isSucess()){
                return examDetailToReview;
            }
            //StudentExamDetail studentExamDetail = (StudentExamDetail) examDetailToReview.getData();
            map.put("exam",examDetailToReview.getData());
            ServerResponse userById = userClient.findUserById(stuId);
            if (!userById.isSucess()){
                return userById;
            }
            User student = (User) userById.getData();
            UserVO studentVO = new UserVO();
            studentVO.setName(student.getName());
            studentVO.setId(student.getId());
            studentVO.setRealName(student.getRealName());
            studentVO.setEmail(student.getEmail());
            studentVO.setPhone(student.getPhone());
            map.put("student", studentVO);
            ExamStudent examStudent = examStudentClient.selectByExamIdAndStuId(examId, stuId);
            map.put("examStudent",examStudent);
            return ServerResponse.serverResponseBySucess(map);
    }


}
