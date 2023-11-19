package com.ruoyi.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.business.controller.PO.ConcatPO;
import com.ruoyi.business.domain.Charge;
import com.ruoyi.business.domain.DictData;
import com.ruoyi.business.service.IChargeService;
import com.ruoyi.business.service.IDictDataService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.LambdaQueryWrapperX;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 客服Controller
 *
 * @author ruoyi
 * @date 2023-11-03
 */
@RestController
@RequestMapping("/business/concat")
public class ConcatController extends BaseController {
    @Autowired
    private IDictDataService dictDataService;
    private String WX_TYPE = "wx_concat";
    private String QQ_TYPE = "qq_concat";

    /**
     * 查询客服列表
     */
    @PreAuthorize("@ss.hasPermi('business:charge:list')")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<DictData> list = dictDataService
                .list(new LambdaQueryWrapper<DictData>()
                        .eq(DictData::getDictType, WX_TYPE)
                        .or()
                        .eq(DictData::getDictType, QQ_TYPE)
                        .orderByDesc(DictData::getDictSort));
        return getDataTable(list);
    }

    /**
     * 获取客服详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:charge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(dictDataService.getById(id));
    }

    /**
     * 新增客服
     */
    @PreAuthorize("@ss.hasPermi('business:charge:add')")
    @Log(title = "客服", businessType = BusinessType.INSERT)
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
     * 修改客服
     */
    @PreAuthorize("@ss.hasPermi('business:charge:edit')")
    @Log(title = "客服", businessType = BusinessType.UPDATE)
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
     * 删除客服
     */
    @PreAuthorize("@ss.hasPermi('business:charge:remove')")
    @Log(title = "客服", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(dictDataService.removeByIds(Arrays.asList(ids)));
    }
}
