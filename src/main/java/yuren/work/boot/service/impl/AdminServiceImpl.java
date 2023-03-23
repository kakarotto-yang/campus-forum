package yuren.work.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import yuren.work.boot.common.exception.CFException;
import yuren.work.boot.common.result.ResultCodeEnum;
import yuren.work.boot.mapper.UserMapper;
import yuren.work.boot.pojo.User;
import yuren.work.boot.query.UserQueryVo;
import yuren.work.boot.service.AdminService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class AdminServiceImpl extends ServiceImpl<UserMapper, User> implements AdminService {

    @Override
    public Map<String, Object> selectUserPage(Integer page, Integer limit, UserQueryVo userQueryVo) {
        Page<User> page1 = new Page<>(page,limit);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //判断查询条件是否非空
        if (userQueryVo!=null){
            String name = userQueryVo.getName();
            Integer sex = userQueryVo.getSex();
            Integer status = userQueryVo.getStatus();
            String createTimeBegin = userQueryVo.getCreateTimeBegin();
            String createTimeEnd = userQueryVo.getCreateTimeEnd();
            String school = userQueryVo.getSchool();
            if(!StringUtils.isEmpty(name)){
                queryWrapper.like("name",name);
            }
            if(!StringUtils.isEmpty(sex)){
                queryWrapper.eq("sex",sex);
            }
            if(!StringUtils.isEmpty(status)){
                queryWrapper.eq("status",status);
            }
            if(!StringUtils.isEmpty(createTimeBegin)){
                queryWrapper.ge("create_date",createTimeBegin);
            }
            if(!StringUtils.isEmpty(createTimeEnd)){
                queryWrapper.le("create_date",createTimeEnd);
            }
            if(!StringUtils.isEmpty(school)){
                queryWrapper.eq("school",school);
            }
        }
        baseMapper.selectPage(page1,queryWrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("users",page1.getRecords());
        map.put("pages",page1.getPages());
        map.put("total",page1.getTotal());
        map.put("currentSize",page1.getRecords().size());
        return map;
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        if (status == 0 || status == 1){
            User user = baseMapper.selectById(id);
            if (user == null){
                throw  new CFException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }
            user.setStatus(status);
            baseMapper.updateById(user);
        }else {
            throw new CFException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public void delUser(List<Integer> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public User getUserById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void editUser(User user) {
        baseMapper.updateById(user);
    }


}
