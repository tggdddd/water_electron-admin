package com.ruoyi.business.mapper;

import java.util.List;

import com.github.yulichang.base.MPJBaseMapper;
import com.ruoyi.business.domain.Charge;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充值选项;Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@Mapper
public interface ChargeMapper extends MPJBaseMapper<Charge>
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
     * 删除充值选项;
     * 
     * @param id 充值选项;主键
     * @return 结果
     */
    public int deleteChargeById(Long id);

    /**
     * 批量删除充值选项;
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChargeByIds(Long[] ids);
}
