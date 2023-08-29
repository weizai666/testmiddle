package com.hyts.assemble.topology.factory.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.graph.MutableNetwork;
import com.hyts.assemble.topology.factory.TopologyGraphAdapter;
import com.hyts.assemble.topology.factory.TopologyGraphPainter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.topology.factory.impl
 * @author:LiBo/Alex
 * @create-date:2022-06-16 22:16
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: json 拓扑画图的适配器
 */
public class JSONTopologyGraphAdapter implements TopologyGraphAdapter<Map<String,Object>, MutableNetwork> {

    @Override
    public MutableNetwork transfer(Map<String,Object> map) {
        TopologyGraphPainter<Object,Object> topologyGraphPainter = new TopologyGraphPainter();
        map.entrySet().stream().forEach(param->{
            topologyGraphPainter.addNode(param.getKey());
            JSONObject nextJoiner = JSONObject.parseObject(param.getValue().toString());
            JSONArray nextNodes = nextJoiner.getJSONArray(NEXT_NODE_IDS);
            nextNodes.stream().forEach(param2->{
                nextJoiner.put("line",param.getKey()+"->"+param2);
                topologyGraphPainter.joinLine(nextJoiner,param2.toString());
            });
        });
        return topologyGraphPainter.getNetworkTopology();
    }
}
