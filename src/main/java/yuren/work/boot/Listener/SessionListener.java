package yuren.work.boot.Listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
@WebListener
public class SessionListener implements HttpSessionListener {

    /**
     * 记录在线的用户数量
     */
    public Integer count = 0;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public synchronized void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log.info("新用户上线了");
        count++;
        redisTemplate.opsForValue().set("onlineCount",count.toString());
    }

    @Override
    public synchronized void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        log.info("用户下线了");
        count--;
        redisTemplate.opsForValue().set("onlineCount",count.toString());
    }

}
