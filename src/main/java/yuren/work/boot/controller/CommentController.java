package yuren.work.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.pojo.Comment;
import yuren.work.boot.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/auth/user/comment")
@Slf4j
@Api(tags = "回复模块")
@CrossOrigin
public class CommentController {
    @Autowired
    CommentService commentService;

    @ApiOperation(value = "添加评论")
    @PostMapping(value = "addComment")
    public Result addComment(@RequestBody Comment comment, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        comment.setFromUid(uid);
        commentService.addComment(comment);
        return Result.ok();
    }

    @ApiOperation(value = "删除评论")
    @PostMapping(value = "delComment/{id}")
    public Result delComment(@PathVariable Integer id){
        commentService.delComment(id);
        return Result.ok();
    }

}
