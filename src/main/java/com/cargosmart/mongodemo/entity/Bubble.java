package com.cargosmart.mongodemo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author licankun
 */
@Data
@NoArgsConstructor
public class Bubble implements Serializable {
    private static final long serialVersionUID = 7281504310779916906L;

    @Id
    private String id;
    private boolean isViewable;
    private String color;
    private Date createTime;
}
