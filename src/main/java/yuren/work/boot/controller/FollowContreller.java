package yuren.work.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.pojo.Follow;
import yuren.work.boot.service.FollowService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/auth/user/follow")
@Api(tags = "关注模块")
@Slf4j
@CrossOrigin
public class FollowContreller {
    @Autowired
    FollowService followService;

    @ApiOperation(value = "用户关注接口")
    @GetMapping(value = "{fid}")
    public Result follow(@PathVariable Integer fid, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        Follow follow = new Follow();
        follow.setUid(uid);
        follow.setFid(fid);
        followService.follow(follow);
        return Result.ok();
    }

}
