package com.huang.core.config;

import cn.huang.core.config.InfluxWrapper;

/**
 * @author huangl
 * @description InfluxWrapperFactory
 * @date 2023/10/13 14:40:14
 */
public class InfluxWrapperFactory {

    public static InfluxWrapper getInfluxWrapper() {
        return InfluxWrapper.builder("http://127.0.0.1:8086", "ez*8TxjA6l%98k", "Huang", "face_platform")
                .logEnabled(true)
                .build();
    }

}
