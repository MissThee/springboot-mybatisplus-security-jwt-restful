package com.github.common.tool;

import org.springframework.stereotype.Component;

@Component()
public class IdGenerator {
    /**
     * 起始的时间戳
     */
    private final static long START_TIMESTAMP = 1571712039678L;//    Tue Oct 22 10:41:44 CST 2019 ;
    private final static long DATA_CENTER_ID = 0;
    private final static long MACHINE_ID = 0;

    /**
     * 每一部分占用的位数
     */
    private final static long DATA_CENTER_BIT = 5;//数据中心占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATA_CENTER_NUM = ~(-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_ID_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_ID_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long START_TIMESTAMP_LEFT = DATA_CENTER_ID_LEFT + DATA_CENTER_BIT;

    private long sequence = 0L; //序列号
    private long lastStamp = -1L;//上一次时间戳

    public IdGenerator() {
        if (DATA_CENTER_ID > MAX_DATA_CENTER_NUM || DATA_CENTER_ID < 0) {
            throw new IllegalArgumentException("DATA_CENTER_ID can't be greater than MAX_DATA_CENTER_NUM or less than 0");
        }
        if (MACHINE_ID > MAX_MACHINE_NUM || MACHINE_ID < 0) {
            throw new IllegalArgumentException("MACHINE_ID can't be greater than MAX_MACHINE_NUM or less than 0");
        }
    }

    /**
     * 产生下一个ID
     */
    public synchronized long nextId() {
        long newStamp = getNewStamp();
        if (newStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (newStamp == lastStamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                newStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = newStamp;

        return (newStamp - START_TIMESTAMP) << START_TIMESTAMP_LEFT //时间戳部分
                | DATA_CENTER_ID << DATA_CENTER_ID_LEFT       //数据中心部分
                | MACHINE_ID << MACHINE_ID_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }
}