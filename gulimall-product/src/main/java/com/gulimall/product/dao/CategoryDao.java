package com.gulimall.product.dao;

import com.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author expo7
 * @email expo7@gmail.com
 * @date 2021-02-18 20:39:03
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}