package yuren.work.boot.service;

import yuren.work.boot.pojo.UserLogin;

import java.util.Map;

public interface InfoService {
    Map<String, Object> loginEmail(UserLogin userLogin);

    Object adminLogin(UserLogin userLogin);
}
