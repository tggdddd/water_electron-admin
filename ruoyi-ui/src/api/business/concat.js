import request from '@/utils/request'

// 查询列表
export function listConcat() {
  return request({
    url: '/business/concat/list',
    method: 'get',
  })
}
export function getConcat(id) {
  return request({
    url: '/business/concat/' + id,
    method: 'get'
  })
}
// 新增选项;
export function addConcat(data) {
  return request({
    url: '/business/concat',
    method: 'post',
    data: data
  })
}

// 修改选项;
export function updateConcat(data) {
  return request({
    url: '/business/concat',
    method: 'put',
    data: data
  })
}

// 删除选项;
export function delConcat(id) {
  return request({
    url: '/business/concat/' + id,
    method: 'delete'
  })
}
