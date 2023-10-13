package cn.huang.core.wrapper.delete;

import cn.huang.core.wrapper.AbstractWrapper;
import cn.hutool.core.util.ObjectUtil;
import com.influxdb.annotations.Measurement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * @author huangl
 * @description LambdaDeleteWrapper
 * @date 2023/10/12 11:37:58
 */
public class LambdaDeleteWrapper<T> extends AbstractWrapper<T> implements LambdaDelete<T> {
    private String MEASUREMENT;
    private OffsetDateTime start;
    private OffsetDateTime end;

    public OffsetDateTime getStart() {
        if (ObjectUtil.isEmpty(start))
            return OffsetDateTime.of(LocalDate.now(), LocalTime.MIN, ZoneOffset.UTC);
        return start;
    }

    public OffsetDateTime getEnd() {
        if (ObjectUtil.isEmpty(end))
            return OffsetDateTime.of(LocalDate.now(), LocalTime.MAX, ZoneOffset.UTC);
        return end;
    }

    public LambdaDeleteWrapper(Class<T> entityClass) {
        super(entityClass);
        Optional.ofNullable(this.getEntityClass().getAnnotation(Measurement.class).name())
                .ifPresentOrElse(
                        measurement -> MEASUREMENT = measurement,
                        () -> {
                            throw new RuntimeException("[influxdb2] ERROR : @Measurement must be annotated on entity class");
                        }
                );
        FLUX.append(String.format("_measurement=\"%s\"", MEASUREMENT));
    }

    @Override
    public LambdaDeleteWrapper<T> range(OffsetDateTime start, OffsetDateTime end) {
        this.start = start;
        this.end = end;
        return this;
    }
}
