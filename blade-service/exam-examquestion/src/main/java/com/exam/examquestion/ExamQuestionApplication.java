package com.exam.examquestion;

import org.springblade.common.constant.CommonConstant;
import org.springblade.core.launch.BladeApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients(CommonConstant.BASE_PACKAGES)
public class ExamQuestionApplication {
	public static void main(String[] args) {
		BladeApplication.run("exam-examquestion",ExamQuestionApplication.class,args);
	}
}
