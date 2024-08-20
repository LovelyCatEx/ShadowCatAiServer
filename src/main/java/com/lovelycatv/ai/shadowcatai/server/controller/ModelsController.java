package com.lovelycatv.ai.shadowcatai.server.controller;

import com.lovelycatv.ai.shadowcatai.server.entity.ModelEntity;
import com.lovelycatv.ai.shadowcatai.server.response.Result;
import com.lovelycatv.ai.shadowcatai.server.service.ModelService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-05 17:48
 */
@RestController
@RequestMapping("/model")
public class ModelsController {
    @Resource
    private ModelService modelService;

    @GetMapping("/list")
    public Result<List<ModelEntity>> getModelList() {
        return Result.success(modelService.list());
    }
}
