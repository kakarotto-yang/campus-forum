package yuren.work.boot.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.common.utils.RandomUtil;
import yuren.work.boot.service.EmailCodeService;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping(value = "/api/email")
@Api(tags = "邮箱验证码模块")
@CrossOrigin
public class EmailCodeController {
    @Autowired
    EmailCodeService emailCodeService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @ApiOperation("发送邮箱验证码")
    @PostMapping("send")
    public Result sendCode(@RequestParam(required = true) String email){
        String code = RandomUtil.getSixBitRandom();
        boolean isSend = emailCodeService.sendCode(email,code);
        //生成验证码放到redis里面，设置有效时间
        if(isSend) {
            redisTemplate.opsForValue().set(email,code,30, TimeUnit.DAYS);
            log.info(redisTemplate.opsForValue().get(email));
            return Result.ok();
        } else {
            return Result.fail().message("发送短信失败");
        }
    }
}
