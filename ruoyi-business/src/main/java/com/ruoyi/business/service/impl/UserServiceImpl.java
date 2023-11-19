package com.ruoyi.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.ruoyi.business.bo.EmailBuilder;
import com.ruoyi.business.clientController.po.RegisterPO;
import com.ruoyi.business.config.EmailProperties;
import com.ruoyi.business.domain.BusUser;
import com.ruoyi.business.mapper.UserMapper;
import com.ruoyi.business.service.IUserService;
import com.ruoyi.business.type.UserType;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, BusUser> implements IUserService {
    @Autowired
    SysLoginService sysLoginService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private JavaMailSender mailSender;
    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private EmailProperties emailProperties;
    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String login(String username, String password) {
        // 登录前置校验
        sysLoginService.loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        sysLoginService.recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return boolean
     */
    public boolean sendEmailCode(String  email,String type) throws MessagingException {
        String code = UUID.randomUUID().toString().substring(0, 4);
        redisCache.setCacheObject("authemail:"+email+":"+type, code,30, TimeUnit.MINUTES);
        EmailBuilder emailBuilder = EmailBuilder.builder()
                .tos(new String[]{email})
                .subject("邮箱验证码")
                .content("<div><span>你的邮箱验证码为</span><span style='color:red'>"+code+"</span></div>").build();
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
        minehelper.setFrom(emailProperties.getUsername());
        minehelper.setTo(emailBuilder.getTos());
        minehelper.setSubject(emailBuilder.getSubject());
        minehelper.setText(emailBuilder.getContent(), true);
        try {
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 校验邮箱验证码
     * @param email
     * @param code
     * @return
     */
    public boolean validateEmailCode(String email,String code,String type){
        Object cacheObject = redisCache.getCacheObject("authemail:" + email+":"+type);
        if(cacheObject!=null && Objects.equals( code,cacheObject)){
            return  true;
        }
        return  false;
    }

    /**
     * 注册账号
     * @param registerPO
     * @return
     */
    public AjaxResult registerUser(RegisterPO registerPO){
        if (count(new LambdaQueryWrapper<BusUser>().eq(BusUser::getUserName, registerPO.getUsername())) > 0) {
            return AjaxResult.error("用户名已存在");
        }
        if (StringUtils.isNotEmpty(registerPO.getEmail()) && count(new LambdaQueryWrapper<BusUser>().eq(BusUser::getEmail, registerPO.getEmail())) > 0) {
            return AjaxResult.error("邮箱已存在");
        }
        if (StringUtils.isNotEmpty(registerPO.getPhone()) && count(new LambdaQueryWrapper<BusUser>().eq(BusUser::getPhonenumber, registerPO.getPhone())) > 0) {
            return AjaxResult.error("手机号已存在");
        }
        if(!validateEmailCode(registerPO.getEmail(),registerPO.getCode(),"0")){
            return AjaxResult.error("验证码错误");
        }
        BusUser user = BusUser.builder()
                .email(registerPO.getEmail())
                .avatar(registerPO.getAvatar())
                .nickName(registerPO.getNickname())
                .phonenumber(registerPO.getPhone())
                .sex(String.valueOf(registerPO.getSex()))
                .password(SecurityUtils.encryptPassword(registerPO.getPassword()))
                .userName(registerPO.getUsername())
                .expireTime(new Date(System.currentTimeMillis()+Duration.ofHours(3L).toMillis()))
                .userType(String.valueOf(UserType.NORMAL.ordinal()))
                .build();
//            保存用户
        boolean save = save(user);
        return save?AjaxResult.success("注册成功"):AjaxResult.error("服务器异常");
    }
}
