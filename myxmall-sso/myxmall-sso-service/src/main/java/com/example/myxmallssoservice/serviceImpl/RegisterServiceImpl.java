package com.example.myxmallssoservice.serviceImpl;

import cn.kzm.common.exception.XmallException;
import cn.kzm.manage.pojo.TbMember;
import cn.kzm.sso.service.RegisterService;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.example.myxmallssoservice.Mapper.TbMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
public class RegisterServiceImpl implements RegisterService {


    @Autowired
    TbMemberMapper tbMemberMapper;

    @Override
    public int register(String userName, String password) {

        TbMember tbMember = new TbMember();
        tbMember.setUsername(userName);

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return -1; //用户名密码不能为空
        }
        boolean check = checkData(userName, 1);
        if (!check) {
            return 0; //用户名已经被注册
        }

        //md5
        String passowrdMd5 = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        tbMember.setCreated(new Date());
        tbMember.setUpdated(new Date());
        tbMember.setPassword(passowrdMd5);
        if (tbMemberMapper.insert(tbMember) == 0) {
            throw new XmallException("用户注册失败");
        }
        return 1;
    }

    @Override
    public boolean checkData(String param, int type) {

        TbMember tbMember = new TbMember();
        Integer result = 0;
        if (type == 1) {
            //用户名
            result = tbMemberMapper.selectCount(new QueryWrapper<TbMember>().lambda().eq(TbMember::getUsername, param));
        } else if (type == 2) {
            //手机
            result = tbMemberMapper.selectCount(new QueryWrapper<TbMember>().lambda().eq(TbMember::getPhone, param));
        } else if (type == 3) {
            //邮箱
            result = tbMemberMapper.selectCount(new QueryWrapper<TbMember>().lambda().eq(TbMember::getEmail, param));
        }
        return result == 0;
    }
}
