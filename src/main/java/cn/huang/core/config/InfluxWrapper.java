package cn.huang.core.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

import java.util.Objects;

/**
 * @author huangl
 * @description InfluxWrapper
 * @date 2023/10/13 12:53:30
 */
public class InfluxWrapper {
    private final InfluxDBClient client;
    private final String org;
    private final String bucket;
    private Boolean logEnabled;

    public InfluxDBClient getClient() {
        return client;
    }

    public String getOrg() {
        return org;
    }

    public String getBucket() {
        return bucket;
    }

    public Boolean getLogEnabled() {
        return logEnabled;
    }

    public static InfluxWrapperBuilder builder(String endpoint, String token, String org, String bucket) {
        return new InfluxWrapperBuilder(endpoint, token, org, bucket);
    }

    public InfluxWrapper(String endpoint, String token, String org, String bucket) {
        this.org = org;
        this.bucket = bucket;
        this.client = InfluxDBClientFactory.create(endpoint, token.toCharArray(), org, bucket);
    }

    public static class InfluxWrapperBuilder {
        private final String endpoint;
        private final String token;
        private final String org;
        private final String bucket;
        private Boolean logEnabled;

        public InfluxWrapperBuilder(String endpoint, String token, String org, String bucket) {
            this.endpoint = endpoint;
            this.token = token;
            this.org = org;
            this.bucket = bucket;
        }

        public InfluxWrapperBuilder logEnabled(Boolean logEnabled) {
            this.logEnabled = logEnabled;
            return this;
        }

        public InfluxWrapper build() {
            InfluxWrapper influxWrapper = new InfluxWrapper(endpoint, token, org, bucket);
            influxWrapper.logEnabled = Objects.nonNull(logEnabled) && logEnabled;
            return influxWrapper;
        }

    }
}
