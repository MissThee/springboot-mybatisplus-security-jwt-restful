package com.github.common.tool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class SimplePageInfo<T> {
    private List<T> list;
    private Long total;
}
