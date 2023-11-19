package com.ruoyi.business.clientController.po;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class HostPo {
    @NotEmpty
    String type;
    @NotEmpty
    String sec_uid;

    String page = "0";
}
