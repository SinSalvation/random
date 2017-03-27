package org.randomheroes.dao;

import org.randomheroes.bean.Order;
import javax.annotation.Resource;
import java.util.List;

@Resource
public interface OrderMapper {
    int deleteByPrimaryKey(String order_id);

    int insert(Order record);

    Order selectByPrimaryKey(Integer order_id);

    int updateByPrimaryKey(Order record);

    List<Order> selectByUserId(String user_id);

    List<Order> selectAll();
}