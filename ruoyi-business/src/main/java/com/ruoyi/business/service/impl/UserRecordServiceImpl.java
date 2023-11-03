package com.ruoyi.business.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.UserRecordMapper;
import com.ruoyi.business.domain.UserRecord;
import com.ruoyi.business.service.IUserRecordService;

/**
 * 用户充值记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@Service
public class UserRecordServiceImpl implements IUserRecordService 
{
    @Autowired
    private UserRecordMapper userRecordMapper;

    /**
     * 查询用户充值记录
     * 
     * @param id 用户充值记录主键
     * @return 用户充值记录
     */
    @Override
    public UserRecord selectUserRecordById(Long id)
    {
        return userRecordMapper.selectUserRecordById(id);
    }

    /**
     * 查询用户充值记录列表
     * 
     * @param userRecord 用户充值记录
     * @return 用户充值记录
     */
    @Override
    public List<UserRecord> selectUserRecordList(UserRecord userRecord)
    {
        return userRecordMapper.selectUserRecordList(userRecord);
    }

    /**
     * 新增用户充值记录
     * 
     * @param userRecord 用户充值记录
     * @return 结果
     */
    @Override
    public int insertUserRecord(UserRecord userRecord)
    {
        userRecord.setCreateTime(DateUtils.getNowDate());
        return userRecordMapper.insertUserRecord(userRecord);
    }

    /**
     * 修改用户充值记录
     * 
     * @param userRecord 用户充值记录
     * @return 结果
     */
    @Override
    public int updateUserRecord(UserRecord userRecord)
    {
        userRecord.setUpdateTime(DateUtils.getNowDate());
        return userRecordMapper.updateUserRecord(userRecord);
    }

    /**
     * 批量删除用户充值记录
     * 
     * @param ids 需要删除的用户充值记录主键
     * @return 结果
     */
    @Override
    public int deleteUserRecordByIds(Long[] ids)
    {
        return userRecordMapper.deleteUserRecordByIds(ids);
    }

    /**
     * 删除用户充值记录信息
     * 
     * @param id 用户充值记录主键
     * @return 结果
     */
    @Override
    public int deleteUserRecordById(Long id)
    {
        return userRecordMapper.deleteUserRecordById(id);
    }
}
