package org.springblade.common.constant;

public class Consts {
    public enum StatusEnum {
        //参数非空判断
        PARAM_NOT_EMPTY(101,"参数不能为空"),
        USERNAME_NOT_EMPTY(101,"用户名不能为空"),
        PASSWORD_NOT_EMPTY(101,"密码不能为空"),
        CODE_IS_EMPTY(101,"验证码为空"),
        EMAIL_NOT_EMPTY(101,"邮箱不能为空"),
        PHONE_NOT_EMPTY(101,"联系方式不能为空"),
        QUESTION_NOT_EMPTY(101,"密保问题不能为空"),
        ANSER_NOT_EMPTY(101,"密保答案不能为空"),
        TOKEN_NOT_EMPTY(101,"非法的令牌参数或令牌已失效"),

        //判断参数是否已存在
        USERNAME_EXISTS(102,"用户名已存在"),
        EMAIL_EXISTS(102,"邮箱已存在"),
        PHONE_EXISTS(102,"手机号已被使用"),

        //参数不存在
        LOGIN_PARAM_ERROR(103,"用户名或密码错误"),
        CODE_IS_ERROR(103,"验证码错误"),
        QUESTION_ANSWER_MISMATCHING(103,"问题答案不匹配"),
        TOKEN_MISMATCHING(103,"令牌不匹配"),
        USERNAME_NOT_EXISTS(103,"用户名不存在"),
        USERID_NOT_EXISTS(103,"用户不存在"),
        //用户未登录
        USER_NOT_LOGIN(104,"用户未登录"),

        //账号状态信息
        USER_DISABLED(105,"用户被禁用"),
        USER_LIMITED_AUTHORITY(105,"用户无权限"),
        //更新信息
        UPDATE_SUCCESS(0,"更新成功"),
        UPDATA_FAILED(-1,"更新失败"),
        //未知错误失败
        ERROR(100,"操作失败或未知错误");
        ;
        public static final String LOGIN_USER = "session_user";
        private int status; //状态码值
        private String desc;//对状态码描述

        StatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    public enum QuestionStatusEnum {
        TITLE_CANNOT_BE_NULL(201,"问题的题干不能为空"),
        SCORE_NOT_TRUE(201,"请填写正确的分值"),
        TRUE_ANSWER_CANNOT_BE_NULL(101,"正确答案不能为空"),
        OPTION_COUNT_NOT_TRUE(202,"选项数量不正确"),
        ;
//        问题类型：1表示单选，2表示多选，3表示填空，4表示判断，5表示问答
        public static final String SINGLE_CHOICE="1";
        public static final String MULTIPLE_CHOICE="2";
        public static final String COMPLETION="3";
        public static final String TRUE_OR_FALSE_QUESTION="4";
        public static final String ESSAY_QUESTION="5";
        private int status; //状态码值
        private String desc;//对状态码描述

        QuestionStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    public enum ExamStatusEnum {
        ADD_EXAM_FAILED(300,"添加试卷失败"),
        ;
        public static final String  TAKE_THE_EXAN_YES="1";//1表示已经参加考试
        public static final String TAKE_THE_EXAN_NO="0";//0表示未参加考试
        public static final String CHECK_THE_TEST_PAPER_YES="0"; //0表示阅卷完成
        public static final String CHECK_THE_TEST_PAPER_NO="1"; //1表示正在阅卷中
        private int status; //状态码值
        private String desc;//对状态码描述

        ExamStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
