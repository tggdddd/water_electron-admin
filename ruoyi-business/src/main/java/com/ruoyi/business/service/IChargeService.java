package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.Charge;

/**
 * 充值选项;Service接口
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
public interface IChargeService 
{
    /**
     * 查询充值选项;
     * 
     * @param id 充值选项;主键
     * @return 充值选项;
     */
    public Charge selectChargeById(Long id);

    /**
     * 查询充值选项;列表
     * 
     * @param charge 充值选项;
     * @return 充值选项;集合
     */
    public List<Charge> selectChargeList(Charge charge);

    /**
     * 新增充值选项;
     * 
     * @param charge 充值选项;
     * @return 结果
     */
    public int insertCharge(Charge charge);

    /**
     * 修改充值选项;
     * 
     * @param charge 充值选项;
     * @return 结果
     */
    public int updateCharge(Charge charge);

    /**
     * 批量删除充值选项;
     * 
     * @param ids 需要删除的充值选项;主键集合
     * @return 结果
     */
    public int deleteChargeByIds(Long[] ids);

    /**
     * 删除充值选项;信息
     * 
     * @param id 充值选项;主键
     * @return 结果
     */
    public int deleteChargeById(Long id);
}
