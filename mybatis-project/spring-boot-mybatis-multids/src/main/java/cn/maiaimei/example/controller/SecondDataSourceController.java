package cn.maiaimei.example.controller;

import cn.maiaimei.example.entity.User;
import cn.maiaimei.example.entity.UserExample;
import cn.maiaimei.example.mapper.second.SecondUserMapper;
import cn.maiaimei.framework.beans.PaginationResult;
import cn.maiaimei.framework.util.SFID;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static cn.maiaimei.framework.util.BeanUtils.copyList;

@Api
@RestController
@RequestMapping("/second")
public class SecondDataSourceController {
    @Resource
    SecondUserMapper secondUserMapper;

    @PostMapping
    public User insert(@RequestBody User user) {
        Date now = new Date();
        user.setId(SFID.nextId());
        user.setGmtCreate(now);
        user.setGmtModified(now);
        secondUserMapper.insertSelective(user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        secondUserMapper.updateByPrimaryKeySelective(user);
        return secondUserMapper.selectByPrimaryKey(user.getId());
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return secondUserMapper.deleteByPrimaryKey(id);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return secondUserMapper.selectByPrimaryKey(id);
    }

    @PostMapping("/pagequery")
    public PaginationResult<User> pageQuery(@RequestBody(required = false) User user,
                                            @RequestParam(required = false, defaultValue = "1") Integer current,
                                            @RequestParam(required = false, defaultValue = "10") Integer size) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(user.getUsername())) {
            criteria.andUsernameLike("%".concat(user.getUsername()).concat("%"));
        }

        Page<User> page = PageHelper.startPage(current, size);
        List<User> users = secondUserMapper.selectByExample(example);
        List<User> userList = copyList(users, User.class);
        PaginationResult<User> pagingResult = new PaginationResult<>();
        pagingResult.setRecords(userList);
        pagingResult.setTotal(page.getTotal());
        pagingResult.setCurrent(page.getPageNum());
        pagingResult.setSize(page.getPageSize());
        return pagingResult;
    }

}
