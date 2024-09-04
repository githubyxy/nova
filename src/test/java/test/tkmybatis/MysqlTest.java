package test.tkmybatis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.didiglobal.turbo.engine.annotation.EnableTurboEngine;
import com.didiglobal.turbo.engine.common.FlowDefinitionStatus;
import com.didiglobal.turbo.engine.dao.FlowDefinitionDAO;
import com.didiglobal.turbo.engine.engine.ProcessEngine;
import com.didiglobal.turbo.engine.entity.FlowDefinitionPO;
import com.didiglobal.turbo.engine.model.*;
import com.didiglobal.turbo.engine.param.*;
import com.didiglobal.turbo.engine.result.*;
import com.google.common.collect.Lists;
import com.yxy.nova.NovaApplication;
import com.yxy.nova.dal.mysql.dataobject.UserDO;
import com.yxy.nova.dal.mysql.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest(classes = NovaApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@EnableTurboEngine
public class MysqlTest {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Resource
    private UserMapper userMapper;
    @Test
    public void mapperTest() {
        Example example = new Example(UserDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", 1);

        List<String> nameList = new ArrayList<>();
        nameList.add("hermes");
        nameList.add("闪应坐席5");

        Example.Criteria criteria1 = example.createCriteria();
        nameList.forEach(realName -> {
            criteria1.orLike("realName", "%" + realName + "%");
        });
        example.and(criteria1);

        List<UserDO> userDOS = userMapper.selectByExample(example);
        System.out.println(JSON.toJSONString(userDOS));
    }
}
