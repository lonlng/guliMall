package com.gulimall.order.dao;

import com.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author lonn
 * @email lonlng@qq.com
 * @date 2021-02-19 17:15:31
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
