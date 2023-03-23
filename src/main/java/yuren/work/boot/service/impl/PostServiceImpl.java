package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import yuren.work.boot.common.exception.CFException;
import yuren.work.boot.common.result.ResultCodeEnum;
import yuren.work.boot.mapper.PostMapper;
import yuren.work.boot.pojo.Post;
import yuren.work.boot.pojo.User;
import yuren.work.boot.query.PostQueryVo;
import yuren.work.boot.service.*;

import java.util.*;

@Service
@Transactional
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Autowired
    PraiseService praiseService;
    @Autowired
    CollectService collectService;
    @Autowired
    UserService userService;
    @Autowired
    AuthorityService authorityService;
    @Override
    public Map<String, Object> selectPostPage(Integer uid,Integer page, Integer pageSize, PostQueryVo postQueryVo) {
        Page<Post> page1 = new Page<>(page,pageSize);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        //判断查询条件是否非空
        if(postQueryVo!=null){
            String title = postQueryVo.getTitle();
            String postType = postQueryVo.getPostType();
            String content = postQueryVo.getContent();
            String createTimeBegin = postQueryVo.getCreateTimeBegin();
            String createTimeEnd = postQueryVo.getCreateTimeEnd();
            if(!StringUtils.isEmpty(title)){
                queryWrapper.like("title",title);
            }
            if(!StringUtils.isEmpty(postType)){
                queryWrapper.eq("post_type",postType);
            }
            if(!StringUtils.isEmpty(content)){
                queryWrapper.like("content",content);
            }
            if(!StringUtils.isEmpty(createTimeBegin)){
                queryWrapper.ge("create_date",createTimeBegin);
            }
            if(!StringUtils.isEmpty(createTimeEnd)){
                queryWrapper.le("create_date",createTimeBegin);
            }
        }
        //如果不是管理员，只能请求审核过的帖子

        queryWrapper.eq("status",1);

        baseMapper.selectPage(page1,queryWrapper);
        Map<String,Object> map = new HashMap<>();
        if (uid != null){
            //是否点赞和收藏
            Map<String,Boolean> isPraise = new HashMap<>();
            Map<String,Boolean> isCollect= new HashMap<>();
            for (Post post : page1.getRecords()){
                isPraise.put(post.getId().toString(),praiseService.checkIsPraise(uid,post.getId()));
                isCollect.put(post.getId().toString(),collectService.checkIsCollect(uid,post.getId()));
            }
            map.put("isPraise",isPraise);
            map.put("isCollect",isCollect);
        }
        List<Post> posts = new ArrayList<>();
        for (Post post : page1.getRecords()){
            post.setAuthor(userService.getUserById(post.getUid()));
            posts.add(post);
        }
        map.put("posts",posts);
        return map;
    }
    @Override
    public Map<String, Object> selectPostPage(Integer page, Integer pageSize, PostQueryVo postQueryVo) {
        Page<Post> page1 = new Page<>(page,pageSize);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        //判断查询条件是否非空
        if(postQueryVo!=null){
            String title = postQueryVo.getTitle();
            String postType = postQueryVo.getPostType();
            String content = postQueryVo.getContent();
            String createTimeBegin = postQueryVo.getCreateTimeBegin();
            String createTimeEnd = postQueryVo.getCreateTimeEnd();
            if(!StringUtils.isEmpty(title)){
                queryWrapper.like("title",title);
            }
            if(!StringUtils.isEmpty(postType)){
                queryWrapper.eq("post_type",postType);
            }
            if(!StringUtils.isEmpty(content)){
                queryWrapper.like("content",content);
            }
            if(!StringUtils.isEmpty(createTimeBegin)){
                queryWrapper.ge("create_date",createTimeBegin);
            }
            if(!StringUtils.isEmpty(createTimeEnd)){
                queryWrapper.le("create_date",createTimeBegin);
            }
        }
        baseMapper.selectPage(page1,queryWrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("pages",page1.getPages());
        map.put("total",page1.getTotal());
        map.put("currentSize",page1.getRecords().size());
        map.put("posts",page1.getRecords());
        return map;
    }

    @Override
    public Post getPostById(Integer id) {
        Post post = baseMapper.selectById(id);
        post.setAuthor(userService.getUserById(post.getUid()));
        return post;
    }

    @Override
    public int addPost(Post post) {
        User user = userService.getUserById(post.getUid());
        user.setPosts(user.getPosts()+1);
        userService.updateUser(user);
        return baseMapper.insert(post);
    }

    @Override
    public void editPost(Post post) {
        baseMapper.updateById(post);
    }

    @Override
    public void delPost(Integer id) {
        //更改用户文章数目
        Post post = getPostById(id);
        User user = userService.getUserById(post.getUid());
        user.setPosts(user.getPosts()-1);
        userService.updateUser(user);
        baseMapper.deleteById(id);
    }

    @Override
    public void delPost(List<Integer> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        Post post = baseMapper.selectById(id);
        if (post == null){
            throw  new CFException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        if (status==0 || status==1 || status == 2){
            post.setStatus(status);
            baseMapper.updateById(post);
        }else {
            throw new CFException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public List<Post> selectPostByBatchIds(Collection<Integer> postIds) {
        List<Post> posts = new ArrayList<>();
        if (postIds.size() == 0)
        {
            return posts;
        }
        posts = baseMapper.selectBatchIds(postIds);
        return posts;
    }

    @Override
    public List<Post> getPostByUserId(Integer id) {
        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<>();
        postQueryWrapper.eq("uid",id);
        postQueryWrapper.eq("status",1);
        List<Post> posts = baseMapper.selectList(postQueryWrapper);
        return posts;
    }

    @Override
    public boolean checkPostBelong(Integer uid, Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        queryWrapper.eq("id",id);
        Post post = baseMapper.selectOne(queryWrapper);
        if (post!=null){
            return true;
        }
        return false;
    }

    @Override
    public void updateComments(Post post) {
        baseMapper.updateById(post);
    }

    @Override
    public List<Post> selectAllPost() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

}
