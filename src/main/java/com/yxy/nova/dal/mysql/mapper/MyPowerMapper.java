package com.yxy.nova.dal.mysql.mapper;

import com.yxy.nova.dal.mysql.dataobject.MyPowerDO;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface MyPowerMapper extends Mapper<MyPowerDO>, InsertListMapper<MyPowerDO> {
}