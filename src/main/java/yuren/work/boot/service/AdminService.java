package yuren.work.boot.service;

import yuren.work.boot.pojo.User;
import yuren.work.boot.query.UserQueryVo;

import java.util.List;
import java.util.Map;

public interface AdminService {
    Map<String, Object> selectUserPage(Integer page, Integer limit, UserQueryVo userQueryVo);


    void updateStatus(Integer id, Integer status);

    void delUser(List<Integer> idList);

    User getUserById(Integer id);

    void editUser(User user);

}
