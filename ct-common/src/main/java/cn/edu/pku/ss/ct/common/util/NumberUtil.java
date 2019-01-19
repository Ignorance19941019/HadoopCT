package cn.edu.pku.ss.ct.common.util;

import java.text.DecimalFormat;

public class NumberUtil {
    public static String zeroPrefixFormat(int num, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            builder.append("0");
        }

        DecimalFormat df = new DecimalFormat(builder.toString());
        return df.format(num);
    }
}
