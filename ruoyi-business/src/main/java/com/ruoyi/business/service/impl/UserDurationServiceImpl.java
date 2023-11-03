package com.ruoyi.business.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.UserDurationMapper;
import com.ruoyi.business.domain.UserDuration;
import com.ruoyi.business.service.IUserDurationService;

/**
 * 用户到期时间Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@Service
public class UserDurationServiceImpl implements IUserDurationService 
{
    @Autowired
    private UserDurationMapper userDurationMapper;

    /**
     * 查询用户到期时间
     * 
     * @param id 用户到期时间主键
     * @return 用户到期时间
     */
    @Override
    public UserDuration selectUserDurationById(Long id)
    {
        return userDurationMapper.selectUserDurationById(id);
    }

    /**
     * 查询用户到期时间列表
     * 
     * @param userDuration 用户到期时间
     * @return 用户到期时间
     */
    @Override
    public List<UserDuration> selectUserDurationList(UserDuration userDuration)
    {
        return userDurationMapper.selectUserDurationList(userDuration);
    }

    /**
     * 新增用户到期时间
     * 
     * @param userDuration 用户到期时间
     * @return 结果
     */
    @Override
    public int insertUserDuration(UserDuration userDuration)
    {
        userDuration.setCreateTime(DateUtils.getNowDate());
        return userDurationMapper.insertUserDuration(userDuration);
    }

    /**
     * 修改用户到期时间
     * 
     * @param userDuration 用户到期时间
     * @return 结果
     */
    @Override
    public int updateUserDuration(UserDuration userDuration)
    {
        userDuration.setUpdateTime(DateUtils.getNowDate());
        return userDurationMapper.updateUserDuration(userDuration);
    }

    /**
     * 批量删除用户到期时间
     * 
     * @param ids 需要删除的用户到期时间主键
     * @return 结果
     */
    @Override
    public int deleteUserDurationByIds(Long[] ids)
    {
        return userDurationMapper.deleteUserDurationByIds(ids);
    }

    /**
     * 删除用户到期时间信息
     * 
     * @param id 用户到期时间主键
     * @return 结果
     */
    @Override
    public int deleteUserDurationById(Long id)
    {
        return userDurationMapper.deleteUserDurationById(id);
    }
}
