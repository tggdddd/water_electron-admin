package com.ruoyi.business.controller.PO;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class UserPO {
    private Long userId;
    private String avatar;
    private String email;
    private Date expireTime;
    private String nickName;
    @NotEmpty(message = "密码不能为空")
    private String password;
    private String phonenumber;
    private String sex;
    private String status;
    @NotEmpty(message = "用户名不能为空")
    private String userName;



}
