package com.erp.common.utils;

import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

/** map集合工具类
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-07-27 17:50
 */
public class MapUtil {

    //通过反射机制     将Map转为实体类
    public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
        T t = null;
        try {
            t = entity.newInstance();
            for (Field field : entity.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    Object object = map.get(field.getName());
                    if (object != null) {
                        if (type.isAssignableFrom(Double.class)) {
                            Double aDouble = Double.valueOf(object.toString());
                            if (field.getType().isAssignableFrom(aDouble.getClass())) {
                                field.set(t, aDouble);
                            }
                        }else {
                            if (field.getType().isAssignableFrom(object.getClass())) {
                                field.set(t, object);
                            }
                        }
                    }

                    field.setAccessible(flag);
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /*
    *   去掉map中value为null的key
    * */
    public static Map<String, Object> removeNullString(Map<String, Object> param) {
        try {
            //hashMap.remove(ele);    //出错 修改了映射结构 影响了迭代器遍历
            //用迭代器删除 则不会出错
            param.keySet().removeIf(ele -> (param.get(ele) == null || param.get(ele).equals("")));
            return param;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**工具类，直接调用，啥也不用改
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v,int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //java对象转map
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = Maps.newHashMap();
        if (obj != null) {
            BeanMap beanMap = BeanMap.create(obj);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }
}
