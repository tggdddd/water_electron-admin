package com.ruoyi.business.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户充值记录对象 user_record
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@Data
public class UserRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** user表关联 */
    @Excel(name = "user表关联")
    private String userId;

    /** 充值类型 */
    @Excel(name = "充值类型")
    private String chargeType;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private String chargePrice;

    /** 充值时间 */
    @Excel(name = "充值时间")
    private String chargeTime;

    /** 充值订单 */
    @Excel(name = "充值订单")
    private String chargeCode;

    /** 会员类型 */
    @Excel(name = "会员类型")
    private String memberType;
}
