package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import yuren.work.boot.common.exception.CFException;
import yuren.work.boot.common.helper.JwtHelper;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.common.result.ResultCodeEnum;
import yuren.work.boot.mapper.UserInfoMapper;
import yuren.work.boot.pojo.User;
import yuren.work.boot.pojo.UserLogin;
import yuren.work.boot.service.AuthorityService;
import yuren.work.boot.service.InfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class InfoServiceImpl extends ServiceImpl<UserInfoMapper, User> implements InfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    AuthorityService authorityService;

    @Override
    public Map<String, Object> loginEmail(UserLogin userLogin) {
        String email = userLogin.getEmail();
        String code = userLogin.getCode();
        //判断邮箱和验证码是否为空
        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(code)){
            throw new CFException(ResultCodeEnum.PARAM_ERROR);
        }
        log.info("=========验证码剩余时间："+redisTemplate.getExpire(email,TimeUnit.SECONDS).toString()+"===========");

        //判断redis验证码和输入的验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(email);
        if(StringUtils.isEmpty(redisCode)){
            throw new CFException(ResultCodeEnum.CODE_Expire);
        }
        if (!redisCode.equals(code)){
            throw new CFException(ResultCodeEnum.CODE_ERROR);
        }

        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("email",email);
        User user = baseMapper.selectOne(queryWrapper);
        if(user == null) { //第一次使用这个手机号登录
            //添加信息到数据库
            user = new User();
            user.setName("");
            user.setHeadImg(ranDomImg());
            user.setEmail(email);
            user.setNickName(email);
            user.setStatus(0);
            user.setCreateDate(new Date());
            baseMapper.insert(user);
        }
        //校验是否被禁用
        if(user.getStatus() == 1) {
            throw new CFException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //不是第一次，直接登录
        //返回登录信息
        //返回登录用户名
        //返回token信息
        Map<String, Object> map = new HashMap<>();
//        String name = user.getName();
//        if(StringUtils.isEmpty(name)) {
//            name = user.getNickName();
//        }
//        if(StringUtils.isEmpty(name)) {
//            name = user.getEmail();
//        }
        map.put("user",user);
        //jwt生成token字符串

        String token = JwtHelper.createToken(user.getId(), user.getNickName());
        //将token信息存到redis
        redisTemplate.opsForValue().set(token,token,1, TimeUnit.DAYS);
        map.put("token",token);
        return map;
    }

    @Override
    public Object adminLogin(UserLogin userLogin) {
        String email = userLogin.getEmail();
        String code = userLogin.getCode();
        //判断邮箱和验证码是否为空
        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(code)){
            throw new CFException(ResultCodeEnum.PARAM_ERROR);
        }
        log.info("=========验证码剩余时间："+redisTemplate.getExpire(email,TimeUnit.SECONDS).toString()+"===========");

        //判断redis验证码和输入的验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(email);
        if(StringUtils.isEmpty(redisCode)){
            throw new CFException(ResultCodeEnum.CODE_Expire);
        }
        if (!redisCode.equals(code)){
            throw new CFException(ResultCodeEnum.CODE_ERROR);
        }

        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("email",email);
        User user = baseMapper.selectOne(queryWrapper);
        if(user == null) { //第一次使用这个手机号登录
            throw new CFException(ResultCodeEnum.LOGIN_ACL);
        }
        //校验是否是管理员
        Integer uid = user.getId();
        if(!authorityService.isAdmin(uid)){
            throw new CFException(ResultCodeEnum.LOGIN_ACL);
        }
        //校验是否被禁用
        if(user.getStatus() == 1) {
            throw new CFException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        Map<String, Object> map = new HashMap<>();
        String name = user.getName();
        if(StringUtils.isEmpty(name)) {
            name = user.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = user.getEmail();
        }
        map.put("name",name);
        //jwt生成token字符串

        String token = JwtHelper.createToken(user.getId(), user.getNickName());
        //将token信息存到redis
        redisTemplate.opsForValue().set(token,token,1, TimeUnit.DAYS);
        map.put("token",token);
        return map;
    }

    public String ranDomImg(){
        String photoPath ="D:\\WorkSpace_Extra\\javaweb\\CampusForum\\public\\romdomAva";
        File file = new File(photoPath);
        if(!file.exists()){
            return "";
        }
        File[] files = file.listFiles();
        Random random = new Random();
        int index = random.nextInt(files.length+1);
        return "http://localhost:8080/romdomAva/"+files[index].getName();
    }
}
