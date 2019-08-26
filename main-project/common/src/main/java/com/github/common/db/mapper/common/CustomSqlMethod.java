package com.github.common.db.mapper.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomSqlMethod {

    INSERT_BATCH("insertBatch", "<script> INSERT INTO %s (%s) %s</script>");

    private final String method;
    private final String sql;
}