package com.ruoyi.business.mapper;

import com.ruoyi.business.vo.Options;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BCommonMapper {
    public List<Options> getLabels(@Param("table")String table,
                                   @Param("key")String key,
                                   @Param("label")String label,
                                   @Param("where")String where);
}
