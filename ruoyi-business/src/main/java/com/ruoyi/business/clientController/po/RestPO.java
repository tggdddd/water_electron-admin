package com.ruoyi.business.clientController.po;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RestPO {
    @NotEmpty(message = "验证码不能为空")
    private String code;
    @NotEmpty(message = "邮箱不能为空")
    private String email;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
