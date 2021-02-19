package com.gulimall.coupon.dao;

import com.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author lonn
 * @email lonlng@qq.com
 * @date 2021-02-19 16:49:40
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
