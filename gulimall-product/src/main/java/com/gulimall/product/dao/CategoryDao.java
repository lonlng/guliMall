package com.gulimall.product.dao;

import com.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author lonn
 * @email lonlng@qq.com
 * @date 2021-02-19 16:01:12
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
