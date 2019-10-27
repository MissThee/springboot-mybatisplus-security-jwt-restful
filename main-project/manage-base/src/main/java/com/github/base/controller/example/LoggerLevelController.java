package com.github.base.controller.example;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.github.common.tool.Res;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//动态控制日志等级接口
@RestController
@RequestMapping("/log")
public class LoggerLevelController {
    @RequestMapping({"/level/{packageName}/{level}","/level/{level}"})
    public Res changeLoggerLevel(@PathVariable(value = "packageName", required = false) String packageName, @PathVariable("level") String level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        packageName = StringUtils.isEmpty(packageName) ? "root" : packageName;
        Logger logger = loggerContext.getLogger(packageName);
        Level levelOld = logger.getLevel();
        String levelOldName;
        try {
            levelOldName = levelOld.levelStr;
        } catch (NullPointerException e) {
            return Res.failure("wrong packageName value [" + packageName + "]");
        }
        switch (level.toUpperCase()) {
            case "OFF":
            case "ERROR":
            case "WARN":
            case "INFO":
            case "DEBUG":
            case "TRACE":
            case "ALL":
                logger.setLevel(Level.valueOf(level));
                return Res.success("change " + packageName + " log from [" + levelOldName + "] to [" + level + "]");
            default:
                return Res.failure("wrong level value [" + level + "]");
        }

    }
}
