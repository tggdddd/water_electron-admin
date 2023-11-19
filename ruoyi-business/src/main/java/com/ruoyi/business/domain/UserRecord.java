package com.ruoyi.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.business.aspect.annotation.Dict;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户充值记录对象 user_record
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@Data
@TableName("user_record")
@Builder
public class UserRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */

    @TableId(type = IdType.AUTO)
    private Long id;

    /** user表关联 */
    @Excel(name = "user表关联")
    @Dict("sys_user:user_id:nick_name")
    private Long userId;

    /** 充值类型 */
    @Excel(name = "充值类型")
    @Dict("charge_type")
    private String chargeType;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal chargePrice;

    /** 充值时间 */
    @Excel(name = "充值时间")
    private Date chargeTime;

    /** 充值订单 */
    @Excel(name = "充值订单")
    private String chargeCode;

    /** 会员类型 */
    @Excel(name = "会员类型")
    @Dict("charge:id:name")
    private String memberType;
}
