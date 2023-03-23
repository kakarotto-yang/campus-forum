package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import yuren.work.boot.mapper.AuthorityMapper;
import yuren.work.boot.pojo.Authority;
import yuren.work.boot.service.AuthorityService;

@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements AuthorityService {

    @Override
    public boolean isAdmin(Integer uid) {
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        Authority authority = baseMapper.selectOne(queryWrapper);
        if (authority==null){
            return false;
        }
        if (authority.getIsAdmin() == 1){
            return true;
        }
        return false;
    }
}
