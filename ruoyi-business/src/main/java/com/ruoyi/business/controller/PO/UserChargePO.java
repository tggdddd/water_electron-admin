package com.ruoyi.business.controller.PO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UserChargePO {
    private String chargeType;
    private String code;

    private String remark;

    private String memberType;

    private BigDecimal money;
    @NotNull(message = "参数有误")
    private Boolean chargeMember;
}
