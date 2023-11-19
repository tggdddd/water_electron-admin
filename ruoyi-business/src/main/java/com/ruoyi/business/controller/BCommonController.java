package com.ruoyi.business.controller;

import com.ruoyi.business.service.IBCommonService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class BCommonController {
    @Autowired
    IBCommonService commonService;

    @GetMapping("/options")
    public AjaxResult getOptions(@RequestParam(value = "t") String table,
                                 @RequestParam(value = "i") String id,
                                 @RequestParam(value = "l") String label,
                                 @RequestParam(value = "f",defaultValue = "") String where){
        return AjaxResult.success(commonService.getOptions(table, id, label, where));
    }
}
