package com.hyts.assemble.dynamicTask.serv.impl;

import com.hyts.assemble.dynamicTask.client.AdminBiz;
import com.hyts.assemble.dynamicTask.domain.HandleCallbackParam;
import com.hyts.assemble.dynamicTask.domain.RegistryParam;
import com.hyts.assemble.dynamicTask.domain.ReturnT;
import com.hyts.assemble.dynamicTask.engine.component.JobCompleteHelper;
import com.hyts.assemble.dynamicTask.engine.component.JobRegistryHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {


    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        return JobCompleteHelper.getInstance().callback(callbackParamList);
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        return JobRegistryHelper.getInstance().registry(registryParam);
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        return JobRegistryHelper.getInstance().registryRemove(registryParam);
    }

}
