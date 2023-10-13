package cn.huang.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huangl
 * @description InfluxDBConfigurationProperties
 * @date 2023/10/12 18:30:22
 */
@ConfigurationProperties(prefix = "spring.influx")
public class InfluxDBConfigurationProperties {
    private String endpoint;
    private String org;
    private String orgId;
    private String bucket;
    private String token;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}