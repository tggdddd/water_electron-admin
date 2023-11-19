package com.ruoyi.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.business.controller.PO.ConcatPO;
import com.ruoyi.business.domain.DictData;
import com.ruoyi.business.service.IDictDataService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 支付Controller
 *
 * @author ruoyi
 * @date 2023-11-03
 */
@RestController
@RequestMapping("/business/pay")
public class PayController extends BaseController {
    @Autowired
    private IDictDataService dictDataService;
    private String WX_TYPE = "wx_pay";
    private String ZFB_TYPE = "zfb_pay";

    /**
     * 查询支付列表
     */
    @PreAuthorize("@ss.hasPermi('business:charge:list')")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<DictData> list = dictDataService
                .list(new LambdaQueryWrapper<DictData>()
                        .eq(DictData::getDictType, WX_TYPE)
                        .or()
                        .eq(DictData::getDictType, ZFB_TYPE)
                        .orderByDesc(DictData::getDictSort));
        return getDataTable(list);
    }

    /**
     * 获取支付详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:charge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(dictDataService.getById(id));
    }

    /**
     * 新增支付
     */
    @PreAuthorize("@ss.hasPermi('business:charge:add')")
    @Log(title = "支付", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConcatPO concatPO) {
        DictData build = DictData.builder()
                .dictLabel(concatPO.getValue())
                .dictValue(concatPO.getValue())
                .dictType(concatPO.getType())
                .dictSort(concatPO.getWeight())
                .build();
        return toAjax(dictDataService.save(build));
    }

    /**
     * 修改支付
     */
    @PreAuthorize("@ss.hasPermi('business:charge:edit')")
    @Log(title = "支付", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConcatPO concatPO) {
        DictData build = DictData.builder()
                .dictCode(concatPO.getId())
                .dictLabel(concatPO.getValue())
                .dictValue(concatPO.getValue())
                .dictType(concatPO.getType())
                .dictSort(concatPO.getWeight())
                .build();
        return toAjax(dictDataService.updateById(build));
    }

    /**
     * 删除支付
     */
    @PreAuthorize("@ss.hasPermi('business:charge:remove')")
    @Log(title = "支付", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(dictDataService.removeByIds(Arrays.asList(ids)));
    }
}
