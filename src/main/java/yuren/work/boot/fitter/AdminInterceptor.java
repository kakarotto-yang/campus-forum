package yuren.work.boot.fitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import yuren.work.boot.common.exception.CFException;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.ResultCodeEnum;
import yuren.work.boot.common.utils.BrowsingRecordUtil;
import yuren.work.boot.pojo.BrowsingRecord;
import yuren.work.boot.service.AuthorityService;
import yuren.work.boot.service.BrowsingRecordService;
import yuren.work.boot.service.AdminService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AdminInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate = new StringRedisTemplate();
    @Autowired
    BrowsingRecordService browsingRecordService;
    @Autowired
    AuthorityService authorityService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object object) throws Exception {
        String reqUrl = request.getRequestURI();
        BrowsingRecord browsingRecord = BrowsingRecordUtil.getBrowsingRecord(request);
        log.info("拦截" + reqUrl);
        System.out.println(request.getHeader("Access-Control-Request-Headers"));
        String reqToken = request.getHeader("token");
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            System.out.println("OPTIONS请求，放行");
            return true;
        }

        if (StringUtils.isEmpty(reqToken)){
            browsingRecord.setReqResult("error");
            browsingRecordService.insertRecord(browsingRecord);
            throw new CFException(ResultCodeEnum.LOGIN_AUTH);
        }
        String redToken = redisTemplate.opsForValue().get(reqToken);
        //如果redis中没有该token或者token已经过期，抛异常
        if(redToken==null||!(redisTemplate.getExpire(redToken)>0)){
            browsingRecord.setReqResult("error");
            browsingRecordService.insertRecord(browsingRecord);
            throw new CFException(ResultCodeEnum.LOGIN_AUTH);
        }

        Integer uid = JwtHelper.getUserId(redToken);
        //如果不是管理员
        if (!authorityService.isAdmin(uid)){
            throw new CFException(ResultCodeEnum.LOGIN_ACL);
        }
        //如果token过期时间低于8小时，重新设置token过期时间
        if(redisTemplate.getExpire(redToken, TimeUnit.HOURS)<8){
            browsingRecord.setReqResult("error");
            browsingRecordService.insertRecord(browsingRecord);
            redisTemplate.expire(redToken,1, TimeUnit.DAYS);
        }

        browsingRecord.setReqResult("ok");
        browsingRecordService.insertRecord(browsingRecord);
        return true;

    }
}
