import request from '@/utils/request'

// 查询列表
export function listpay() {
  return request({
    url: '/business/pay/list',
    method: 'get',
  })
}
export function getpay(id) {
  return request({
    url: '/business/pay/' + id,
    method: 'get'
  })
}
// 新增选项;
export function addpay(data) {
  return request({
    url: '/business/pay',
    method: 'post',
    data: data
  })
}

// 修改选项;
export function updatepay(data) {
  return request({
    url: '/business/pay',
    method: 'put',
    data: data
  })
}

// 删除选项;
export function delpay(id) {
  return request({
    url: '/business/pay/' + id,
    method: 'delete'
  })
}
