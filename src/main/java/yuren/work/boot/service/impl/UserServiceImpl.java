package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuren.work.boot.common.exception.CFException;
import yuren.work.boot.common.result.Result;
import yuren.work.boot.common.result.ResultCodeEnum;
import yuren.work.boot.mapper.UserMapper;
import yuren.work.boot.pojo.*;
import yuren.work.boot.service.*;

import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    CollectService collectService;
    @Autowired
    PostService postService;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    ReplyService replyService;

    @Override
    public User getUserById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void delUser(Integer id) {
        //删除掉用户所有连带痕迹
//        List<Post> posts = postService.getPostByUserId(id);
//        List<Comment> commentList = commentService.getCommentByUserId(id);
//        List<Reply> replyList = commentService.getReplyByUserId(id);
//        //删除掉用户发的帖子
//        for (Post post : posts) {
//            postService.delPost(post.getId());
//        }
        baseMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getUserIndexInfo(String nickName) {
        Map<String,Object> result = new HashMap<>();
        QueryWrapper<User> userQW= new QueryWrapper<>();
        userQW.eq("nick_name",nickName);
        //用户信息
        User user = baseMapper.selectOne(userQW);
        if (user == null){
            throw new CFException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        //用户的文章
        List<Post> posts = postService.getPostByUserId(user.getId());
        //查找该用户收藏信息
        List<Collect> collects = collectService.getCollectionByUid(user.getId());
        Collection<Integer> postIds = new ArrayList<>();
        for (Collect collect:collects) {
            postIds.add(collect.getPostId());
        }
        List<Post> collect = postService.selectPostByBatchIds(postIds);
        //查找该用户关注信息
        //关注列表信息
        List<Follow> follows = followService.getFollowByUid(user.getId());
        //关注人id
        Collection<Integer> followIds = new ArrayList<>();
        for (Follow follow:follows) {
            followIds.add(follow.getFid());
        }
        //关注人列表
        List<User> followers = new ArrayList<>();
        if (followIds.size() != 0){
            followers = baseMapper.selectBatchIds(followIds);
        }

        //查找该用户粉丝信息
        //粉丝列表信息
        List<Follow> fans = followService.getFollowByFid(user.getId());
        //粉丝id
        Collection<Integer> fanIds = new ArrayList<>();
        for (Follow fan:fans) {
            fanIds.add(fan.getUid());
        }
        //粉丝列表
        List<User> fan = new ArrayList<>();
        if (fanIds.size() != 0){
            fan = baseMapper.selectBatchIds(fanIds);
        }
        result.put("user",user);
        result.put("post",posts);
        result.put("fans",fan);
        result.put("collect",collect);
        result.put("followers",followers);
        return result;
    }

    @Override
    public void updateUser(User user) {
        baseMapper.updateById(user);
    }

    @Override
    public Boolean checkNickName(String nickName) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nick_name",nickName);
        User user = baseMapper.selectOne(queryWrapper);
        if (user != null){
            return true;
        }
        return false;
    }
}
