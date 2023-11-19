package com.ruoyi.business.aspect;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.business.aspect.annotation.Dict;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName DictAspect
 * @Description   对@AutoDict注解的类或者方法执行后，对@Dict注解的字段自动翻译
 * @Author 肖润杰
 * @Time 2023/4/3 16:45
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class DictAspect {
    private static String DICT_TEXT_SUFFIX = "Text";
    @Autowired
    public RedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * 定义切点Pointcut
     */
    @Pointcut("@within(com.ruoyi.business.aspect.annotation.AutoDict)||@annotation(com.ruoyi.business.aspect.annotation.AutoDict)")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long time1 = System.currentTimeMillis();
        Object result = pjp.proceed();
        long time2 = System.currentTimeMillis();
        log.debug("获取JSON数据 耗时：" + (time2 - time1) + "ms");
        long start = System.currentTimeMillis();
        result = this.parseDictText(result);
        long end = System.currentTimeMillis();
        log.debug("注入字典到JSON数据  耗时" + (end - start) + "ms");
        return result;
    }

    /**
     * 本方法Controller返回数据进行动态字典注入
     * 字典注入实现 通过对实体类添加注解@dict 来标识需要的字典内容
     *
     * @param result
     */
    private Object parseDictText(Object result) {
        if (result instanceof TableDataInfo || result instanceof AjaxResult) {
            // 结果集
            List<Object> records = null;
            // 解决乱序  与 records 类似
            List<JSONObject> items = new ArrayList<>();
            // 需要翻译的字段
            List<Field> dictFieldList = new ArrayList<>();
            boolean isList = true;
            boolean isPage = false;
            Page page = null;
            // 需要翻译的字典数据列表 type,value
            Map<String, List<String>> dataListMap = new HashMap<>(5);
            if (result instanceof AjaxResult) {
                if (((AjaxResult) result).get(AjaxResult.DATA_TAG) == null) {
                    return result;
                }
                Object o = ((AjaxResult) result).get(AjaxResult.DATA_TAG);
                if(o instanceof Page){
                    isPage = true;
                    page = (Page) o;
                    o = ((Page<?>) o).getRecords();
                }
                if (o instanceof List) {
                    records = (List<Object>) o;
                } else {
                    //    单个对象转化List
                    isList = false;
                    records = Collections.singletonList(o);
                }
            } else {
                // 取出结果集
                records = (List<Object>) ((TableDataInfo) result).getRows();
            }
            // step.1 筛选出加了 Dict 注解的字段列表
            // 判断是否含有字典注解,没有注解返回
            Boolean hasDict = checkHasDict(records);
            if (!hasDict) {
                return result;
            }
            log.debug(" __ 进入字典翻译切面 DictAspect —— ");
            // 判断是否含有字典注解,没有注解返回-----
            for (Object record : records) {
                String json = "{}";
                try {
                    // 解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
                    json = objectMapper.writeValueAsString(record);
                } catch (JsonProcessingException e) {
                    log.error("json解析失败" + e.getMessage(), e);
                }
                // 解决返回json数据后key顺序错乱 ---
                JSONObject item = JSONObject.parseObject(json);
                // 遍历所有字段，把字典Code取出来，放到 map 里
                for (Field field : getAllFields(record)) {
                    String value = item.getString(field.getName());
                    if (!StringUtils.hasText(value)) {
                        continue;
                    }
                    // 解决继承实体字段无法翻译问题------
                    if (field.getAnnotation(Dict.class) != null) {
                        if (!dictFieldList.contains(field)) {
                            dictFieldList.add(field);
                        }
                        String type = field.getAnnotation(Dict.class).value();
                        List<String> dataList;
                        dataList = dataListMap.computeIfAbsent(type, k -> new ArrayList<>());
                        this.listAddAllDeduplicate(dataList, Arrays.asList(value.split(",")));
                    }
                }
                items.add(item);
            }

            // step.2 调用翻译方法，一次性翻译
            Map<String, List<DictModel>> translText = this.translateAllDict(dataListMap);

            // step.3 将翻译结果填充到返回结果里
            for (JSONObject record : items) {
                for (Field field : dictFieldList) {
                    String type = field.getAnnotation(Dict.class).value();
                    String value = record.getString(field.getName());
                    if (StringUtils.hasText(value)) {
                        List<DictModel> dictModels = translText.get(type);
                        if (dictModels == null || dictModels.size() == 0) {
                            continue;
                        }
                        String textValue = this.translDictText(dictModels, value);
                        log.debug(" 字典Val : " + textValue);
                        log.debug(" __翻译字典字段__ " + field.getName() + "： " + textValue);

                        record.put(field.getName() + DICT_TEXT_SUFFIX, textValue);
                    }
                }
            }
            if (result instanceof AjaxResult) {
                if(isPage){
                    page.setRecords(items);
                }else
                if (!isList) {
                    ((AjaxResult) result).put(AjaxResult.DATA_TAG, items.get(0));
                } else {
                    ((AjaxResult) result).put(AjaxResult.DATA_TAG, items);
                }
            } else {
                ((TableDataInfo) result).setRows(items);
            }
        }
        return result;
    }

    /**
     * list 去重添加
     */
    private void listAddAllDeduplicate(List<String> dataList, List<String> addList) {
        // 筛选出dataList中没有的数据
        List<String> filterList = addList.stream().filter(i -> !dataList.contains(i)).collect(Collectors.toList());
        dataList.addAll(filterList);
    }

    /**
     * 一次性把所有的字典都翻译了
     * 1.  所有的普通数据字典的所有数据只执行一次SQL
     * 2.  表字典相同的所有数据只执行一次SQL
     *
     * @param dataListMap
     * @return
     */
    private Map<String, List<DictModel>> translateAllDict(Map<String, List<String>> dataListMap) {
        // 翻译后的字典文本，key=dictCode
        Map<String, List<DictModel>> translText = new HashMap<>(5);
        for (String dictCode : dataListMap.keySet()) {
            List<String> dataList = dataListMap.get(dictCode);
            if (dataList.size() == 0) {
                continue;
            }

            //     todo 先通过redis中获取缓存表字典数据
            List<DictModel> list = translText.computeIfAbsent(dictCode, k -> new ArrayList<>());
            //翻译表字典
            if (dictCode.contains(":")){
                String[] strings = dictCode.split(":");
                if (strings.length != 3){
                    throw new RuntimeException("dict注解表翻译有误:"+dictCode);
                }
                String   table =  strings[0];
                String   id =  strings[1];
                String   label =  strings[2];
                String   sql = String.format("select %s as value, %s as label from  %s where  %s = ?",id,label,table,id);
                for (String s : dataList) {
                    try {
                        DictModel dictModel = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(DictModel.class), s);
                        list.add(dictModel);
                    }catch (EmptyResultDataAccessException e){
                        // 修复找不到字典
                        list.add(new DictModel(s, "无"));
                    }
                }
            }else {
                // 调用数据库进行翻译普通字典
                for (String s : dataList) {
                    list.add(new DictModel(s, DictUtils.getDictLabel(dictCode, s)));
                }
            }
        }
        return translText;
    }

    /**
     * 字典值替换文本
     *
     * @param dictModels
     * @param values
     * @return
     */
    private String translDictText(List<DictModel> dictModels, String values) {
        List<String> result = new ArrayList<>();

        // 允许多个逗号分隔，允许传数组对象
        String[] splitVal = values.split(",");
        for (String val : splitVal) {
            String dictText = val;
            for (DictModel dict : dictModels) {
                if (val.equals(dict.getValue())) {
                    dictText = dict.getLabel();
                    break;
                }
            }
            result.add(dictText);
        }
        return String.join(",", result);
    }

    /**
     * 检测返回结果集中是否包含Dict注解
     *
     * @param records
     * @return
     */
    private Boolean checkHasDict(List<Object> records) {
        if (records != null && !records.isEmpty()) {
            for (Field field : getAllFields(records.get(0))) {
                if (field.getAnnotation(Dict.class) != null) {
                    return true;
                }
            }
        }
        return false;
    }

}

@Data
@AllArgsConstructor
class DictModel {
    private String value;
    private String label;
    public DictModel(){

    }
}