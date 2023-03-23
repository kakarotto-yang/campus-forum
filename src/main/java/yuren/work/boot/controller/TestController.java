package yuren.work.boot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import yuren.work.boot.mapper.UserInfoMapper;
import yuren.work.boot.pojo.User;
import yuren.work.boot.common.result.Result;

@RestController
@RequestMapping("/test")
@Api(tags = "测试模块")
@CrossOrigin
public class TestController {

    private final static Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserInfoMapper userInfoMapper;
    @GetMapping("/mail")
    public Result mail(){
        //在这里调用业务层代码
        //发送邮箱
        //内容你们可以用一些随机数,毫秒值做为内容,以便达到验证码的效果
        Page<User> page = new Page<>(1,3);
        System.out.println(page);
        System.out.println(page.hasNext());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
        userInfoMapper.selectPage(page, null);
        System.out.println(page);
        System.out.println(page.hasNext());
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
        return Result.ok();
    }

    @GetMapping("onlineCount")
    public Result online(){
        String onlineCount = redisTemplate.opsForValue().get("onlineCount");
        return Result.ok(onlineCount);
    }
}
