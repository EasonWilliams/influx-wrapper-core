package com.huang.core.service.impl;

import cn.huang.core.config.InfluxWrapper;
import cn.huang.core.service.impl.ServiceImpl;
import com.huang.core.pojo.Alert;
import com.huang.core.service.IAlertService;

/**
 * @author huangl
 * @description AlertServiceImpl
 * @date 2023/10/13 14:37:34
 */
public class AlertServiceImpl extends ServiceImpl<Alert> implements IAlertService {

    public AlertServiceImpl(InfluxWrapper influxWrapper) {
        super(influxWrapper);
    }

}
