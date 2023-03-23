package yuren.work.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;
import yuren.work.boot.pojo.User;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
