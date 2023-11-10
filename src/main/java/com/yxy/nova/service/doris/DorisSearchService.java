package com.yxy.nova.service.doris;

import com.yxy.nova.bean.doris.CallRecordFlattenedDTO;

import java.util.List;

public interface DorisSearchService {

    public List<CallRecordFlattenedDTO> query();
}
