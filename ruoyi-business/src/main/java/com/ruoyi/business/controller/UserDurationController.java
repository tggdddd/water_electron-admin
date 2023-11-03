package com.ruoyi.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.business.domain.UserDuration;
import com.ruoyi.business.service.IUserDurationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户到期时间Controller
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@RestController
@RequestMapping("/business/duration")
public class UserDurationController extends BaseController
{
    @Autowired
    private IUserDurationService userDurationService;

    /**
     * 查询用户到期时间列表
     */
    @PreAuthorize("@ss.hasPermi('business:duration:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserDuration userDuration)
    {
        startPage();
        List<UserDuration> list = userDurationService.selectUserDurationList(userDuration);
        return getDataTable(list);
    }

    /**
     * 导出用户到期时间列表
     */
    @PreAuthorize("@ss.hasPermi('business:duration:export')")
    @Log(title = "用户到期时间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserDuration userDuration)
    {
        List<UserDuration> list = userDurationService.selectUserDurationList(userDuration);
        ExcelUtil<UserDuration> util = new ExcelUtil<UserDuration>(UserDuration.class);
        util.exportExcel(response, list, "用户到期时间数据");
    }

    /**
     * 获取用户到期时间详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:duration:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userDurationService.selectUserDurationById(id));
    }

    /**
     * 新增用户到期时间
     */
    @PreAuthorize("@ss.hasPermi('business:duration:add')")
    @Log(title = "用户到期时间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserDuration userDuration)
    {
        return toAjax(userDurationService.insertUserDuration(userDuration));
    }

    /**
     * 修改用户到期时间
     */
    @PreAuthorize("@ss.hasPermi('business:duration:edit')")
    @Log(title = "用户到期时间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserDuration userDuration)
    {
        return toAjax(userDurationService.updateUserDuration(userDuration));
    }

    /**
     * 删除用户到期时间
     */
    @PreAuthorize("@ss.hasPermi('business:duration:remove')")
    @Log(title = "用户到期时间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userDurationService.deleteUserDurationByIds(ids));
    }
}
