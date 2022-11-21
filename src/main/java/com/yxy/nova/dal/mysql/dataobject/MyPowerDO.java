package com.yxy.nova.dal.mysql.dataobject;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "my_power")
public class MyPowerDO implements Serializable {
    @Id
    private Integer id;

    private String content;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}