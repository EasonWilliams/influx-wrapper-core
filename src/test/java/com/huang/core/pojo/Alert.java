package com.huang.core.pojo;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

/**
 * @author huangl
 * @description AlertInfo
 * @date 2023/10/13 14:36:26
 */
@Measurement(name = "info")
public class Alert {
    @Column(timestamp = true, name = "_time")
    private Instant timestamp;

    @Column(tag = true, name = "pid")
    private String pid;

    @Column(tag = true, name = "name")
    private String name;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "timestamp=" + timestamp +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
}
