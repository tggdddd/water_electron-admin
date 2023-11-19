package com.ruoyi.business.service;

import com.ruoyi.business.vo.Options;

import java.util.List;

public interface IBCommonService {
    public List<Options> getOptions(String table, String key, String label, String where);
}
