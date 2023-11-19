package com.ruoyi.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.business.aspect.annotation.AutoDict;
import com.ruoyi.business.controller.PO.UserChargePO;
import com.ruoyi.business.controller.PO.UserPO;
import com.ruoyi.business.domain.BusUser;
import com.ruoyi.business.domain.Charge;
import com.ruoyi.business.domain.UserRecord;
import com.ruoyi.business.service.IChargeService;
import com.ruoyi.business.service.IUserRecordService;
import com.ruoyi.business.service.IUserService;
import com.ruoyi.business.type.UserType;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

@RestController
@AutoDict
@RequestMapping("/business/user")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRecordService recordService;
    @Autowired
    private IChargeService chargeService;
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 查询用户列表
     */
    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                           BusUser busUser) {
        LambdaQueryWrapper<BusUser> wrapper = new LambdaQueryWrapper<BusUser>();
        if (busUser.getUserName() != null && !busUser.getUserName().trim().isEmpty()) {
            wrapper.like(BusUser::getUserName, "%" + busUser.getUserName().trim() + "%");
        }
        if (busUser.getNickName() != null && !busUser.getNickName().trim().isEmpty()) {
            wrapper.like(BusUser::getNickName, "%" + busUser.getNickName().trim() + "%");
        }
        if (busUser.getUserType() != null && !busUser.getUserType().trim().isEmpty()) {
            wrapper.eq(BusUser::getUserType, busUser.getUserType().trim());
        }
        if (busUser.getEmail() != null && !busUser.getEmail().trim().isEmpty()) {
            wrapper.like(BusUser::getEmail, "%" + busUser.getEmail().trim() + "%");
        }
        if (busUser.getPhonenumber() != null && !busUser.getPhonenumber().trim().isEmpty()) {
            wrapper.like(BusUser::getPhonenumber, "%" + busUser.getPhonenumber().trim() + "%");
        }

        if (busUser.getSex() != null && !busUser.getSex().trim().isEmpty()) {
            wrapper.eq(BusUser::getSex, busUser.getSex().trim());
        }
        if (busUser.getStatus() != null && !busUser.getStatus().trim().isEmpty()) {
            wrapper.eq(BusUser::getStatus, busUser.getStatus().trim());
        }
        Page<BusUser> page = userService.page(new Page<>(pageNum, pageSize), wrapper);
        return AjaxResult.success(page);
    }

    /**
     * 获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userService.getById(id));
    }

    /**
     * 获取充值记录列表
     */
    @PreAuthorize("@ss.hasPermi('business:user:chargelist')")
    @GetMapping(value = "/charge/{id}")
    public AjaxResult getChargeRecordList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                          @PathVariable("id") Long id) {
        return success(recordService.page(new Page<UserRecord>(pageNum,pageSize),new LambdaQueryWrapper<UserRecord>().eq(UserRecord::getUserId, id)));
    }

    /**
     * 充值记
     */
    @PreAuthorize("@ss.hasPermi('business:user:charge')")
    @PostMapping(value = "/charge/{id}")
    public AjaxResult charge(@PathVariable("id") Long id,
                             @Validated @RequestBody UserChargePO chargePO) {
        UserRecord record = UserRecord.builder()
                .userId(id)
                .chargeType(chargePO.getChargeType())
                .chargeCode(chargePO.getCode())
                .chargeTime(new Date(System.currentTimeMillis()))
                .chargePrice(chargePO.getMoney())
                .memberType(chargePO.getMemberType()).build();
        TransactionStatus transaction = startTransaction();
        try {
            BusUser user = userService.getById(id);
            if (user == null) throw new Exception();
            if (chargePO.getChargeMember()) {
                Charge charge = chargeService.getById(chargePO.getMemberType());
                if (charge == null) throw new Exception("找不到该会员套餐");
                record.setChargePrice(charge.getPrice());
                //  增加时间
                Date expireTime = user.getExpireTime();
                if (expireTime.before(new Date(System.currentTimeMillis()))) {
                    expireTime = new Date(System.currentTimeMillis());
                }
                long millis = Duration.ofHours(charge.getMonths() * 30 * 24 + charge.getDays() * 24 + charge.getHours()).toMillis();
                user.setExpireTime(new Date(expireTime.getTime()+millis));
//                修改用户类型
                if(Integer.parseInt(user.getUserType())!= UserType.MEMBER.ordinal()){
                    user.setUserType(String.valueOf(UserType.MEMBER.ordinal()));
                }
                boolean b = userService.updateById(user);
                if (!b) throw new Exception();
            } else {
//                增加金额
                BigDecimal money = user.getMoney();
                money = chargePO.getMoney().add(money);
                user.setMoney(money);
                boolean b = userService.updateById(user);
                if (!b) throw new Exception();
                record.setMemberType(null);
            }
//        保存记录
            record.setCreateBy(String.valueOf(getLoginUser().getUserId()));
            record.setCreateTime(new Date(System.currentTimeMillis()));
            boolean save = recordService.save(record);
            if (!save) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transaction);
            return AjaxResult.error(StringUtils.isEmpty(e.getMessage()) ? "服务器异常" : e.getMessage());
        }
        transactionManager.commit(transaction);
        return AjaxResult.success("操作成功");
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('business:user:add')")
    @Log(title = "用户充值记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserPO userPO) {

        if (userService.count(new LambdaQueryWrapper<BusUser>().eq(BusUser::getUserName, userPO.getUserName())) > 0) {
            return AjaxResult.error("用户名已存在");
        }
        if (StringUtils.isNotEmpty(userPO.getEmail()) && userService.count(new LambdaQueryWrapper<BusUser>().eq(BusUser::getEmail, userPO.getEmail())) > 0) {
            return AjaxResult.error("邮箱已存在");
        }
        if (StringUtils.isNotEmpty(userPO.getPhonenumber()) && userService.count(new LambdaQueryWrapper<BusUser>().eq(BusUser::getPhonenumber, userPO.getPhonenumber())) > 0) {
            return AjaxResult.error("手机号已存在");
        }
        if (userPO.getExpireTime() == null) {
            userPO.setExpireTime(new Date(System.currentTimeMillis()));
        }
        BusUser user = BusUser.builder()
                .email(userPO.getEmail())
                .avatar(userPO.getAvatar())
                .nickName(userPO.getNickName())
                .phonenumber(userPO.getPhonenumber())
                .status(userPO.getStatus())
                .sex(userPO.getSex())
                .password(SecurityUtils.encryptPassword(userPO.getPassword()))
                .userName(userPO.getUserName())
                .expireTime(userPO.getExpireTime())
                .build();
//            保存用户
        boolean save = userService.save(user);
        if (!save) {
            return AjaxResult.error("服务器异常");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('business:user:edit')")
    @Log(title = "用户充值记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserPO userPO) {
        if (userService.count(new LambdaQueryWrapper<BusUser>()
                .ne(BusUser::getUserId, userPO.getUserId()).eq(BusUser::getUserName, userPO.getUserName())) > 0) {
            return AjaxResult.error("用户名已存在");
        }
        if (StringUtils.isNotEmpty(userPO.getEmail()) && userService.count(new LambdaQueryWrapper<BusUser>()
                .ne(BusUser::getUserId, userPO.getUserId()).eq(BusUser::getEmail, userPO.getEmail())) > 0) {
            return AjaxResult.error("邮箱已存在");
        }
        if (StringUtils.isNotEmpty(userPO.getPhonenumber()) && userService.count(new LambdaQueryWrapper<BusUser>()
                .ne(BusUser::getUserId, userPO.getUserId()).eq(BusUser::getPhonenumber, userPO.getPhonenumber())) > 0) {
            return AjaxResult.error("手机号已存在");
        }
        BusUser user = BusUser.builder()
                .email(userPO.getEmail())
                .avatar(userPO.getAvatar())
                .nickName(userPO.getNickName())
                .phonenumber(userPO.getPhonenumber())
                .status(userPO.getStatus())
                .sex(userPO.getSex())
                .userName(userPO.getUserName())
                .expireTime(userPO.getExpireTime())
                .userId(userPO.getUserId())
                .build();
        if (StringUtils.isNotEmpty(userPO.getPassword())) {
            user.setPassword(SecurityUtils.encryptPassword(userPO.getPassword()));
        }
//            保存用户
        boolean save = userService.updateById(user);
        if (!save) {
            return AjaxResult.error("服务器异常");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('business:user:remove')")
    @Log(title = "用户充值记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userService.removeByIds(Arrays.asList(ids)));
    }

    private TransactionStatus startTransaction() {
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(defaultTransactionDefinition);
    }
}
