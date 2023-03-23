package yuren.work.boot.service;

import yuren.work.boot.pojo.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);

    void delComment(Integer id);

    //根据postid获取评论
    List<Comment> getComment(Integer postId);

    Comment getCommentById(Integer commentId);
}
