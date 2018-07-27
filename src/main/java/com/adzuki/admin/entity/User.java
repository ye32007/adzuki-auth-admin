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
@Table(name = "admin_user")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "chinese_name")
    private String chineseName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "mobile_verified")
    private Boolean mobileVerified;

    @Column(name = "email")
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "head_img")
    private String headImg;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "sex")
    private String sex;

    @Column(name = "location")
    private String location;

    @Column(name = "user_type")
    private Integer userType;

    /**
     * 公司ID
     */
    @Column(name = "company_id")
    private Integer companyId;

    /**
     * 公司名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 是否app登陆
     */
    @Column(name = "app_enabled")
    private Boolean appEnabled;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "register_app")
    private String registerApp;

    @Column(name = "data_range")
    private String dataRange;

    @Column(name = "memo")
    private String memo;

    @Column(name = "ext")
    private String ext;

    @Column(name = "version")
    private Integer version;

    @Column(name = "created_time",insertable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time",insertable = false, updatable = false)
    private Date updatedTime;

    /**
     * 0：未删除，1：已删除
     */
    @Column(name = "deleted")
    private Boolean deleted;

    private static final long serialVersionUID = 1L;
}