package yuren.work.boot.service;

import yuren.work.boot.pojo.User;

import java.util.Map;

public interface UserService {
    User getUserById(Integer id);

    void delUser(Integer id);

    Map<String, Object> getUserIndexInfo(String nickName);

    void updateUser(User user);

    Boolean checkNickName(String nickName);
}
