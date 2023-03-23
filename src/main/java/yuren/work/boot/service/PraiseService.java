package yuren.work.boot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import yuren.work.boot.pojo.Follow;
import yuren.work.boot.pojo.Praise;

public interface PraiseService {

    void praise(Praise praise);

    boolean checkIsPraise(Integer uid,Integer postId);
}
