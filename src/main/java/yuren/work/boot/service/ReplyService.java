package yuren.work.boot.service;

import yuren.work.boot.pojo.Reply;

import java.util.List;

public interface ReplyService {
    void addReply(Reply reply);

    void delReply(Integer id);
    //根据postid获取评论
    List<Reply> getReplyList(Integer commentId);
}
