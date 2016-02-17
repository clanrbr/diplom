package utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by macbook on 1/19/16.
 */
public class ConstantsImportant {
    public static final String imotApiUrl="http://api.imot.bg/mobile_api/";
    public static final String imotApiUrlSearch="http://api.imot.bg/mobile_api/search";

    public static final Map<String,String> orderTypeHome;
    static {
        HashMap<String,String> orderTypeHomeDummy =new HashMap<>();
        orderTypeHomeDummy.put("1-СТАЕН","1");
        orderTypeHomeDummy.put("2-СТАЕН","2");
        orderTypeHomeDummy.put("3-СТАЕН","3");
        orderTypeHomeDummy.put("4-СТАЕН","4");
        orderTypeHomeDummy.put("МНОГОСТАЕН","5");
        orderTypeHomeDummy.put("МЕЗОНЕТ","6");
        orderTypeHomeDummy.put("ОФИС","7");
        orderTypeHomeDummy.put("АТЕЛИЕ ТАВАН","8");
        orderTypeHomeDummy.put("ЕТАЖ ОТ КЪЩА","9");
        orderTypeHomeDummy.put("КЪЩА","10");
        orderTypeHomeDummy.put("ВИЛА","11");
        orderTypeHomeDummy.put("МАГАЗИН","12");
        orderTypeHomeDummy.put("ЗАВЕДЕНИЕ","13");
        orderTypeHomeDummy.put("СКЛАД","14");
        orderTypeHomeDummy.put("ГАРАЖ","15");
        orderTypeHomeDummy.put("ПРОМ. ПОМЕЩЕНИЕ","16");
        orderTypeHomeDummy.put("ХОТЕЛ","17");
        orderTypeHomeDummy.put("ПАРЦЕЛ","18");
        orderTypeHomeDummy.put("ЗЕМЕДЕЛСКА ЗЕМЯ","19");
        orderTypeHome = Collections.unmodifiableMap(orderTypeHomeDummy);
    }

    public static final Map<String,String> orderTypeHomeRent;
    static {
        HashMap<String,String> orderTypeHomeDummy =new HashMap<>();
        orderTypeHomeDummy.put("СТАЯ","1");
        orderTypeHomeDummy.put("1-СТАЕН","2");
        orderTypeHomeDummy.put("2-СТАЕН","3");
        orderTypeHomeDummy.put("3-СТАЕН","4");
        orderTypeHomeDummy.put("4-СТАЕН","5");
        orderTypeHomeDummy.put("МНОГОСТАЕН","6");
        orderTypeHomeDummy.put("МЕЗОНЕТ","7");
        orderTypeHomeDummy.put("ОФИС","8");
        orderTypeHomeDummy.put("АТЕЛИЕ ТАВАН","9");
        orderTypeHomeDummy.put("ЕТАЖ ОТ КЪЩА","10");
        orderTypeHomeDummy.put("КЪЩА","11");
        orderTypeHomeDummy.put("ВИЛА","12");
        orderTypeHomeDummy.put("МАГАЗИН","13");
        orderTypeHomeDummy.put("ЗАВЕДЕНИЕ","14");
        orderTypeHomeDummy.put("СКЛАД","15");
        orderTypeHomeDummy.put("ГАРАЖ","16");
        orderTypeHomeDummy.put("ПРОМ. ПОМЕЩЕНИЕ","17");
        orderTypeHomeDummy.put("ХОТЕЛ","18");
        orderTypeHomeDummy.put("МЯСТО","19");
        orderTypeHomeDummy.put("ЗЕМЕДЕЛСКА ЗЕМЯ","20");
        orderTypeHomeRent = Collections.unmodifiableMap(orderTypeHomeDummy);
    }
}
