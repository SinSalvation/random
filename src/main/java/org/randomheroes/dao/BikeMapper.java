package org.randomheroes.dao;

import org.randomheroes.bean.Bike;
import javax.annotation.Resource;
import java.util.List;

@Resource
public interface BikeMapper {
    int deleteByPrimaryKey(Integer bike_id);

    int insert(Bike bike);

    Bike selectByPrimaryKey(Integer bike_id);

    int updateByPrimaryKey(Bike bike);

    List<Bike> selectAll();
}