package org.randomheroes.dao;

import org.randomheroes.bean.User;
import javax.annotation.Resource;

/**
 * Created by jixu_m on 2017/3/24.
 */
@Resource
public interface UserMapper {
    int deleteByPrimaryKey(String user_id);

    int insert(User user);

    User selectByPrimaryKey(String user_id);
}
