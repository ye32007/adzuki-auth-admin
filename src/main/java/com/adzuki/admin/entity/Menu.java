package com.adzuki.admin.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "admin_menu")
public class Menu implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "icon")
    private String icon;

    @Column(name = "folder")
    private Boolean folder;

    @Column(name = "level")
    private Integer level;

    @Column(name = "permit")
    private String permit;

    @Column(name = "pid")
    private Long pid;

    @Column(name = "tag")
    private String tag;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "memo")
    private String memo;

    @Column(name = "buttons")
    private String buttons;

    @Column(name = "version")
    private Integer version;

    @Column(name = "created_time",insertable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time",insertable = false, updatable = false)
    private Date updatedTime;
    
    @Transient
    private List<Menu> childrens;

    private static final long serialVersionUID = 1L;
}