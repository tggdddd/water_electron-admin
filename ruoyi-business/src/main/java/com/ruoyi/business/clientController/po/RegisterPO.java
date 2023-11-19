package com.ruoyi.business.clientController.po;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegisterPO {

    private String avatar;
    @NotEmpty(message = "验证码不能为空")
    private String code;
    @NotEmpty(message = "邮箱不能为空")
    private String email;
    @NotEmpty(message = "账号不能为空")
    private String nickname;
    @NotEmpty(message = "密码不能为空")
    private String password;
    private String phone;
    private Integer sex = 2;
    @NotEmpty(message = "用户名不能为空")
    private String username;

}
