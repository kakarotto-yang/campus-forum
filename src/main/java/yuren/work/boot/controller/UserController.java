package yuren.work.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.pojo.User;
import yuren.work.boot.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/auth/user")
@CrossOrigin
@Api(tags = "用户模块")
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation("根据id获取用户信息")
    @GetMapping(value = "getUserById/{id}")
    public Result getUserById(@PathVariable Integer id){
        User user = userService.getUserById(id);
        return Result.ok(user);
    }

    @ApiOperation("根据id获取用户信息")
    @GetMapping(value = "getInfo")
    public Result getInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        Integer id = JwtHelper.getUserId(token);
        User user = userService.getUserById(id);
        return Result.ok(user);
    }


    @ApiOperation("检查数据库中是否存在该昵称")
    @GetMapping(value = "checkNickName")
    public Result checkNickName(String nickName,HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        User user = userService.getUserById(uid);
        //如果用户没改动昵称，则直接返回false
        if (user.getNickName().equals(nickName)){
            return Result.ok(false);
        }
        //如果改动了昵称，且数据库中存在该昵称，返回true
        if(userService.checkNickName(nickName)){
            return Result.ok(true);
        }
        return Result.ok(false);
    }

    @ApiOperation("更新用户信息")
    @PostMapping(value = "updateUser")
    public Result updateUser(@RequestBody User user, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        user.setId(uid);
        userService.updateUser(user);
        return Result.ok();
    }

    @RequestMapping(value = "/uploadAvatar")
    @ResponseBody
    public Result uploadAvatar(@RequestParam("avatarPic") MultipartFile photo, HttpServletRequest request) throws IOException {
        String fileName = photo.getOriginalFilename();
        //处理文件重名问题
        String qzName = fileName.substring(0,fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString() + fileName;
        //获取服务器中photo目录的路径
        String token = request.getHeader("token");
        String userName = JwtHelper.getUserName(token);
        Integer uid = JwtHelper.getUserId(token);
        String photoPath ="D:\\WorkSpace_Extra\\javaweb\\CampusForum\\public\\photo\\"+userName+"_ava";
        File file = new File(photoPath);
        if(!file.exists()){
            file.mkdir();
        }
        String finalPath = photoPath + File.separator + fileName;
        //实现上传功能
        photo.transferTo(new File(finalPath));
        //更新头像地址
        User user = userService.getUserById(uid);
        user.setHeadImg("http://localhost:8080/photo/"+userName+"_ava/"+fileName);
        userService.updateUser(user);
        return Result.ok();
    }

}
