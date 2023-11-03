import request from '@/utils/request'

// 查询用户充值记录列表
export function listRecord(query) {
  return request({
    url: '/business/record/list',
    method: 'get',
    params: query
  })
}

// 查询用户充值记录详细
export function getRecord(id) {
  return request({
    url: '/business/record/' + id,
    method: 'get'
  })
}

// 新增用户充值记录
export function addRecord(data) {
  return request({
    url: '/business/record',
    method: 'post',
    data: data
  })
}

// 修改用户充值记录
export function updateRecord(data) {
  return request({
    url: '/business/record',
    method: 'put',
    data: data
  })
}

// 删除用户充值记录
export function delRecord(id) {
  return request({
    url: '/business/record/' + id,
    method: 'delete'
  })
}
