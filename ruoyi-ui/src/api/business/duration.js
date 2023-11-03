import request from '@/utils/request'

// 查询用户到期时间列表
export function listDuration(query) {
  return request({
    url: '/business/duration/list',
    method: 'get',
    params: query
  })
}

// 查询用户到期时间详细
export function getDuration(id) {
  return request({
    url: '/business/duration/' + id,
    method: 'get'
  })
}

// 新增用户到期时间
export function addDuration(data) {
  return request({
    url: '/business/duration',
    method: 'post',
    data: data
  })
}

// 修改用户到期时间
export function updateDuration(data) {
  return request({
    url: '/business/duration',
    method: 'put',
    data: data
  })
}

// 删除用户到期时间
export function delDuration(id) {
  return request({
    url: '/business/duration/' + id,
    method: 'delete'
  })
}
