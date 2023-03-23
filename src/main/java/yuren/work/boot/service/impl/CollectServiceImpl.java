package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuren.work.boot.mapper.CollectMapper;
import yuren.work.boot.pojo.Collect;
import yuren.work.boot.pojo.Post;
import yuren.work.boot.pojo.Praise;
import yuren.work.boot.service.CollectService;
import yuren.work.boot.service.PostService;


import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CollectServiceImpl extends ServiceImpl<CollectMapper,Collect> implements CollectService{
    @Autowired
    PostService postService;
    @Override
    public void collect(Collect collect) {
        Integer uid = collect.getUid();
        Integer postId = collect.getPostId();
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        queryWrapper.eq("post_id",postId);
        Collect collect1 = baseMapper.selectOne(queryWrapper);
        //如果数据库中没有该用户对该帖子的收藏数据，insert
        //如果存在该收藏数据，就将收藏状态取反，状态：（0：正常 1：取消收藏）
        Post post = postService.getPostById(postId);
        if (collect1==null){
            collect.setStatus(1);
            collect.setCreateDate(new Date());
            post.setCollections(post.getCollections()+1);
            postService.editPost(post);
            baseMapper.insert(collect);
        }else {
            collect1.setUpdateDate(new Date());
            if (collect1.getStatus()==1){
                post.setCollections(post.getCollections()-1);
                postService.editPost(post);
                collect1.setStatus(0);
                baseMapper.updateById(collect1);
            }else {
                post.setCollections(post.getCollections()+1);
                postService.editPost(post);
                collect1.setStatus(1);
                baseMapper.updateById(collect1);
            }
        }
    }

    @Override
    public List<Collect> getCollectionByUid(Integer id) {
        QueryWrapper<Collect> collectQW= new QueryWrapper<>();
        collectQW.eq("uid",id);
        collectQW.eq("status",1);
        List<Collect> collects = baseMapper.selectList(collectQW);
        return collects;
    }

    @Override
    public boolean checkIsCollect(Integer uid, Integer postId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("post_id", postId);
        queryWrapper.eq("status", 1);
        Collect collect = baseMapper.selectOne(queryWrapper);
        if (collect != null){
            return true;
        }
        return false;
    }
}
