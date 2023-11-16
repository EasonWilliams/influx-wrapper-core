package cn.huang.core.service.impl;

import cn.huang.core.config.InfluxWrapper;
import cn.huang.core.result.PageResult;
import cn.huang.core.service.IService;
import cn.huang.core.util.JsonUtils;
import cn.huang.core.wrapper.delete.LambdaDeleteWrapper;
import cn.huang.core.wrapper.query.LambdaQueryWrapper;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxTable;

import java.util.*;
import java.util.logging.Logger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author huangl
 * @description ServiceImpl
 * @date 2023/9/27 17:55:40
 */
public abstract class ServiceImpl<T> implements IService<T> {
    private static final Logger log = Logger.getLogger(ServiceImpl.class.getName());
    private static InfluxWrapper influxWrapper;

    public ServiceImpl(InfluxWrapper influxWrapper) {
        if (Objects.isNull(influxWrapper))
            throw new NullPointerException("【influx wrapper】influxWrapper is null");
        if (Objects.isNull(influxWrapper.getClient()))
            throw new NullPointerException("【influx wrapper】influxWrapper client is null");
        ServiceImpl.influxWrapper = influxWrapper;
    }

    @Override
    public List<T> queryList(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        lambdaQueryWrapper.FLUX.insert(0, String.format("from(bucket: \"%s\")", influxWrapper.getBucket()));
        if (influxWrapper.getLogEnabled())
            log.info(String.format("【influx wrapper】\n----QueryFlux：\n%s", lambdaQueryWrapper.FLUX.toString()));
        List<T> tables = influxWrapper.getClient().getQueryApi().query(lambdaQueryWrapper.FLUX.toString(), lambdaQueryWrapper.getEntityClass());
        if (influxWrapper.getLogEnabled())
            log.info(String.format("【influx wrapper】\n----QueryTables：\n%s", JsonUtils.arrToJsonStr(tables)));
        return tables;
    }

    @Override
    public PageResult<T> queryPage(Long current, Long size, LambdaQueryWrapper<T> lambdaQueryWrapper) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(this.queryCount(lambdaQueryWrapper.copy()));
        Long offset = (current - 1) * size;
        List<T> records = this.queryList(lambdaQueryWrapper.copy().limit(size, offset));
        if (records != null && records.size() > 0)
            result.setRecords(records);
        result.setCurrent(current);
        result.setSize(size);
        return result;
    }

    @Override
    public Long queryCount(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        lambdaQueryWrapper.FLUX.insert(0, String.format("from(bucket: \"%s\")", influxWrapper.getBucket()));
        lambdaQueryWrapper.group(Collections.singletonList("_measurement")).count();
        if (influxWrapper.getLogEnabled())
            log.info(String.format("【influx wrapper】\n----QueryFlux：\n%s", lambdaQueryWrapper.FLUX.toString()));
        List<FluxTable> tables = influxWrapper.getClient().getQueryApi().query(lambdaQueryWrapper.FLUX.toString());
        AtomicLong count = new AtomicLong();
        tables.forEach(table -> table.getRecords().forEach(record -> {
            Map<String, Object> values = record.getValues();
            values.forEach((key, value) -> {
                if (key.equals("_value"))
                    count.set(Long.parseLong(value.toString()));
            });
        }));
        if (influxWrapper.getLogEnabled())
            log.info(String.format("【influx wrapper】\n----QueryCounts：%s", count.get()));
        return count.get();
    }

    @Override
    public void insert(T entity) {
        WriteApiBlocking blocking = influxWrapper.getClient().getWriteApiBlocking();
        if (influxWrapper.getLogEnabled())
            log.info(String.format("【influx wrapper】\n----Insert：\n%s", JsonUtils.objToJsonStr(entity)));
        blocking.writeMeasurement(influxWrapper.getBucket(), influxWrapper.getOrg(), WritePrecision.NS, entity);
    }

    @Override
    public void insertBatch(List<T> entityList) {
        WriteApiBlocking blocking = influxWrapper.getClient().getWriteApiBlocking();
        if (influxWrapper.getLogEnabled())
            log.info(String.format("【influx wrapper】\n----InsertBatch：\n%s", JsonUtils.arrToJsonStr(entityList)));
        blocking.writeMeasurements(influxWrapper.getBucket(), influxWrapper.getOrg(), WritePrecision.NS, entityList);
    }

    @Override
    public void delete(LambdaDeleteWrapper<T> lambdaDeleteWrapper) {
        if (influxWrapper.getLogEnabled())
            log.info(String.format("【influx wrapper】\n----Delete：\nstart：%s\nend：%s\nflux：%s",
                    lambdaDeleteWrapper.getStart(), lambdaDeleteWrapper.getEnd(), lambdaDeleteWrapper.FLUX.toString()));
        influxWrapper.getClient().getDeleteApi().delete(lambdaDeleteWrapper.getStart(),
                lambdaDeleteWrapper.getEnd(),
                lambdaDeleteWrapper.FLUX.toString(),
                influxWrapper.getBucket(),
                influxWrapper.getOrg());
    }
}
