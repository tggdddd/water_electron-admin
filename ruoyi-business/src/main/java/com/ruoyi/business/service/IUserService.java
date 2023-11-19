package com.ruoyi.business.service;

import com.github.yulichang.base.MPJBaseService;
import com.ruoyi.business.clientController.po.RegisterPO;
import com.ruoyi.business.domain.BusUser;
import com.ruoyi.common.core.domain.AjaxResult;

import javax.mail.MessagingException;

public interface IUserService extends MPJBaseService<BusUser> {
    public String login(String username, String password);
    public boolean sendEmailCode(String  email,String type) throws MessagingException;
    public boolean validateEmailCode(String email,String code,String type);
    public AjaxResult registerUser(RegisterPO registerPO);
}
