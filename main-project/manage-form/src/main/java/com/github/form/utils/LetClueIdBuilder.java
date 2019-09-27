package com.github.form.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.common.db.entity.primary.LetClue;
import com.github.form.db.mapper.primary.letter.clue.LetClueMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class LetClueIdBuilder {
    private final LetClueMapper letClueMapper;

    private static Integer currentIdYear;//默认获取当前年号
    private static Integer currentIdIndex;//默认序号从1开始

    public LetClueIdBuilder(LetClueMapper letClueMapper) {
        this.letClueMapper = letClueMapper;
    }

    //id构建构建，获取“年加+4位序号编号”的id
    public synchronized String getId() throws Exception {
        if (currentIdYear == null) {//首次调用时赋予初始值
            QueryWrapper<LetClue> qw = new QueryWrapper<>();
            qw.orderByDesc(LetClue.ID);//id倒序
            qw.last("limit 1");
            List<LetClue> letClueList = letClueMapper.selectList(qw);//查找id倒序的第一条记录
            if (letClueList.size() > 0) {//如果缓存的当前id为有上一条的话，记录最后一条线索年号、编号
                String LastClueId = letClueList.get(0).getId();//获取最后一条记录的id
                currentIdYear = Integer.parseInt(LastClueId.substring(0, 4));//截取年号
                currentIdIndex = Integer.parseInt(LastClueId.substring(4, 8));//截取序号编号
            } else {
                currentIdYear = LocalDate.now().getYear();
                currentIdIndex = 0;
            }
        }
        if (currentIdYear != null) {//首次及后续调用判断是否跨年
            Integer currentYear = LocalDate.now().getYear();
            if (currentYear > currentIdYear) {//如果 当前年号不等于最后一条记录，表明已经跨年（1年或多年）。年号使用当前年号，序号从1开始
                currentIdYear = currentYear;
                currentIdIndex = 0;
            } else if (currentYear < currentIdYear) {
                throw new Exception("服务器系统时间错误，检测到时间会跳");
            }
        }
        return currentIdYear + String.format("%04d", ++currentIdIndex);
    }
}
