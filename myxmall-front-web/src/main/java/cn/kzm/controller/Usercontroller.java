package cn.kzm.controller;

import cn.kzm.common.dto.Result;
import cn.kzm.common.utils.ResultUtil;
import cn.kzm.manage.dto.front.MemberInfo;
import cn.kzm.manage.dto.front.MemberLoginRegist;
import cn.kzm.sso.service.LoginService;
import cn.kzm.sso.service.RegisterService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Usercontroller {


    @Autowired
    private CaptchaService captchaService;

    @Reference
    RegisterService registerService;

    @Reference
    LoginService loginService;


    @ApiModelProperty("注册接口")
    @RequestMapping("/member/register")
    public Result<MemberInfo> registerMember(@RequestBody MemberLoginRegist memberLoginRegist) {


        //校验码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(memberLoginRegist.getCaptchaVerification());
        ResponseModel verification = captchaService.verification(captchaVO);

        if (verification.isSuccess() == false) {
            return new ResultUtil().setErrorMsg(verification.getRepMsg());
        }
        int result = registerService.register(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());

        if (result == 0) {
            return new ResultUtil<MemberInfo>().setErrorMsg("用户名已经注册");
        } else if (result == -1) {
            return new ResultUtil<MemberInfo>().setErrorMsg("用户名密码不能为空");
        }
        return new ResultUtil<MemberInfo>().BuildSuccess();
    }

    @ApiModelProperty("/登录接口")
    @RequestMapping("/member/login")
    public Result<MemberInfo> login(@RequestBody MemberLoginRegist memberLoginRegist) {


        //校验码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(memberLoginRegist.getCaptchaVerification());
        ResponseModel verification = captchaService.verification(captchaVO);

        if (verification.isSuccess() == false) {
            return new ResultUtil().setErrorMsg(verification.getRepMsg());
        }

        MemberInfo memberInfo = new MemberInfo();

        memberInfo = loginService.login(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
        return new ResultUtil<MemberInfo>().setData(memberInfo);
    }

    @ApiOperation("/校验用户是否登录")
    @RequestMapping("/user/checkLogin")
    public Result<MemberInfo> checkLogin(@RequestParam("token") String token) {
        MemberInfo memberInfo =loginService.checkLogin(token);
        return new  ResultUtil<MemberInfo>().setData(memberInfo);
    }


}
