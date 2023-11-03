package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.UserRecord;

/**
 * 用户充值记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
public interface UserRecordMapper 
{
    /**
     * 查询用户充值记录
     * 
     * @param id 用户充值记录主键
     * @return 用户充值记录
     */
    public UserRecord selectUserRecordById(Long id);

    /**
     * 查询用户充值记录列表
     * 
     * @param userRecord 用户充值记录
     * @return 用户充值记录集合
     */
    public List<UserRecord> selectUserRecordList(UserRecord userRecord);

    /**
     * 新增用户充值记录
     * 
     * @param userRecord 用户充值记录
     * @return 结果
     */
    public int insertUserRecord(UserRecord userRecord);

    /**
     * 修改用户充值记录
     * 
     * @param userRecord 用户充值记录
     * @return 结果
     */
    public int updateUserRecord(UserRecord userRecord);

    /**
     * 删除用户充值记录
     * 
     * @param id 用户充值记录主键
     * @return 结果
     */
    public int deleteUserRecordById(Long id);

    /**
     * 批量删除用户充值记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserRecordByIds(Long[] ids);
}
