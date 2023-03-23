package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuren.work.boot.mapper.PraiseMapper;
import yuren.work.boot.pojo.Follow;
import yuren.work.boot.pojo.Post;
import yuren.work.boot.pojo.Praise;
import yuren.work.boot.service.PostService;
import yuren.work.boot.service.PraiseService;

import java.util.Date;

@Service
@Transactional
public class PraiseServiceImpl extends ServiceImpl<PraiseMapper, Praise> implements PraiseService {

    @Autowired
    PostService postService;
    @Override
    public void praise(Praise praise) {
        Integer uid = praise.getUid();
        Integer postId = praise.getPostId();
        QueryWrapper<Praise> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        queryWrapper.eq("post_id",postId);
        Praise praise1 = baseMapper.selectOne(queryWrapper);
        //如果数据库中没有该用户对该帖子的点赞数据，insert
        //如果存在该点赞数据，就将点赞状态取反，状态：（0：正常 1：取消点赞）
        Post post = postService.getPostById(postId);
        if (praise1==null){
            praise.setStatus(1);
            praise.setCreateDate(new Date());
            post.setPraises(post.getPraises()+1);
            postService.editPost(post);
            baseMapper.insert(praise);
        }else {
            praise1.setUpdateDate(new Date());
            if (praise1.getStatus()==1){
                post.setPraises(post.getPraises()-1);
                postService.editPost(post);
                praise1.setStatus(0);
                baseMapper.updateById(praise1);
            }else {
                post.setPraises(post.getPraises()+1);
                postService.editPost(post);
                praise1.setStatus(1);
                baseMapper.updateById(praise1);
            }
        }
    }

    @Override
    public boolean checkIsPraise(Integer uid,Integer postId) {
        QueryWrapper<Praise> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("status", 1);
        Praise praise = baseMapper.selectOne(queryWrapper);
        if (praise != null){
            return true;
        }
        return false;
    }
}
