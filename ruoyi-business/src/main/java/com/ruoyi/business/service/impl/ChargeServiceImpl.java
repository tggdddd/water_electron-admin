package com.ruoyi.business.service.impl;

import java.util.List;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.ChargeMapper;
import com.ruoyi.business.domain.Charge;
import com.ruoyi.business.service.IChargeService;

/**
 * 充值选项;Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@Service
public class ChargeServiceImpl extends MPJBaseServiceImpl<ChargeMapper,Charge> implements IChargeService
{
    @Autowired
    private ChargeMapper chargeMapper;

    /**
     * 查询充值选项;
     * 
     * @param id 充值选项;主键
     * @return 充值选项;
     */
    @Override
    public Charge selectChargeById(Long id)
    {
        return chargeMapper.selectChargeById(id);
    }

    /**
     * 查询充值选项;列表
     * 
     * @param charge 充值选项;
     * @return 充值选项;
     */
    @Override
    public List<Charge> selectChargeList(Charge charge)
    {
        return chargeMapper.selectChargeList(charge);
    }

    /**
     * 新增充值选项;
     * 
     * @param charge 充值选项;
     * @return 结果
     */
    @Override
    public int insertCharge(Charge charge)
    {
        charge.setCreateTime(DateUtils.getNowDate());
        return chargeMapper.insertCharge(charge);
    }

    /**
     * 修改充值选项;
     * 
     * @param charge 充值选项;
     * @return 结果
     */
    @Override
    public int updateCharge(Charge charge)
    {
        charge.setUpdateTime(DateUtils.getNowDate());
        return chargeMapper.updateCharge(charge);
    }

    /**
     * 批量删除充值选项;
     * 
     * @param ids 需要删除的充值选项;主键
     * @return 结果
     */
    @Override
    public int deleteChargeByIds(Long[] ids)
    {
        return chargeMapper.deleteChargeByIds(ids);
    }

    /**
     * 删除充值选项;信息
     * 
     * @param id 充值选项;主键
     * @return 结果
     */
    @Override
    public int deleteChargeById(Long id)
    {
        return chargeMapper.deleteChargeById(id);
    }
}
