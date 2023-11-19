package com.ruoyi.business.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.base.MPJBaseServiceImpl;
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
public class UserRecordServiceImpl extends MPJBaseServiceImpl<UserRecordMapper,UserRecord> implements IUserRecordService{
    @Autowired
    private UserRecordMapper userRecordMapper;

}
