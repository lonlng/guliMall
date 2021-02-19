package com.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.gulimall.member.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author lonn
 * @email lonlng@qq.com
 * @date 2021-02-19 17:07:13
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

