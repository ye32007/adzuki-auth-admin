package com.adzuki.admin.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "admin_type_code")
public class TypeCode implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 代码类型编号
     */
    @Column(name = "tcode")
    private String tcode;

    /**
     * 代码类型名称
     */
    @Column(name = "tname")
    private String tname;

    /**
     * 代码内容
     */
    @Column(name = "content")
    private String content;

    @Column(name = "created_time",insertable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time",insertable = false, updatable = false)
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}