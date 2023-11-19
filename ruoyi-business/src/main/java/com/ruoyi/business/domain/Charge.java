package com.ruoyi.business.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 充值选项;对象 charge
 *
 * @author ruoyi
 * @date 2023-11-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("charge")
public class Charge extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 价格
     */
    @Excel(name = "价格")
    private BigDecimal price;


    /**
     * 月
     */
    @Excel(name = "月")
    private Integer months = 0;


    /**
     * 周
     */
    @Excel(name = "周")
    private Integer weeks = 0;


    /**
     * 天
     */
    @Excel(name = "天")
    private Integer days = 0;


    /**
     * 小时
     */
    @Excel(name = "小时")
    private Integer hours = 0;


    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long weight;


}
