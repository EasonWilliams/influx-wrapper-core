package cn.huang.core.service.impl;

import cn.huang.core.config.InfluxDBConfigurationProperties;
import cn.huang.core.result.PageResult;
import cn.huang.core.service.IService;
import cn.huang.core.wrapper.delete.LambdaDeleteWrapper;
import cn.huang.core.wrapper.query.LambdaQueryWrapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Logger;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author huangl
 * @description ServiceImpl
 * @date 2023/9/27 17:55:40
 */
public abstract class ServiceImpl<T> implements IService<T> {
    @Value("${spring.influx.log:false}")
    private Boolean logEnable;
    private static final Logger log = Logger.getLogger(ServiceImpl.class.getName());
    @Resource
    private InfluxDBClient client;
    @Resource
    private InfluxDBConfigurationProperties properties;

    @Override
    public List<T> queryList(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        lambdaQueryWrapper.FLUX.insert(0, String.format("from(bucket: \"%s\")", properties.getBucket()));
        var tables = client.getQueryApi().query(lambdaQueryWrapper.FLUX.toString(), lambdaQueryWrapper.getEntityClass());
        if (logEnable)
            log.info(String.format("【influx wrapper】\n----QueryFlux：\n%s\n----QueryTables：\n%s", lambdaQueryWrapper.FLUX.toString(), tables));
        return tables;
    }

    @Override
    public PageResult<T> queryPage(Long current, Long size, LambdaQueryWrapper<T> lambdaQueryWrapper) {
        var result = new PageResult<T>();
        result.setTotal(this.queryCount(lambdaQueryWrapper.copy()));
        var offset = (current - 1) * size;
        var records = this.queryList(lambdaQueryWrapper.copy().limit(size, offset));
        Optional.of(records)
                .ifPresentOrElse(recordList -> result.setRecords(records), () -> result.setRecords(null));
        result.setCurrent(current);
        result.setSize(size);
        return result;
    }

    @Override
    public Long queryCount(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        lambdaQueryWrapper.FLUX.insert(0, String.format("from(bucket: \"%s\")", properties.getBucket()));
        lambdaQueryWrapper.group(List.of("_measurement")).count();
        var tables = client.getQueryApi().query(lambdaQueryWrapper.FLUX.toString());
        var count = new AtomicLong();
        tables.forEach(table -> table.getRecords().forEach(record -> {
            var values = record.getValues();
            values.forEach((key, value) -> {
                if (key.equals("_value"))
                    count.set(Long.parseLong(value.toString()));
            });
        }));
        if (logEnable)
            log.info(String.format("【influx wrapper】\n----QueryFlux：\n%s\n----QueryCounts：%s", lambdaQueryWrapper.FLUX.toString(), count.get()));
        return count.get();
    }

    @Override
    public void insert(T entity) {
        var blocking = client.getWriteApiBlocking();
        blocking.writeMeasurement(properties.getBucket(), properties.getOrg(), WritePrecision.NS, entity);
        if (logEnable)
            log.info(String.format("【influx wrapper】\n----Insert：\n%s", entity));
    }

    @Override
    public void insertBatch(List<T> entityList) {
        var blocking = client.getWriteApiBlocking();
        blocking.writeMeasurements(properties.getBucket(), properties.getOrg(), WritePrecision.NS, entityList);
        if (logEnable)
            log.info(String.format("【influx wrapper】\n----InsertBatch：\n%s", entityList));
    }

    @Override
    public void delete(LambdaDeleteWrapper<T> lambdaDeleteWrapper) {
        client.getDeleteApi().delete(lambdaDeleteWrapper.getStart(),
                lambdaDeleteWrapper.getEnd(),
                lambdaDeleteWrapper.FLUX.toString(),
                properties.getBucket(),
                properties.getOrg());
        if (logEnable)
            log.info(String.format("【influx wrapper】\n----Delete：\nstart：%s\nend：%s\nflux：%s",
                    lambdaDeleteWrapper.getStart(), lambdaDeleteWrapper.getEnd(), lambdaDeleteWrapper.FLUX.toString()));
    }
}
