package com.beini.ui.fragment.wifi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/4/13.
 */

public class CreatePasswordUtils {

    public static String returnPassword() {
        String str = null;
        //从6位开始
        return str;
    }

    public static List<String> getPassword() {
        List<String> strings = new ArrayList<>();
        int total = 0;
        co:
        for (int a = 0; a <= 8; a++) {
            for (int b = 0; b <= 8; b++) {
                for (int c = 0; c <= 8; c++) {
                    for (int d = 0; d <= 8; d++) {
                        for (int e = 0; e <= 8; e++) {
                            for (int f = 0; f <= 8; f++) {
                                for (int g = 0; g <= 8; g++) {
                                    for (int h = 0; h <= 8; h++) {
                                        strings.add(a + "" + b + "" + c + "" + d + "" + e + "" + f + "" + g + "" + h);
                                        System.out.println(a + "" + b + "" + c + "" + d + "" + e + "" + f + "" + g + "" + h);
                                        total++;
                                        if ((a + "" + b + "" + c + "" + d + "" + e + "" + f + "" + g + "" + h).equals("00002308")) {
                                            break co;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("  total== " + total);
        return strings;
    }
}
