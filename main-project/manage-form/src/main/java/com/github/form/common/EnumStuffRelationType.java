package com.github.form.common;

public enum EnumStuffRelationType {
    XianSuo(1);
    private Integer value;

    EnumStuffRelationType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
