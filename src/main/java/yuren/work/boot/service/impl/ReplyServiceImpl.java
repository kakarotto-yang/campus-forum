package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuren.work.boot.mapper.ReplyMapper;
import yuren.work.boot.pojo.Comment;
import yuren.work.boot.pojo.Post;
import yuren.work.boot.pojo.Reply;
import yuren.work.boot.service.CollectService;
import yuren.work.boot.service.CommentService;
import yuren.work.boot.service.PostService;
import yuren.work.boot.service.ReplyService;

import java.util.List;

@Service
@Transactional
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Override
    public void addReply(Reply reply) {
        //改变相应post的评论数
        Comment comment = commentService.getCommentById(reply.getCommentId());
        Post post = postService.getPostById(comment.getPostId());
        post.setComments(post.getComments()+1);
        postService.updateComments(post);
        baseMapper.insert(reply);
    }

    @Override
    public void delReply(Integer id) {
        //改变相应post的评论数
        Reply reply = getReplyById(id);
        Comment comment = commentService.getCommentById(reply.getCommentId());
        Post post = postService.getPostById(comment.getPostId());
        post.setComments(post.getComments()-1);
        postService.updateComments(post);
        baseMapper.deleteById(id);
    }

    public Reply getReplyById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<Reply> getReplyList(Integer commentId) {
        QueryWrapper<Reply> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("comment_id",commentId);
        List<Reply> replyList = baseMapper.selectList(queryWrapper);
        return baseMapper.selectList(queryWrapper);
    }
}
