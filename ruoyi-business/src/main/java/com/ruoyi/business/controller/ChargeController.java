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
import com.ruoyi.business.domain.Charge;
import com.ruoyi.business.service.IChargeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 充值套餐Controller
 *
 * @author ruoyi
 * @date 2023-11-03
 */
@RestController
@RequestMapping("/business/charge")
public class ChargeController extends BaseController {
    @Autowired
    private IChargeService chargeService;

    /**
     * 查询充值套餐列表
     */
    @PreAuthorize("@ss.hasPermi('business:charge:list')")
    @GetMapping("/list")
    public TableDataInfo list(Charge charge) {
        startPage();
        List<Charge> list = chargeService.selectChargeList(charge);
        return getDataTable(list);
    }

    /**
     * 导出充值套餐列表
     */
    @PreAuthorize("@ss.hasPermi('business:charge:export')")
    @Log(title = "充值套餐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Charge charge) {
        List<Charge> list = chargeService.selectChargeList(charge);
        ExcelUtil<Charge> util = new ExcelUtil<Charge>(Charge.class);
        util.exportExcel(response, list, "充值套餐数据");
    }

    /**
     * 获取充值套餐详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:charge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(chargeService.selectChargeById(id));
    }

    /**
     * 新增充值套餐
     */
    @PreAuthorize("@ss.hasPermi('business:charge:add')")
    @Log(title = "充值套餐", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Charge charge) {
        try {
            Float v = Float.valueOf(charge.getPrice());
        } catch (Exception e) {
            return AjaxResult.error("价格格式有误");
        }
        return toAjax(chargeService.insertCharge(charge));
    }

    /**
     * 修改充值套餐
     */
    @PreAuthorize("@ss.hasPermi('business:charge:edit')")
    @Log(title = "充值套餐", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Charge charge) {
        try {
            Float v = Float.valueOf(charge.getPrice());
        } catch (Exception e) {
            return AjaxResult.error("价格格式有误");
        }
        return toAjax(chargeService.updateCharge(charge));
    }

    /**
     * 删除充值套餐
     */
    @PreAuthorize("@ss.hasPermi('business:charge:remove')")
    @Log(title = "充值套餐", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(chargeService.deleteChargeByIds(ids));
    }
}
