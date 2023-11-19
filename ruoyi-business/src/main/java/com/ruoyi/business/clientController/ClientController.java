package com.ruoyi.business.clientController;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.business.clientController.po.*;
import com.ruoyi.business.domain.BusUser;
import com.ruoyi.business.domain.Charge;
import com.ruoyi.business.domain.DictData;
import com.ruoyi.business.mapper.BCommonMapper;
import com.ruoyi.business.service.IChargeOrderService;
import com.ruoyi.business.service.IChargeService;
import com.ruoyi.business.service.IDictDataService;
import com.ruoyi.business.service.IUserService;
import com.ruoyi.business.tool.NewVideoRemoveWatermarkToolConstant;
import com.ruoyi.business.tool.OwnVideoRemoveWatermarkToolApiConstant;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    IUserService userService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private BCommonMapper bCommonMapper;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private IDictDataService dictService;
    @Autowired
    private IChargeOrderService chargeOrderService;
    private String WX_TYPE = "wx_concat";
    private String QQ_TYPE = "qq_concat";
    private String WX_PAY = "wx_pay";
    private String ZFB_PAY = "zfb_pay";
    @Autowired
    private IChargeService chargeService;
    @Autowired
    private TokenService tokenService;
    /**
     * 上传头像
     */
    @PostMapping("/uploadAvatar")
    @Anonymous
    public AjaxResult uploadAvatar(MultipartFile file) throws IOException {
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath();
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        String url = serverConfig.getUrl() + fileName;
        AjaxResult ajax = AjaxResult.success();
        ajax.put("url", url);
        ajax.put("fileName", fileName);
        ajax.put("newFileName", FileUtils.getName(fileName));
        ajax.put("originalFilename", file.getOriginalFilename());
        return ajax;
    }

    /**
     * 登录
     *
     * @param loginPO
     * @return
     */
    @PostMapping("/login")
    @Anonymous
    public AjaxResult login(@Validated @RequestBody LoginPO loginPO) {
        String token = userService.login(loginPO.getUsername(), loginPO.getPassword());
        if(loginPO.getRemember()){
            tokenService.refreshToken(SecurityUtils.getLoginUser(),true);
        }
        BusUser one = userService.getOne(new LambdaQueryWrapper<BusUser>().eq(BusUser::getUserName, loginPO.getUsername()));
//        one.setUserTypeText(dictDataService.selectDictLabel("user_type", one.getUserType()));
        JSONObject json = JSONObject.from(one);
        json.put("userTypeText", dictDataService.selectDictLabel("user_type", one.getUserType()));
        return Objects.requireNonNull(new AjaxResult(200, "登录成功").put(Constants.TOKEN, token)).put("user", json);
    }

    /**
     * 注册
     *
     * @param registerPO
     * @return
     */
    @PostMapping("/register")
    @Anonymous
    public AjaxResult register(@Validated @RequestBody RegisterPO registerPO) {
        return userService.registerUser(registerPO);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */
    @PostMapping("/sendEmailCode/{email}")
    @Anonymous
    public AjaxResult sendEmailCode(@PathVariable("email") String email, @RequestParam(value = "type", defaultValue = "0") String type) {
        try {
            long count = userService.count(new LambdaQueryWrapper<BusUser>().eq(BusUser::getEmail, email));
            if (type.equals("0") && count > 0) {
                return AjaxResult.error("该邮箱已注册");
            }
            if (type.equals("1") && count == 0) {
                return AjaxResult.error("该邮箱未注册");
            }
            boolean flag = userService.sendEmailCode(email, type);
            return flag ? AjaxResult.success("发送成功") : AjaxResult.error("邮箱发送失败");
        } catch (Exception e) {
            return AjaxResult.error("邮箱发送失败");
        }
    }

    @PostMapping("/restPassword")
    @Anonymous
    public AjaxResult restPassword(@Validated @RequestBody RestPO restPO) {
        BusUser user = userService.getOne(new LambdaQueryWrapper<BusUser>().eq(BusUser::getEmail, restPO.getEmail()));
        if (user == null) {
            return AjaxResult.error("未找到账号");
        }
        boolean b = userService.validateEmailCode(restPO.getEmail(), restPO.getCode(), "1");
        if (!b) {
            return AjaxResult.error("验证码错误");
        }
        user.setPassword(SecurityUtils.encryptPassword(restPO.getPassword()));
        boolean b1 = userService.updateById(user);
        if (!b1) {
            return AjaxResult.error("服务器异常");
        }
        return AjaxResult.success("密码已重置");
    }

    /**
     * 获取客服列表
     */
    @PostMapping("/concatList")
    @Anonymous
    public AjaxResult getConcatList() {
        List<DictData> list = dictService.list(new LambdaQueryWrapper<DictData>().eq(DictData::getDictType, WX_TYPE).or().eq(DictData::getDictType, QQ_TYPE).orderByAsc(DictData::getDictType).orderByDesc(DictData::getDictSort));
        return AjaxResult.success(list);
    }

    /**
     * 获取付款码
     */
    @PostMapping("/payCode")
    @Anonymous
    public AjaxResult getPayImg(@RequestParam(value = "type", defaultValue = "0") Integer type) {
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<DictData>();
        if (type == 0) {
            queryWrapper.eq(DictData::getDictType, WX_PAY);
        } else {
            queryWrapper.eq(DictData::getDictType, QQ_TYPE);
        }
        List<DictData> list = dictService.list(queryWrapper);
        if (list == null || list.size() == 0) {
            return AjaxResult.error("服务器异常");
        }
        return AjaxResult.success(list.get(0));
    }

    /**
     * 修改密码
     */
    @PostMapping("/changePwd")
    public AjaxResult changePassWord(@Validated @RequestBody PwdPO pwd) {
        Long userId = SecurityUtils.getUserId();
        if (!pwd.getUserId().equals(userId)) {
            return AjaxResult.error("错误的操作");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (!SecurityUtils.matchesPassword(pwd.getPassword(), loginUser.getPassword())) {
            return AjaxResult.error("密码错误");
        }
        boolean b = userService.updateById(BusUser.builder().userId(pwd.getUserId()).password(SecurityUtils.encryptPassword(pwd.getNewPassword())).build());
        return b ? AjaxResult.success("修改成功") : AjaxResult.error("服务器异常");
    }

    /**
     * 获取会员列表
     */
    @PostMapping("/charge/list")
    @Anonymous
    public AjaxResult chargeList() {
//        会员列表
        List<Charge> list = chargeService.list(new LambdaQueryWrapper<Charge>().orderByDesc(Charge::getWeight));
        LambdaQueryWrapper<DictData> queryWrapper = new LambdaQueryWrapper<DictData>();
        queryWrapper.eq(DictData::getDictType, WX_PAY).or().eq(DictData::getDictType, ZFB_PAY);
        List<DictData> payList = dictService.list(queryWrapper);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("charge",list);
        jsonObject.put("pay",payList);
        return AjaxResult.success(jsonObject);
    }

    /**
     * 视频解析
     * @param urls
     * @return
     */
    @RequestMapping("/reduceVideo")
    @Anonymous
    public AjaxResult reduceVideo(@Validated @RequestBody Urls urls) {
        String[] urlList = urls.getUrls().split(",");
        List<Object> list = new ArrayList<>(urlList.length);
        for (String url : urlList) {
            list.add(OwnVideoRemoveWatermarkToolApiConstant.removeWatermark(url));
        }
        return AjaxResult.success(list);
    }

    /**
     * 获取下载链接
     * @param type
     * @param id
     * @return
     */
    @RequestMapping("/downloadUrl")
    @Anonymous
    public AjaxResult getDownloadUrl(@RequestParam("type") String type,@RequestParam("id")String id){
        String url;
        switch (type) {
            case "bilibili":
            case "1":
                url = NewVideoRemoveWatermarkToolConstant.bilibiliUrlById(id);
                return AjaxResult.success(url);
            case "快手":
            case "2":
//                result = NewVideoRemoveWatermarkToolConstant.kuaishouHost(hostPo.getSec_uid());
//                return AjaxResult.success(result);
            case "西瓜":
            case "3":
                url = NewVideoRemoveWatermarkToolConstant.xiguaUrlById(id);
                return AjaxResult.success(url);
            case "微博":
            case "4":
//                result = NewVideoRemoveWatermarkToolConstant.weiboHost();
//                return AjaxResult.success(result);
            case "小红书":
            case "5":
                url = NewVideoRemoveWatermarkToolConstant.xiaohongshuUrlById(id);
                return AjaxResult.success(url);
            case "抖音":
            case "6":
//                cn.hutool.json.JSONObject data = OwnVideoRemoveWatermarkToolApiConstant.douyinAwemeSub(hostPo.getSec_uid(), hostPo.getPage());
//                return  AjaxResult.success(data);
            default:
                return AjaxResult.error("链接获取失败");
        }
    }

    /**
     * 解析主页
     * @param hostPo
     * @return
     */
    @RequestMapping("/resolveHost")
    @Anonymous
    public AjaxResult reduce(@Validated @RequestBody HostPo hostPo) {
        System.out.printf("解析 %s %s 主页\n", hostPo.getType(), hostPo.getSec_uid());
        JSONObject result = null;
        try {
            switch (hostPo.getType()) {
                case "bilibili":
                case "1":
                    result = NewVideoRemoveWatermarkToolConstant.bilibiliHost(hostPo.getSec_uid(), Integer.valueOf(hostPo.getPage()));
                    return AjaxResult.success(result);
                case "快手":
                case "2":
                    result = NewVideoRemoveWatermarkToolConstant.kuaishouHost(hostPo.getSec_uid());
                    return AjaxResult.success(result);
                case "西瓜":
                case "3":
                    result = NewVideoRemoveWatermarkToolConstant.xiguaHost(hostPo.getSec_uid(), Integer.valueOf(hostPo.getPage()));
                    return AjaxResult.success(result);
                case "微博":
                case "4":
                    result = NewVideoRemoveWatermarkToolConstant.weiboHost();
                    return AjaxResult.success(result);
                case "小红书":
                case "5":
                    result = NewVideoRemoveWatermarkToolConstant.xiaohongshuHost(hostPo.getSec_uid());
                    return AjaxResult.success(result);
                case "抖音":
                case "6":
                    cn.hutool.json.JSONObject data = OwnVideoRemoveWatermarkToolApiConstant.douyinAwemeSub(hostPo.getSec_uid(), hostPo.getPage());
                    return  AjaxResult.success(data);
                default:
                    return AjaxResult.error("主页解析失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成购买订单
     */
    @RequestMapping("/generateOrder/{id}")
    public AjaxResult generateOrder(@PathVariable("id")String id){
        Long userId = SecurityUtils.getUserId();
        Charge charge = chargeService.getById(id);
//        ChargeOrder order = ChargeOrder.builder()
//                .userId(userId)
//                .chargeId(charge.getId())
//                .money(charge.getPrice())
//                .orderNo(UUID.randomUUID().toString().replaceAll("-", ""))
//                .payNo().build();
//        chargeOrderService.save(order);
        return null;
    }
    public static void main(String[] args) throws IOException {
        cn.hutool.json.JSONObject kuaishou = OwnVideoRemoveWatermarkToolApiConstant.weibo("https://weibo.com/tv/show/1034:4969476085448754?mid=4969476715187308");
        System.out.println(kuaishou);
    }
}



