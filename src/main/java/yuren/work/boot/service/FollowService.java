package yuren.work.boot.service;

import yuren.work.boot.pojo.Follow;
import yuren.work.boot.pojo.User;

import java.util.List;

public interface FollowService {

    List<Follow> getFollowByUid(Integer id);

    void follow(Follow follow);

    List<Follow> getFollowByFid(Integer id);

    boolean checkIsFollow(Integer uid, Integer fid);
}
