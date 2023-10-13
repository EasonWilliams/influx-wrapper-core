package com.huang.core;

import cn.huang.core.Wrappers;
import com.huang.core.config.InfluxWrapperFactory;
import com.huang.core.pojo.Alert;
import com.huang.core.service.impl.AlertServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author huangl
 * @description FluxTest
 * @date 2023/10/13 14:34:41
 */
public class FluxTest {

    public static void main(String[] args) {
        AlertServiceImpl alertService = new AlertServiceImpl(InfluxWrapperFactory.getInfluxWrapper());
        alertService.queryList(Wrappers.lambdaQuery(Alert.class)
                .range(LocalDateTime.parse("2023-10-08T14:00:00").atZone(ZoneOffset.UTC).toInstant(), LocalDateTime.parse("2023-10-13T14:30:00").atZone(ZoneOffset.UTC).toInstant())
                .pivot()
        );
    }

}
