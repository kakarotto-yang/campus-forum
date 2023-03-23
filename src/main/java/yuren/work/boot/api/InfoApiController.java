package yuren.work.boot.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.pojo.Comment;
import yuren.work.boot.pojo.Post;
import yuren.work.boot.pojo.UserLogin;
import yuren.work.boot.query.PostQueryVo;
import yuren.work.boot.service.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@RestController
@RequestMapping(value = "/api")
@Api(tags = "基础信息调用模块")
@CrossOrigin
public class InfoApiController {

    @Autowired
    InfoService infoService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @ApiOperation("邮箱验证码登录接口")
    @PostMapping(value = "login")
    public Result loginEmail(@ApiParam(value = "邮箱账号") @RequestParam(required = true) String email,@ApiParam(value = "验证码") @RequestParam(required = true) String code){
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(email);
        userLogin.setCode(code);
        return Result.ok(infoService.loginEmail(userLogin));
    }

    @ApiOperation("后台邮箱验证码登录接口")
    @PostMapping(value = "adminLogin")
    public Result adminLogin(@ApiParam(value = "邮箱账号") @RequestParam(required = true) String email,@ApiParam(value = "验证码") @RequestParam(required = true) String code){
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(email);
        userLogin.setCode(code);
        return Result.ok(infoService.adminLogin(userLogin));
    }

    @ApiOperation("根据token退出登录")
    @PostMapping(value = "logout")
    public Result loginOut(HttpServletRequest request){
        String token = request.getHeader("token");
        redisTemplate.delete(token);
        return Result.ok();
    }

    /*
     *后期还得将从数据库中请求的数据放到redis缓存以减轻数据库压力
     */
    //按查询条件和页数和每页条数查询post
              @ApiOperation(value = "根据查询条件获取post列表分页数据")
    @PostMapping(value = "getPostPage/{page}/{limit}")
    public Result getPostPage(@PathVariable Integer page, @PathVariable Integer limit, @RequestBody(required = false) PostQueryVo postQueryVo,HttpServletRequest request){
                  String token = request.getHeader("token");
                  Integer uid = JwtHelper.getUserId(token);
                  Map<String,Object> map = postService.selectPostPage(uid,page,limit,postQueryVo);
        return Result.ok(map);
    }

    @ApiOperation(value = "根据id返回post")
    @GetMapping(value = "getPost/{id}")
    public Result getPost(@PathVariable Integer id){
        Post post = postService.getPostById(id);
        post.setViews(post.getViews()+1);
        postService.editPost(post);
        return Result.ok(post);
    }

    @ApiOperation(value = "根据id返回post")
    @GetMapping(value = "romdomImg")
    public Result romdomImg(){
        String photoPath ="D:\\WorkSpace_Extra\\javaweb\\CampusForum\\public\\romdomAva";
        File file = new File(photoPath);
        if(!file.exists()){
            return Result.fail();
        }
        File[] files = file.listFiles();
        Random random = new Random();
        int index = random.nextInt(files.length+1);
        Map<String,String> map = new HashMap<>();
        map.put("imgUrl","http://localhost:8080/romdomAva/"+files[index].getName());
        return Result.ok(map);
    }

    @ApiOperation("根据昵称获取用户主页信息")
    @GetMapping(value = "getUserIndexInfo")
    public Result getUserIndexInfo(String nickName){
        Map<String,Object> map = userService.getUserIndexInfo(nickName);
        return Result.ok(map);
    }

    @ApiOperation(value = "根据用户id检查是否有关注该用户")
    @GetMapping(value = "checkIsFollow/{fid}")
    public Result checkIsFollow(@PathVariable Integer fid, HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        boolean result = followService.checkIsFollow(uid,fid);
        return Result.ok(result);
    }

    @ApiOperation("判断post是否属于登录用户")
    @GetMapping(value = "checkPostBelong")
    public Result checkPostBelong(Integer postId,HttpServletRequest request){
        String token = request.getHeader("token");
        Integer uid = JwtHelper.getUserId(token);
        return Result.ok(postService.checkPostBelong(uid,postId));
    }


    //获取评论
    @ApiOperation(value = "根据topicId获取")
    @GetMapping(value = "getComment/{topicId}")
    public Result getComment(@PathVariable Integer topicId){
        List<Comment> commentList = commentService.getComment(topicId);
        return Result.ok(commentList);
    }

    //搜索
    @ApiOperation(value = "根据关键词查询")
    @RequestMapping(value = "search/{keyword}")
    @GetMapping
    public Result search(@PathVariable String keyword) {
        List<Post> postList = postService.selectAllPost();
        Map<String,String> results = new HashMap<String, String>();
        for (Post post:postList) {
            int titleIndex =post.getTitle().indexOf(keyword);
            int contentIndex =post.getContent().indexOf(keyword);
            if((titleIndex!=-1)||(contentIndex!=-1)){
                results.put("value",post.getTitle());
                results.put("id",post.getId().toString());
            }
        }
        return Result.ok(results);
    }
}