package com.lovelycatv.ai.shadowcatai.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovelycatv.ai.shadowcatai.server.entity.ModelEntity;
import com.lovelycatv.ai.shadowcatai.server.mapper.ModelMapper;
import com.lovelycatv.ai.shadowcatai.server.service.ModelService;
import org.springframework.stereotype.Service;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-03 21:57
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, ModelEntity> implements ModelService {
}
