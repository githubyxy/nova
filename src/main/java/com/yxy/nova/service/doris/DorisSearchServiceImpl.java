package com.yxy.nova.service.doris;

import com.yxy.nova.bean.doris.CallRecordFlattenedDTO;
import com.yxy.nova.doris.CallRecordRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class DorisSearchServiceImpl implements DorisSearchService {
    @Resource(name="dorisJdbcTemplate")
    private JdbcTemplate dorisJdbcTemplate;


    @Override
    public List<CallRecordFlattenedDTO> query() {
        // 查询分页数据
        StringBuilder pageSqlBuilder = new StringBuilder("select detail")
                .append("\n")
                .append("from call_record")
                .append("\n")
                .append("order by gmtCreate desc")
                .append("\n")
                .append(String.format("limit %d, %d", 0, 10));

        String pageSql = pageSqlBuilder.toString();
        log.info("page sql:{}", pageSql);
        List<CallRecordFlattenedDTO> callRecordFlattenedDTOList = dorisJdbcTemplate.query(pageSql, new CallRecordRowMapper());

        return callRecordFlattenedDTOList;
    }



}
