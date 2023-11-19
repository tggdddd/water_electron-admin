//获取选项组
import request from "@/utils/request";

export function getOptions(table, value, text, where) {
  return request({
    url: '/common/options',
    method: 'get',
    params:{
      t:table,
      i:value,
      l:text,
      f:where,
    }
  })
}
