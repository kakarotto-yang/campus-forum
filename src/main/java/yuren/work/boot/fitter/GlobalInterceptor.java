package yuren.work.boot.fitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import yuren.work.boot.common.exception.CFException;
import yuren.work.boot.common.result.ResultCodeEnum;
import yuren.work.boot.common.utils.BrowsingRecordUtil;
import yuren.work.boot.pojo.BrowsingRecord;
import yuren.work.boot.service.BrowsingRecordService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate = new StringRedisTemplate();
    @Autowired
    BrowsingRecordService browsingRecordService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object object) throws Exception {
        String reqUrl = request.getRequestURI();
        BrowsingRecord browsingRecord = BrowsingRecordUtil.getBrowsingRecord(request);
        log.info("拦截" + reqUrl);

        browsingRecord.setReqResult("ok");
        browsingRecordService.insertRecord(browsingRecord);
        return true;

    }
}
