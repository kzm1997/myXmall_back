package cn.kzm.sso.service;

import cn.kzm.manage.dto.front.MemberInfo;

public interface LoginService {

   MemberInfo login(String username,String passowrd);

    MemberInfo checkLogin(String token);
}
