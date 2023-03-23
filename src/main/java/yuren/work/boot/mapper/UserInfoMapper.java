package yuren.work.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import yuren.work.boot.pojo.User;

@Repository
public interface UserInfoMapper extends BaseMapper<User> {
}
