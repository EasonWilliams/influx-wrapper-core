package cn.huang.core.util;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author huangl
 * @description JsonUtils
 * @date 2023/10/13 15:09:46
 */
public class JsonUtils {

    public static <T> String objToJsonStr(T obj) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                Object fieldValue = field.get(obj);
                jsonBuilder.append("\"").append(fieldName).append("\":");
                if (fieldValue instanceof String) {
                    jsonBuilder.append("\"").append(fieldValue).append("\"");
                } else {
                    jsonBuilder.append(fieldValue);
                }
                if (i < fields.length - 1) {
                    jsonBuilder.append(",");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    public static <T> String arrToJsonStr(List<T> objs) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for (int i = 0; i < objs.size(); i++) {
            Object obj = objs.get(i);
            jsonBuilder.append(objToJsonStr(obj));
            if (i < objs.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

}
