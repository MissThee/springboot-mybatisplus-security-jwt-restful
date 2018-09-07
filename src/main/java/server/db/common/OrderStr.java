package server.db.common;

public class OrderStr {
    public static final String sqlOrderByReportHourDecodeStr =
            "DECODE(report_hour," +
                    "'08:00:00',1," +
                    "'10:00:00',2," +
                    "'12:00:00',3," +
                    "'14:00:00',4," +
                    "'16:00:00',5," +
                    "'18:00:00',6," +
                    "'20:00:00',7," +
                    "'22:00:00',8," +
                    "'00:00:00',9," +
                    "'02:00:00',10," +
                    "'04:00:00',11," +
                    "'06:00:00',12," +
                    "'08：00：00',1," +
                    "'10：00：00',2," +
                    "'12：00：00',3," +
                    "'14：00：00',4," +
                    "'16：00：00',5," +
                    "'18：00：00',6," +
                    "'20：00：00',7," +
                    "'22：00：00',8," +
                    "'00：00：00',9," +
                    "'02：00：00',10," +
                    "'04：00：00',11," +
                    "'06：00：00',12)";

    public static final String sqlOrderByReportHourCaseWhenStr =
            "(case " +
            " when REPORT_HOUR='08:00:00' then 1" +
            " when REPORT_HOUR='10:00:00' then 2 " +
            " when REPORT_HOUR='12:00:00' then 3 " +
            " when REPORT_HOUR='14:00:00' then 4 " +
            " when REPORT_HOUR='16:00:00' then 5 " +
            " when REPORT_HOUR='18:00:00' then 6 " +
            " when REPORT_HOUR='20:00:00' then 7 " +
            " when REPORT_HOUR='22:00:00' then 8 " +
            " when REPORT_HOUR='00:00:00' then 9 " +
            " when REPORT_HOUR='02:00:00' then 10 " +
            " when REPORT_HOUR='04:00:00' then 11 " +
            " when REPORT_HOUR='06:00:00' then 12 " +
            " when REPORT_HOUR='08：00：00' then 1 " +
            " when REPORT_HOUR='10：00：00' then 2 " +
            " when REPORT_HOUR='12：00：00' then 3 " +
            " when REPORT_HOUR='14：00：00' then 4 " +
            " when REPORT_HOUR='16：00：00' then 5 " +
            " when REPORT_HOUR='18：00：00' then 6 " +
            " when REPORT_HOUR='20：00：00' then 7 " +
            " when REPORT_HOUR='22：00：00' then 8 " +
            " when REPORT_HOUR='00：00：00' then 9 " +
            " when REPORT_HOUR='02：00：00' then 10 " +
            " when REPORT_HOUR='04：00：00' then 11 " +
            " when REPORT_HOUR='06：00：00' then 12 " +
            " end)";
}
