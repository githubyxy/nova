package com.yxy.nova.doris;

import com.yxy.nova.bean.doris.CallRecordFlattenedDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author shui.ren
 * @description: 将查询结果映射为CallRecordFlattenedDTO
 * @date 2023/9/30 8:56 PM
 */
public class CallRecordRowMapper implements RowMapper<CallRecordFlattenedDTO> {
    @Override
    public CallRecordFlattenedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CallRecordFlattenedDTO dto = new CallRecordFlattenedDTO();
        dto.setDetail(rs.getString("detail"));
        return dto;
    }
}
