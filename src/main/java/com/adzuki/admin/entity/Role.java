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
@Table(name = "admin_role")
public class Role implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "memo")
    private String memo;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "system")
    private Boolean system;

    @Column(name = "assignable")
    private Boolean assignable;

    @Column(name = "version")
    private Integer version;

    @Column(name = "created_time",insertable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time",insertable = false, updatable = false)
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}