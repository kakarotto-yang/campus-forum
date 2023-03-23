package yuren.work.boot.service;

public interface EmailCodeService {
    boolean sendCode(String to, String code);
}
