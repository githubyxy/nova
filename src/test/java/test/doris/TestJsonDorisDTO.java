package test.doris;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuxiaoyu
 * @date 2021/4/2 下午1:45
 * @Description
 */
@Data
public class TestJsonDorisDTO implements Serializable {

//    /** 分区字段，格式：yyyy-MM-dd */
    private String ds;
    private String jsonDetail;

    /** 创建时间 */
    private String gmtCreate;
    private String gmtModify;

}
