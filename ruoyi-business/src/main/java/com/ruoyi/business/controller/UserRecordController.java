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
import com.ruoyi.business.domain.UserRecord;
import com.ruoyi.business.service.IUserRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户充值记录Controller
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@RestController
@RequestMapping("/business/record")
public class UserRecordController extends BaseController
{
    @Autowired
    private IUserRecordService userRecordService;

    /**
     * 查询用户充值记录列表
     */
    @PreAuthorize("@ss.hasPermi('business:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserRecord userRecord)
    {
        startPage();
        List<UserRecord> list = userRecordService.selectUserRecordList(userRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户充值记录列表
     */
    @PreAuthorize("@ss.hasPermi('business:record:export')")
    @Log(title = "用户充值记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserRecord userRecord)
    {
        List<UserRecord> list = userRecordService.selectUserRecordList(userRecord);
        ExcelUtil<UserRecord> util = new ExcelUtil<UserRecord>(UserRecord.class);
        util.exportExcel(response, list, "用户充值记录数据");
    }

    /**
     * 获取用户充值记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userRecordService.selectUserRecordById(id));
    }

    /**
     * 新增用户充值记录
     */
    @PreAuthorize("@ss.hasPermi('business:record:add')")
    @Log(title = "用户充值记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserRecord userRecord)
    {
        return toAjax(userRecordService.insertUserRecord(userRecord));
    }

    /**
     * 修改用户充值记录
     */
    @PreAuthorize("@ss.hasPermi('business:record:edit')")
    @Log(title = "用户充值记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserRecord userRecord)
    {
        return toAjax(userRecordService.updateUserRecord(userRecord));
    }

    /**
     * 删除用户充值记录
     */
    @PreAuthorize("@ss.hasPermi('business:record:remove')")
    @Log(title = "用户充值记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userRecordService.deleteUserRecordByIds(ids));
    }
}
