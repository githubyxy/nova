package com.yxy.nova.service.impl;

import com.yxy.nova.dal.mysql.dataobject.MyPowerDO;
import com.yxy.nova.dal.mysql.mapper.MyPowerMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author yuxiaoyu
 * @date 2021/3/5 下午6:07
 * @Description
 */
@Service
public class MyPowerServiceImpl implements MyPowerService {

    @Autowired
    private MyPowerMapper myPowerMapper;

    @Override
    public void insert(String content) {
        MyPowerDO myPowerDO = new MyPowerDO();
        myPowerDO.setContent(content);
        myPowerMapper.insert(myPowerDO);
    }

    @Override
    public String getAllPower() {
        Example example = new Example(MyPowerDO.class);
        List<MyPowerDO> myPowerDOS = myPowerMapper.selectByExample(example);
        StringBuffer sb = new StringBuffer();

        myPowerDOS.forEach(myPowerDO -> {
            sb.append(myPowerDO.getContent()).append("\n").append("\n");
        });

        return sb.toString();
    }

    @Override
    public String fuzzyQuery(String content) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(content)) {
            Example example = new Example(MyPowerDO.class);
            example.createCriteria().andLike("content", "%" + content + "%");
            List<MyPowerDO> myPowerDOS = myPowerMapper.selectByExample(example);
            myPowerDOS.forEach(myPowerDO -> {
                sb.append(myPowerDO.getContent()).append("\n").append("\n");
            });
        }

        return sb.toString();
    }
}
