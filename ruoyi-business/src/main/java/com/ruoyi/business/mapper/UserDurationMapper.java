package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.UserDuration;

/**
 * 用户到期时间Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
public interface UserDurationMapper 
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
     * 删除用户到期时间
     * 
     * @param id 用户到期时间主键
     * @return 结果
     */
    public int deleteUserDurationById(Long id);

    /**
     * 批量删除用户到期时间
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserDurationByIds(Long[] ids);
}
