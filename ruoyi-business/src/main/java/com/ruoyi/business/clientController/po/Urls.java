package com.ruoyi.business.clientController.po;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Urls {
    @NotBlank(message = "链接不能为空")
String urls;
}
