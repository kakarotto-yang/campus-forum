package yuren.work.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.pojo.Post;
import yuren.work.boot.pojo.User;
import yuren.work.boot.query.PostQueryVo;
import yuren.work.boot.query.UserQueryVo;
import yuren.work.boot.service.PostService;
import yuren.work.boot.service.AdminService;
import yuren.work.boot.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin")
@Slf4j
@Api(tags = "后台用户管理模块")
@CrossOrigin
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @ApiOperation(value = "根据查询条件获取用户列表分页数据")
    @GetMapping(value = "getUserPage/{page}/{limit}")
    public Result getUserPage(@PathVariable Integer page, @PathVariable Integer limit, UserQueryVo userQueryVo){
        Map<String,Object> map = adminService.selectUserPage(page,limit,userQueryVo);
        return Result.ok(map);
    }

    @ApiOperation("根据id获取用户信息")
    @GetMapping(value = "getUserById/{id}")
    public Result getUserById(@PathVariable Integer id){
        User user = adminService.getUserById(id);
        return Result.ok(user);
    }


    @ApiOperation("修改用户信息")
    @PostMapping(value = "editUser")
    public Result editUser(@RequestBody User user){
        adminService.editUser(user);
        return Result.ok();
    }

    @ApiOperation(value = "根据id改变用户状态")
    @GetMapping(value = "lockUser/{id}/{status}")
    public Result updateUserStatus(@PathVariable Integer id,@PathVariable Integer status){
        adminService.updateStatus(id,status);
        return Result.ok();
    }

    @ApiOperation(value = "根据id删除单个用户")
    @PostMapping(value = "delUser/{id}")
    public Result delUser(@PathVariable Integer id){
        userService.delUser(id);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户,传入数组批量删除")
    @PostMapping(value = "delUserRows")
    public Result delUserRows(@RequestBody List<Integer> idList){
        adminService.delUser(idList);
        return Result.ok();
    }


    /* =====post==== */
    @ApiOperation(value = "根据查询条件获取用户列表分页数据")
    @GetMapping(value = "getPostPage/{page}/{limit}")
    public Result getPostPage(@PathVariable Integer page, @PathVariable Integer limit, PostQueryVo postQueryVo){
        Map<String,Object> map = postService.selectPostPage(page, limit, postQueryVo);
        return Result.ok(map);
    }


    @ApiOperation("根据id获取用户信息")
    @GetMapping(value = "getPostById/{id}")
    public Result getPostById(@PathVariable Integer id){
        Post post = postService.getPostById(id);
        return Result.ok(post);
    }

    @ApiOperation(value = "根据id删除单个Post")
    @PostMapping(value = "delPost/{id}")
    public Result delPost(@PathVariable Integer id){
        postService.delPost(id);
        return Result.ok();
    }

    @ApiOperation(value = "删除Post,传入id数组批量删除")
    @PostMapping(value = "delPostRows")
    public Result delPostRows(@RequestBody List<Integer> idList){
        postService.delPost(idList);
        return Result.ok();
    }

    @ApiOperation(value = "根据id改变帖子状态")
    @GetMapping(value = "lockPost/{id}/{status}")
    public Result updatePostStatus(@PathVariable Integer id,@PathVariable Integer status){
        postService.updateStatus(id,status);
        return Result.ok();
    }

    @ApiOperation("修改帖子信息")
    @PostMapping(value = "editPost")
    public Result editPost(@RequestBody Post post){
        postService.editPost(post);
        return Result.ok();
    }
}
