package com.github.missthee.config.log.format;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class ProcessIdClassicConverter extends ClassicConverter {

    /**
     * (non-Javadoc)
     *
     * @see ch.qos.logback.core.pattern.Converter#convert(java.lang.Object)
     */
    public String convert(ILoggingEvent event) {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        return name.substring(0, name.indexOf("@"));
    }

}
