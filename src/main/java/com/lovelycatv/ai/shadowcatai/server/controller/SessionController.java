package com.lovelycatv.ai.shadowcatai.server.controller;

import com.lovelycatv.ai.shadowcatai.server.response.Result;
import com.lovelycatv.ai.shadowcatai.server.service.SessionService;
import com.lovelycatv.ai.shadowcatai.server.util.PrincipalUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-05 17:49
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    @Resource
    private PrincipalUtils principalUtils;
    @Resource
    private SessionService sessionService;

    @PostMapping("/create")
    public Result<?> createSession(
            @RequestHeader("Authorization") String token,
            @RequestParam("name") String name,
            @RequestParam("modelId") Long modelId
    ) {
        sessionService.createSession(name, principalUtils.getUserIdFromToken(token), modelId);
        return Result.success("Session created successfully");
    }

    @GetMapping("/mine")
    public Result<?> getMySessions(@RequestHeader("Authorization") String token) {
        Long userId = principalUtils.getUserIdFromToken(token);
        return Result.success(sessionService.getSessionsByUserId(userId));
    }

    @PostMapping("/delete")
    public Result<?> deleteSession(
            @RequestHeader("Authorization") String token,
            @RequestParam("sessionId") String sessionId
    ) {
        sessionService.deleteSession(principalUtils.getUserIdFromToken(token), sessionId, false);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<?> updateSession(
            @RequestHeader("Authorization") String token,
            @RequestParam("sessionId") String sessionId,
            @RequestParam("name") String name,
            @RequestParam("modelId") Long modelId
    ) {
        sessionService.updateSession(principalUtils.getUserIdFromToken(token), sessionId, name, modelId);
        return Result.success();
    }

    @PostMapping("/branch")
    public Result<?> createBranch(
            @RequestHeader("Authorization") String token,
            @RequestParam("sessionId") String originalSessionId,
            @RequestParam("before") Long before,
            @RequestParam("name") String name,
            @RequestParam("modelId") Long modelId
    ) {
        if (before == 0L) {
            before = System.currentTimeMillis();
        }
        sessionService.createBranch(originalSessionId, before, name, principalUtils.getUserIdFromToken(token), modelId);
        return Result.success("Session branch created successfully");
    }
}
