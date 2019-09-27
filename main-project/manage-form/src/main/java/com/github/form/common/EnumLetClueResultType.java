package com.github.form.common;

import java.util.Objects;

//配合数据库let_clue表的result_type字段
public enum EnumLetClueResultType {
    NULL(null, false, false),
    Default(0, true, false),
    FenLei1(1, false, false),
    FenLei2(2, false, false),
    AnGuan(3, false, true),
    BoHui(4, true, false);
    private Integer value;
    private boolean isEditableInLetClue;
    private boolean isEditableInAnGuan;

    EnumLetClueResultType(Integer value, Boolean isEditableInLetClue, Boolean isEditableInAnGuan) {
        this.value = value;
        this.isEditableInLetClue = isEditableInLetClue;
        this.isEditableInAnGuan = isEditableInAnGuan;
    }

    public Integer getValue() {
        return this.value;
    }

    public Boolean getIsEditableInLetClue() {
        return this.isEditableInLetClue;
    }

    public Boolean getIsEditableInAnGuan() {
        return this.isEditableInAnGuan;
    }

    public static EnumLetClueResultType getEnumByValue(Integer value) {
        for (EnumLetClueResultType item : EnumLetClueResultType.values()) {
            if (Objects.equals(item.getValue(), value)) {
                return item;
            }
        }
        return NULL;
    }
}
