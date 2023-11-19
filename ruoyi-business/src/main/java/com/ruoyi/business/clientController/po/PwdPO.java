package com.ruoyi.business.clientController.po;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PwdPO {
    @NotNull(message = "参数错误")
    private Long userId;
    @NotBlank(message = "旧密码不能为空")
    private String password;
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
