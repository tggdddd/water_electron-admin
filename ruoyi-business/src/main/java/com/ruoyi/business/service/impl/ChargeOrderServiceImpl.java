package com.ruoyi.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.domain.ChargeOrder;
import com.ruoyi.business.mapper.ChargeOrderMapper;
import com.ruoyi.business.service.IChargeOrderService;
import org.springframework.stereotype.Service;

/**
 * 充值订单(ChargeOrder)表服务实现类
 *
 * @author makejava
 * @since 2023-11-20 01:38:22
 */
@Service("chargeOrderService")
public class ChargeOrderServiceImpl extends ServiceImpl<ChargeOrderMapper, ChargeOrder> implements IChargeOrderService {

}

