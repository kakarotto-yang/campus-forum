package yuren.work.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.pojo.Praise;
import yuren.work.boot.service.PraiseService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/auth/user/praise")
@Api(tags = "点赞模块")
@Slf4j
@CrossOrigin
public class PraiseContreller {
    @Autowired
    PraiseService praiseService;

    @ApiOperation(value = "用户点赞接口")
    @GetMapping(value = "{postId}")
    public Result praise(@PathVariable Integer postId, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        Praise praise = new Praise();
        praise.setUid(uid);
        praise.setPostId(postId);
        praiseService.praise(praise);
        return Result.ok();
    }

    @ApiOperation(value = "根据帖子id检查是否点赞")
    @GetMapping(value = "checkIsPraise/{postId}")
    public Result checkIsPraise(@PathVariable Integer postId, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        boolean result = praiseService.checkIsPraise(uid,postId);
        return Result.ok(result);
    }
}
