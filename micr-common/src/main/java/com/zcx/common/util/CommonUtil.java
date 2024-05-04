package com.zcx.common.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class CommonUtil {

    public static int defaultPageNo(Integer pageNo) {
        int pNo = pageNo;
        if (pageNo == null || pageNo < 1) {
            pNo = 1;
        }
        return pNo;
    }

    public static int defaultPageSize(Integer pageSize) {
        int pSize = pageSize;
        if (pageSize == null || pageSize < 1) {
            pSize = 1;
        }
        return pSize;
    }

    public static String tuoMinPhone(String phone) {
        String result = "***********";
        if (phone != null && phone.trim().length() == 11) {
            result = phone.substring(0, 3) + "******" + phone.substring(9, 11);
        }
        return result;
    }

    public static boolean checkPhone(String phone) {
        boolean flag = false;
        if (phone != null && phone.length() == 11) {
            flag = Pattern.matches("^1[1-9]\\d{9}$", phone);
        }
        return flag;
    }

    public static boolean ge(BigDecimal n1, BigDecimal n2) {
        if (n1 == null || n2 == null) {
            throw new RuntimeException("参数BigDecimal是null");
        }
        return n1.compareTo(n2) >= 0;
    }
}
