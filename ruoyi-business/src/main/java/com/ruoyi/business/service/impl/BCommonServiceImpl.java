package com.ruoyi.business.service.impl;

import com.ruoyi.business.mapper.BCommonMapper;
import com.ruoyi.business.service.IBCommonService;
import com.ruoyi.business.vo.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("SSSSSS")
public class BCommonServiceImpl implements IBCommonService {
    @Autowired
    BCommonMapper BCommonMapper;

    public List<Options> getOptions(String table, String key, String label, String where) {
        if (!where.trim().isEmpty()) {
            where = "where "+ where;
        }
        return BCommonMapper.getLabels(table, key, label, where);
    }
}
