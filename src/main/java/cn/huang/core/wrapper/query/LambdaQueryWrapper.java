package cn.huang.core.wrapper.query;

import cn.huang.core.wrapper.AbstractWrapper;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;

/**
 * @author huangl
 * @description LambdaQueryWrapper
 * @date 2023/9/27 14:55:11
 */
public class LambdaQueryWrapper<T> extends AbstractWrapper<T> implements LambdaQuery<T> {
    private boolean isRanged = false;
    private final String MEASUREMENT;

    public LambdaQueryWrapper(Class<T> entityClass) {
        super(entityClass);
        String measurement = this.getEntityClass().getAnnotation(Measurement.class).name();
        if (measurement != null && !measurement.equals("")) {
            this.MEASUREMENT = measurement;
        } else {
            throw new RuntimeException("[influxdb2] ERROR : @Measurement must be annotated on entity class");
        }
    }

    @Override
    public LambdaQueryWrapper<T> range(Instant left, Instant right) {
        FLUX.append(String.format("\n|> range(start: %s, stop: %s)", left, right));
        isRanged = true;
        this.eq("_measurement", MEASUREMENT);
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> eq(String key, Object val) {
        isRanged();
        FLUX.append(String.format("\n|> filter(fn: (r) => r[\"%s\"] == \"%s\")", key, val));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> ne(String key, Object val) {
        isRanged();
        FLUX.append(String.format("\n|> filter(fn: (r) => r[\"%s\"] != \"%s\")", key, val));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> gt(String key, Object val) {
        isRanged();
        FLUX.append(String.format("\n|> filter(fn: (r) => r[\"%s\"] > %s)", key, val));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> ge(String key, Object val) {
        isRanged();
        FLUX.append(String.format("\n|> filter(fn: (r) => r[\"%s\"] >= %s)", key, val));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> lt(String key, Object val) {
        isRanged();
        FLUX.append(String.format("\n|> filter(fn: (r) => r[\"%s\"] < %s)", key, val));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> le(String key, Object val) {
        isRanged();
        FLUX.append(String.format("\n|> filter(fn: (r) => r[\"%s\"] <= %s)", key, val));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> between(String key, Object leftVal, Object rightVal) {
        isRanged();
        FLUX.append(String.format("\n|> filter(fn: (r) => r[\"%s\"] >= %s and r[\"%s\"] <= %s)", key, leftVal, key, rightVal));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> notBetween(String key, Object leftVal, Object rightVal) {
        isRanged();
        FLUX.append(String.format("\n|> filter(fn: (r) => r[\"%s\"] < %s or r[\"%s\"] > %s)", key, leftVal, key, rightVal));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> like(String key, Object val) {
        isRanged();
        FLUX.append(String.format("\n|> filter(fn: (r) => r[\"%s\"] =~ /%s/)", key, val));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> pivot() {
        isRanged();
        FLUX.append("\n|> pivot(rowKey: [\"_time\"");
        Field[] fields = this.getEntityClass().getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).tag())
                FLUX.append(String.format(",\"%s\"", field.getAnnotation(Column.class).name() == null ? field.getName() : field.getAnnotation(Column.class).name()));
        }
        FLUX.append("], columnKey: [\"_field\"], valueColumn: \"_value\")");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> map(String key, String as) {
        isRanged();
        FLUX.append(String.format("\n|> map(fn: (r) => ({r with %s: r.%s}))", key, as));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> group(List<String> keys) {
        FLUX.append("\n|> group(columns: [");
        for (int i = 0; i < keys.size(); i++) {
            FLUX.append(String.format("\"%s\"", keys.get(i)));
            if (i != keys.size() - 1)
                FLUX.append(",");
        }
        FLUX.append("], mode: \"by\")");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> window(String time) {
        FLUX.append(String.format("\n|> window(every: %s)", time));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> last() {
        FLUX.append("\n|> last()");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> sort(List<String> keys, Boolean desc) {
        FLUX.append("\n|> sort(columns: [");
        for (int i = 0; i < keys.size(); i++) {
            FLUX.append(String.format("\"%s\"", keys.get(i)));
            if (i != keys.size() - 1)
                FLUX.append(",");
        }
        FLUX.append(String.format("], desc: %s)", desc));
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> count() {
        FLUX.append("\n|> count()");
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> limit(Long n, Long offset) {
        FLUX.append(String.format("\n|> limit(n: %s, offset: %s)", n, offset));
        return this;
    }

    private void isRanged() {
        if (!isRanged)
            throw new RuntimeException("[influxdb2] ERROR : other operations must be called after range");
    }

    public LambdaQueryWrapper<T> copy() {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>(this.getEntityClass());
        wrapper.FLUX = new StringBuffer(this.FLUX);
        return wrapper;
    }
}
