package cn.kzm.common.utils;


import cn.kzm.common.dto.Result;

public class ResultUtil<T> {

    private Result<T> result=new Result<>();

    public  Result<T> BuildSuccess(){

        result.setSuccess(true);
        result.setMessage("success");
        result.setCode(200);
        return result;
    }

    public Result<T>  BuidFailure(){
        result.setSuccess(false);
        result.setMessage("failure");
        result.setCode(500);
        return result;
    }

    public Result<T> setData(T t){
        this.result.setResult(t);
        this.result.setSuccess(true);
        this.result.setCode(200);
        return this.result;
    }

    public Result<T> setData(T t, String msg){
        this.result.setResult(t);
        this.result.setCode(200);
        this.result.setMessage(msg);
        return this.result;
    }

    public Result<T> setErrorMsg(String msg){
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(500);
        return this.result;
    }

    public Result<T> setErrorMsg(Integer code, String msg){
        this.result.setSuccess(false);
        this.result.setMessage(msg);
        this.result.setCode(code);
        return this.result;
    }


}
