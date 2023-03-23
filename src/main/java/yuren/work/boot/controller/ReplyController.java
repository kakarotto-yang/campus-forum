package yuren.work.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.pojo.Reply;
import yuren.work.boot.service.ReplyService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/auth/user/reply")
@Slf4j
@Api(tags = "评论模块")
@CrossOrigin
public class ReplyController {
    @Autowired
    ReplyService replyService;

    @ApiOperation(value = "根据commentId获取")
    @GetMapping(value = "getReply/{commentId}")
    public Result getReplyList(@PathVariable Integer commentId){
        List<Reply> replyList = replyService.getReplyList(commentId);
        return Result.ok(replyList);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping(value = "addReply")
    public Result addReply(@RequestBody Reply reply, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        reply.setFromUid(uid);
        replyService.addReply(reply);
        return Result.ok();
    }

    @ApiOperation(value = "删除评论")
    @PostMapping(value = "delReply/{id}")
    public Result delReply(@PathVariable Integer id){
        replyService.delReply(id);
        return Result.ok();
    }

}
