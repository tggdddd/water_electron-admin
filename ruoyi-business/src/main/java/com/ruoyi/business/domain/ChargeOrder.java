package com.ruoyi.business.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 充值订单(ChargeOrder)表实体类
 *
 * @author makejava
 * @since 2023-11-20 01:38:22
 */
@Data
@Builder
public class ChargeOrder {

    private Long id;
//用户id
    private Long userId;
//充值项目
    private Long chargeId;
//金额
    private BigDecimal money;
//订单号
    private String orderNo;
    //支付订单号
    private String payNo;
//创建时间
    private Date createTime;
//xiu
    private Date updateTime;
}

