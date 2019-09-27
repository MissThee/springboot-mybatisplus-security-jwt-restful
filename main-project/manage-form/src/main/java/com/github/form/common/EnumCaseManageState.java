package com.github.form.common;

//case_manage的stateId字段值
public enum EnumCaseManageState {
    Default(0),
    YiBanLi(1),
    YiBoHui(2);
    private Integer value;

    EnumCaseManageState(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
