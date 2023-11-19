import request from '@/utils/request'

// 查询用户信息列表
export function listUser(query) {
  return request({
    url: '/business/user/list',
    method: 'get',
    params: query
  })
}

// 新增用户信息
export function addUser(data) {
  return request({
    url: '/business/user',
    method: 'post',
    data: data
  })
}

// 修改用户信息
export function updateUser(data) {
  return request({
    url: '/business/user',
    method: 'put',
    data: data
  })
}

// 删除用户信息
export function delUser(userId) {
  return request({
    url: '/business/user/' + userId,
    method: 'delete'
  })
}

// 查询用户信息详细
export function getUser(userId) {
  return request({
    url: '/business/user/' + userId,
    method: 'get'
  })
}
//获取充值记录
export function getUserChargeRecord(userId,params) {
  return request({
    url: '/business/user/charge/' + userId,
    method: 'get',
    params:params
  })
}

//账号充值
export function userChargeApi(userId,params) {
  return request({
    url: '/business/user/charge/' + userId,
    method: 'post',
    data:params
  })
}
