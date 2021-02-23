package com.yxy.nova.dal.mysql.mapper;

import com.yxy.nova.dal.mysql.dataobject.TaskItemExecCallDO;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface TaskItemExecCallMapper extends Mapper<TaskItemExecCallDO>, InsertListMapper<TaskItemExecCallDO> {
}