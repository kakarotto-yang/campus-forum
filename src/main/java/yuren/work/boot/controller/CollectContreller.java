package yuren.work.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.pojo.Collect;
import yuren.work.boot.pojo.Praise;
import yuren.work.boot.service.CollectService;
import yuren.work.boot.service.PraiseService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/auth/user/collect")
@Api(tags = "收藏模块")
@Slf4j
@CrossOrigin
public class CollectContreller {
    @Autowired
    CollectService collectService;

    @ApiOperation(value = "用户收藏接口")
    @GetMapping(value = "{postId}")
    public Result praise(@PathVariable Integer postId, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        Collect collect = new Collect();
        collect.setUid(uid);
        collect.setPostId(postId);
        collectService.collect(collect);
        return Result.ok();
    }

    @ApiOperation(value = "根据帖子id检查是否收藏了")
    @GetMapping(value = "checkIsCollect/{postId}")
    public Result checkIsCollect(@PathVariable Integer postId, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        boolean result = collectService.checkIsCollect(uid,postId);
        return Result.ok(result);
    }
}
