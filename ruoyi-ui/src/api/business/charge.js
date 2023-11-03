import request from '@/utils/request'

// 查询充值选项;列表
export function listCharge(query) {
  return request({
    url: '/business/charge/list',
    method: 'get',
    params: query
  })
}

// 查询充值选项;详细
export function getCharge(id) {
  return request({
    url: '/business/charge/' + id,
    method: 'get'
  })
}

// 新增充值选项;
export function addCharge(data) {
  return request({
    url: '/business/charge',
    method: 'post',
    data: data
  })
}

// 修改充值选项;
export function updateCharge(data) {
  return request({
    url: '/business/charge',
    method: 'put',
    data: data
  })
}

// 删除充值选项;
export function delCharge(id) {
  return request({
    url: '/business/charge/' + id,
    method: 'delete'
  })
}
