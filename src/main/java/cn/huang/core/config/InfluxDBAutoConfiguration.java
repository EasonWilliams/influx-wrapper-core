package cn.huang.core.config;

import cn.hutool.core.collection.CollUtil;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.OrganizationsQuery;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangl
 * @description InFlux Auto Configuration
 * @date 2023/9/28 10:06:25
 */
@Configuration
@EnableConfigurationProperties(InfluxDBConfigurationProperties.class)
public class InfluxDBAutoConfiguration {
    @Resource
    private InfluxDBConfigurationProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public InfluxDBClient influxDBClient() {
        var client = InfluxDBClientFactory.create(properties.getEndpoint(),
                properties.getToken().toCharArray(),
                properties.getOrg(),
                properties.getBucket());
        this.setOrgId(client, properties);
        return client;
    }


    private void setOrgId(InfluxDBClient client, InfluxDBConfigurationProperties properties) {
        var query = new OrganizationsQuery();
        query.setOrg(properties.getOrg());
        var organizations = client.getOrganizationsApi().findOrganizations(query);
        if (CollUtil.isNotEmpty(organizations))
            properties.setOrgId(organizations.get(0).getId());
    }

}
