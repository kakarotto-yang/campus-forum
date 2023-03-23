package yuren.work.boot.service;

import yuren.work.boot.pojo.Post;
import yuren.work.boot.query.PostQueryVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PostService {
    Map<String, Object> selectPostPage(Integer uid,Integer page, Integer pageSize, PostQueryVo postQueryVo);
    Map<String, Object> selectPostPage(Integer page, Integer pageSize, PostQueryVo postQueryVo);
    Post getPostById(Integer id);

    int addPost(Post post);

    void editPost(Post post);

    void delPost(Integer id);

    void delPost(List<Integer> idList);

    void updateStatus(Integer id, Integer status);

    List<Post> selectPostByBatchIds(Collection<Integer> postIds);

    List<Post> getPostByUserId(Integer id);

    boolean checkPostBelong(Integer uid, Integer id);

    void updateComments(Post post);

    List<Post> selectAllPost();
}
