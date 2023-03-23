package yuren.work.boot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yuren.work.boot.common.exception.CFException;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.common.result.ResultCodeEnum;
import yuren.work.boot.pojo.Post;
import yuren.work.boot.pojo.User;
import yuren.work.boot.service.PostService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "auth/post")
@Slf4j
@Api(tags = "帖子管理模块")
@CrossOrigin
public class PostController {

    @Autowired
    PostService postService;

    @ApiOperation(value = "添加post")
    @PostMapping(value = "addPost")
    public Result addPost(@RequestBody Post post, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        post.setUid(uid);
        post.setCreateDate(new Date());
        postService.addPost(post);
        return Result.ok();
    }

    @ApiOperation(value = "编辑修改post")
    @PostMapping(value = "editPost/{id}")
    public Result editPost(@PathVariable Integer id,@RequestBody Post post, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        //检查该post是否属于该用户
        if (postService.checkPostBelong(uid,id)){
            post.setId(id);
            postService.editPost(post);
        }else {
            throw new CFException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        return Result.ok();
    }

    @ApiOperation(value = "逻辑删除post")
    @GetMapping(value = "delPost/{id}")
    public Result delPost(@PathVariable Integer id, HttpServletRequest request ){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        //检查该post是否属于该用户
        if (postService.checkPostBelong(uid,id)){
            postService.delPost(id);
        }else {
            throw new CFException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        return Result.ok();
    }

    @RequestMapping(value = "/uploadImg")
    @ResponseBody
    public Result uploadImg(@RequestParam(value = "file", required = false) MultipartFile photo,HttpServletRequest request) throws IOException {
        String fileName = photo.getOriginalFilename();
        //处理文件重名问题
        String qzName = fileName.substring(0,fileName.lastIndexOf("."));
        fileName = UUID.randomUUID().toString() + fileName;
        //获取服务器中photo目录的路径
        String token = request.getHeader("token");
        String userName = JwtHelper.getUserName(token);
        String photoPath ="D:\\WorkSpace_Extra\\javaweb\\CampusForum\\public\\photo\\"+userName;
        File file = new File(photoPath);
        if(!file.exists()){
            file.mkdir();
        }
        String finalPath = photoPath + File.separator + fileName;
        //实现上传功能
        photo.transferTo(new File(finalPath));
        Map<String,String> json=new HashMap<String,String>();
        json.put("success","1");
        json.put("desc",qzName);
        json.put("url","/photo/"+userName+'/'+fileName);
        return Result.ok(json);
    }

}
