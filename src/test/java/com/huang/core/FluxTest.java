package com.huang.core;

import com.huang.core.config.InfluxWrapperFactory;
import com.huang.core.pojo.Alert;
import com.huang.core.service.impl.AlertServiceImpl;

/**
 * @author huangl
 * @description FluxTest
 * @date 2023/10/13 14:34:41
 */
public class FluxTest {

    public static void main(String[] args) {
        AlertServiceImpl alertService = new AlertServiceImpl(InfluxWrapperFactory.getInfluxWrapper());
        Alert alert = new Alert();
        alert.setPid("1");
        alert.setName("huangl");
        alert.setHeight(1.8);
        alert.setWeight(70.0);
        alertService.insert(alert);
    }

}
