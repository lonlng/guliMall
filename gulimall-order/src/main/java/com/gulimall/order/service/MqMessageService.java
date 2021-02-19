package com.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.utils.PageUtils;
import com.gulimall.order.entity.MqMessageEntity;

import java.util.Map;

/**
 * 
 *
 * @author lonn
 * @email lonlng@qq.com
 * @date 2021-02-19 17:15:31
 */
public interface MqMessageService extends IService<MqMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

