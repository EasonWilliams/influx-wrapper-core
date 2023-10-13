package cn.huang.core.wrapper.delete;

import cn.huang.core.wrapper.AbstractWrapper;
import com.influxdb.annotations.Measurement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * @author huangl
 * @description LambdaDeleteWrapper
 * @date 2023/10/12 11:37:58
 */
public class LambdaDeleteWrapper<T> extends AbstractWrapper<T> implements LambdaDelete<T> {
    private OffsetDateTime start;
    private OffsetDateTime end;

    public OffsetDateTime getStart() {
        if (Objects.isNull(start))
            return OffsetDateTime.of(LocalDate.now(), LocalTime.MIN, ZoneOffset.UTC);
        return start;
    }

    public OffsetDateTime getEnd() {
        if (Objects.isNull(end))
            return OffsetDateTime.of(LocalDate.now(), LocalTime.MAX, ZoneOffset.UTC);
        return end;
    }

    public LambdaDeleteWrapper(Class<T> entityClass) {
        super(entityClass);
        String measurement = this.getEntityClass().getAnnotation(Measurement.class).name();
        String MEASUREMENT;
        if (measurement != null && !measurement.equals("")) {
            MEASUREMENT = measurement;
        } else {
            throw new RuntimeException("[influxdb2] ERROR : @Measurement must be annotated on entity class");
        }
        FLUX.append(String.format("_measurement=\"%s\"", MEASUREMENT));
    }

    @Override
    public LambdaDeleteWrapper<T> range(OffsetDateTime start, OffsetDateTime end) {
        this.start = start;
        this.end = end;
        return this;
    }
}
