package com.yxy.nova.service.doris;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DorisSearchServiceImpl implements DorisSearchService {
    @Resource(name="dorisJdbcTemplate")
    private JdbcTemplate dorisJdbcTemplate;





}
