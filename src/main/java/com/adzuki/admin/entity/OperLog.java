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
@Table(name = "admin_oper_log")
public class OperLog implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "content")
    private String content;

    @Column(name = "oper_data")
    private String operData;

    @Column(name = "ip")
    private String ip;

    @Column(name = "oper_time")
    private Date operTime;
    
    @Column(name = "created_time",insertable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time",insertable = false, updatable = false)
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}