package cn.kzm.manage.dto.front;

import lombok.Data;

@Data
public class MemberInfo {

    private Long id;

    private String username;

    private String phone;

    private String sex;

    private String address;


    private String file;

    private String description;

    private Integer points;

    private Long balance;

    private int state;

    private String token;

    private String message;
}
