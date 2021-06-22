package cn.kzm.controller;

import cn.kzm.common.dto.Result;
import cn.kzm.manage.dto.front.MemberInfo;
import cn.kzm.manage.dto.front.MemberLoginRegist;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Usercontroller {

    @Autowired



    @ApiModelProperty("注册接口")
    @RequestMapping("/member/register")
    public Result<MemberInfo> registerMember(@RequestBody MemberLoginRegist memberLoginRegist){


    }
}
