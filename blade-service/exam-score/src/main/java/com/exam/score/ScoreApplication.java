package com.exam.score;

import org.springblade.common.constant.CommonConstant;
import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients({CommonConstant.BASE_PACKAGES, AppConstant.BASE_PACKAGES})
public class ScoreApplication {
	public static void main(String[] args) {
		BladeApplication.run("exam-score",ScoreApplication.class,args);
	}
}
