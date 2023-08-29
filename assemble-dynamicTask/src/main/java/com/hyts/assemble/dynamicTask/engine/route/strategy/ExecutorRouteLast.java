package com.hyts.assemble.dynamicTask.engine.route.strategy;

import com.hyts.assemble.dynamicTask.domain.ReturnT;
import com.hyts.assemble.dynamicTask.domain.TriggerParam;
import com.hyts.assemble.dynamicTask.engine.route.ExecutorRouter;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<String>(addressList.get(addressList.size()-1));
    }

}
