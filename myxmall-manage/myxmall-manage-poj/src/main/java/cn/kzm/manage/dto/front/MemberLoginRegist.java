package cn.kzm.manage.dto.front;




public class MemberLoginRegist {

    private String userName; //用户名

    private String userPwd; //密码

    private String userPwd2;


    private String captchaVerification; //校验码

    public String getUserName() {
        return userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getCaptchaVerification() {
        return captchaVerification;
    }

    public void setCaptchaVerification(String captchaVerification) {
        this.captchaVerification = captchaVerification;
    }

    public String getUserPwd2() {
        return userPwd2;
    }

    public void setUserPwd2(String userPwd2) {
        this.userPwd2 = userPwd2;
    }
}
