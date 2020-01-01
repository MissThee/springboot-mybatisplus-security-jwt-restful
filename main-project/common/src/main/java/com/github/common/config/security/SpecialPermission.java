package com.github.common.config.security;
//记录两个特殊权限，管理员、基础账号。约定前端：账号拥有以下特殊权限值时会拥有更高权限，如所有功能块可见等。
public class SpecialPermission {
    public static final String ADMIN = "[ADMIN]";
    public static final String BASIC = "[BASIC]";
}
