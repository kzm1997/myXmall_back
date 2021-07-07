package com.example.myxmallssoservice.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSON;
import cn.kzm.common.myxmallConst.MyXmallConst;
import cn.kzm.manage.dto.front.MemberInfo;
import cn.kzm.manage.pojo.TbMember;
import cn.kzm.sso.service.LoginService;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myxmallssoservice.Mapper.TbMemberMapper;
import com.example.myxmallssoservice.utils.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbMemberMapper tbMemberMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public MemberInfo login(String username, String passowrd) {

        //检查用户名
        TbMember tbMember = tbMemberMapper.selectOne(new QueryWrapper<TbMember>()
                .lambda().eq(TbMember::getUsername, username));
        MemberInfo memberInfo = new MemberInfo();
        if (tbMember == null) {

            memberInfo.setState(0);
            memberInfo.setMessage("用户名不存在");
            return memberInfo;
        }

        if (!DigestUtils.md5DigestAsHex(passowrd.getBytes()).equals(tbMember.getPassword())) {
            memberInfo.setState(0);
            memberInfo.setMessage("密码错误");
            return memberInfo;
        }
        String token = UUID.randomUUID().toString();
        BeanUtil.copyProperties(tbMember, memberInfo);
        memberInfo.setState(1);
        memberInfo.setToken(token);
        redisUtils.set(MyXmallConst.SESSION + token, memberInfo, MyXmallConst.SESSION_TIME);
        return memberInfo;
    }

    @Override
    public MemberInfo checkLogin(String token) {
        Object o = redisUtils.get(MyXmallConst.SESSION + token);
        MemberInfo memberInfo=new MemberInfo();
        if (o==null){
            memberInfo.setState(0);
            memberInfo.setMessage("用户登录已过期");
            return memberInfo;
        }
        MemberInfo info = BeanUtil.mapToBean((Map<?, ?>) o, MemberInfo.class, null);
        redisUtils.expire(MyXmallConst.SESSION+token,MyXmallConst.SESSION_TIME);
        return info;
    }
}
