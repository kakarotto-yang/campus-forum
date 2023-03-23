package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuren.work.boot.mapper.CommentMapper;
import yuren.work.boot.pojo.Comment;
import yuren.work.boot.pojo.Post;
import yuren.work.boot.pojo.Reply;
import yuren.work.boot.service.CommentService;
import yuren.work.boot.service.PostService;
import yuren.work.boot.service.ReplyService;
import yuren.work.boot.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper,Comment> implements CommentService {

    @Autowired
    ReplyService replyService;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Override
    public void addComment(Comment comment) {
        //改变相应post的评论数
        Post post = postService.getPostById(comment.getPostId());
        post.setComments(post.getComments()+1);
        postService.updateComments(post);
        baseMapper.insert(comment);
    }

    @Override
    public void delComment(Integer id) {
        //改变相应post的评论数
        Comment comment = getCommentById(id);
        Post post = postService.getPostById(comment.getPostId());
        post.setComments(post.getComments()-1);
        postService.updateComments(post);
        baseMapper.deleteById(id);
    }

    @Override
    public List<Comment> getComment(Integer postId) {
        QueryWrapper<Comment> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("post_id",postId);
        List<Comment> comments = baseMapper.selectList(queryWrapper);
        List<Comment> commentList = new ArrayList<>();
        //设置好评论的作者信息
        for (Comment comment:
                comments) {

            comment.setFrom(userService.getUserById(comment.getFromUid()));
            List<Reply> replyList = replyService.getReplyList(comment.getId());
            for (Reply reply:
                 replyList) {
                reply.setFrom(userService.getUserById(reply.getFromUid()));
                reply.setTo(userService.getUserById(reply.getToUid()));
            }
            comment.setReplyList(replyList);
            commentList.add(comment);
        }
        return commentList;
    }

    @Override
    public Comment getCommentById(Integer commentId) {
        return baseMapper.selectById(commentId);
    }
}
