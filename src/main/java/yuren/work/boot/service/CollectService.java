package yuren.work.boot.service;

import yuren.work.boot.pojo.Collect;
import yuren.work.boot.pojo.Praise;

import java.util.List;

public interface CollectService {

    void collect(Collect collect);

    List<Collect> getCollectionByUid(Integer id);

    boolean checkIsCollect(Integer uid, Integer postId);
}
