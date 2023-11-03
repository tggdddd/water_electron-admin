package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.UserDuration;

/**
 * 用户到期时间Service接口
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
public interface IUserDurationService 
{
    /**
     * 查询用户到期时间
     * 
     * @param id 用户到期时间主键
     * @return 用户到期时间
     */
    public UserDuration selectUserDurationById(Long id);

    /**
     * 查询用户到期时间列表
     * 
     * @param userDuration 用户到期时间
     * @return 用户到期时间集合
     */
    public List<UserDuration> selectUserDurationList(UserDuration userDuration);

    /**
     * 新增用户到期时间
     * 
     * @param userDuration 用户到期时间
     * @return 结果
     */
    public int insertUserDuration(UserDuration userDuration);

    /**
     * 修改用户到期时间
     * 
     * @param userDuration 用户到期时间
     * @return 结果
     */
    public int updateUserDuration(UserDuration userDuration);

    /**
     * 批量删除用户到期时间
     * 
     * @param ids 需要删除的用户到期时间主键集合
     * @return 结果
     */
    public int deleteUserDurationByIds(Long[] ids);

    /**
     * 删除用户到期时间信息
     * 
     * @param id 用户到期时间主键
     * @return 结果
     */
    public int deleteUserDurationById(Long id);
}
