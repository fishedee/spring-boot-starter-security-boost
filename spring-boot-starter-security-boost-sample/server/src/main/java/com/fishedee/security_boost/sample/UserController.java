package com.fishedee.security_boost.sample;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Types;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Data
    public static class Count{
        private int count;
    }

    @GetMapping("search")
    public Result<Page<List<User>>> search(@RequestParam("pageIndex") int pageIndex,@RequestParam("pageSize") int pageSize){
        List<User> data = this.jdbcTemplate.query("select * from my_user limit ?,?",
                new Object[]{pageIndex,pageSize},
                new int[]{Types.INTEGER,Types.INTEGER},
                new BeanPropertyRowMapper<>(User.class));

        List<Count> count = this.jdbcTemplate.query("select count(*) as count from my_user",new BeanPropertyRowMapper<>(Count.class));

        Page<List<User>> result = new Page<>();
        result.setData(data);
        result.setCount(count.get(0).getCount());
        return Result.success(result);
    }
}
