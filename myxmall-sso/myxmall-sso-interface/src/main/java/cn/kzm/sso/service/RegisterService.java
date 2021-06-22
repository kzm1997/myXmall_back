package cn.kzm.sso.service;

public interface RegisterService {


    /**
     * 校验唯一
     * @param param
     * @param type
     * @return
     */
    boolean checkData(String param,int type);

    /**
     * 注册
     * @param userName
     * @param password
     * @return
     */
    int register(String userName,String password);
}
