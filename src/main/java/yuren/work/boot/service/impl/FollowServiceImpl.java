package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuren.work.boot.mapper.FollowMapper;
import yuren.work.boot.mapper.UserInfoMapper;
import yuren.work.boot.pojo.Collect;
import yuren.work.boot.pojo.Follow;
import yuren.work.boot.pojo.User;
import yuren.work.boot.service.FollowService;
import yuren.work.boot.service.UserService;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class FollowServiceImpl extends ServiceImpl<FollowMapper,Follow> implements FollowService {

    @Autowired
    UserService userService;
    @Override
    public void follow(Follow follow) {
        Integer uid = follow.getUid();
        Integer fid = follow.getFid();
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        queryWrapper.eq("fid",fid);
        Follow follow1 = baseMapper.selectOne(queryWrapper);
        //如果数据库中没有该用户对该帖子的收藏数据，insert
        //如果存在该收藏数据，就将收藏状态取反，状态：（0：正常 1：取消收藏）
        User uUser= userService.getUserById(uid);
        User fUser= userService.getUserById(fid);
        if (follow1==null){
            follow.setStatus(1);
            follow.setCreateDate(new Date());
            uUser.setFollows(uUser.getFollows()+1);
            fUser.setFans(fUser.getFans()+1);
            userService.updateUser(uUser);
            userService.updateUser(fUser);
            baseMapper.insert(follow);
        }else {
            follow1.setUpdateDate(new Date());
            if (follow1.getStatus()==1){
                uUser.setFollows(uUser.getFollows()-1);
                fUser.setFans(fUser.getFans()-1);
                userService.updateUser(uUser);
                userService.updateUser(fUser);
                follow1.setStatus(0);
                baseMapper.updateById(follow1);
            }else {
                uUser.setFollows(uUser.getFollows()+1);
                fUser.setFans(fUser.getFans()+1);
                userService.updateUser(uUser);
                userService.updateUser(fUser);
                follow1.setStatus(1);
                baseMapper.updateById(follow1);
            }
        }
    }

    @Override
    public List<Follow> getFollowByUid(Integer id) {
        QueryWrapper<Follow> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("uid",id);
        queryWrapper.eq("status",1);
        List<Follow> follows = baseMapper.selectList(queryWrapper);
        return follows;
    }

    @Override
    public List<Follow> getFollowByFid(Integer id) {
        QueryWrapper<Follow> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("fid",id);
        queryWrapper.eq("status",1);
        List<Follow> fans = baseMapper.selectList(queryWrapper);
        return fans;
    }

    @Override
    public boolean checkIsFollow(Integer uid, Integer fid) {
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("fid", fid);
        queryWrapper.eq("status",1);
        Follow follow = baseMapper.selectOne(queryWrapper);
        if (follow != null){
            return true;
        }
        return false;
    }

}
