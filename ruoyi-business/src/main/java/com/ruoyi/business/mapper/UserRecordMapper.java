package com.ruoyi.business.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.ruoyi.business.domain.UserRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户充值记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@Mapper
public interface UserRecordMapper  extends MPJBaseMapper<UserRecord> {
}
