package com.gulimall.member.dao;

import com.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author lonn
 * @email lonlng@qq.com
 * @date 2021-02-19 17:07:13
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
