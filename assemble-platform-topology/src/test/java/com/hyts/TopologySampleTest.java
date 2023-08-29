/**
 * Copyright [2020] [LiBo/Alex of copyright liboware@gmail.com ]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyts;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.graph.MutableNetwork;
import com.hyts.assemble.topology.factory.TopologyFactory;
import com.hyts.assemble.topology.factory.TopologyGraphPainter;
import com.hyts.assemble.topology.factory.TopologyGraphSelector;
import com.hyts.assemble.topology.factory.impl.JSONTopologyGraphAdapter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.hyts.assemble.topology.factory.TopologyFactory.createMutableNetWork;
import static com.hyts.assemble.topology.factory.TopologyGraphAdapter.NEXT_NODE_IDS;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.topology.factory
 * @author:LiBo/Alex
 * @create-date:2022-06-14 23:53
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class TopologySampleTest {

    @Test
    public void test1() {
        MutableNetwork<String,String> mutableNetwork = createMutableNetWork();
        mutableNetwork.addEdge("a","b","a->b");
        mutableNetwork.addEdge("a","c","a->c");
        mutableNetwork.addEdge("b","d","b->d");
        System.out.println(mutableNetwork.adjacentEdges("a->c").size());
        System.out.println(mutableNetwork.successors("a"));
        System.out.println(mutableNetwork.inEdges("c"));
        System.out.println(mutableNetwork.outEdges("b"));
        System.out.println(mutableNetwork.hasEdgeConnecting("a","d"));
        System.out.println(mutableNetwork.hasEdgeConnecting("a","b"));
        System.out.println(mutableNetwork.predecessors("d"));
        System.out.println(mutableNetwork.nodes());
        System.out.println(mutableNetwork.edges());
        System.out.println(mutableNetwork.successors("a"));
        System.out.println(mutableNetwork.outEdges("a"));
    }

    @Test
    public void test2() {
        TopologyGraphPainter<String,String> topologyGraphPainter = new TopologyGraphPainter();
        topologyGraphPainter.
                addNode("start Node").
                joinLine("first line","second node").
                joinLine("second line","third node");
        MutableNetwork<String,String> mutableNetwork = topologyGraphPainter.getNetworkTopology();
        TopologyGraphSelector<String,String> topologyGraphSelector = new TopologyGraphSelector(mutableNetwork,"testTopology");
        System.out.println(topologyGraphSelector.getNodes());
        System.out.println(topologyGraphSelector.getLines());
        System.out.println(topologyGraphSelector.getGraph());
        System.out.println(topologyGraphSelector.getNextNodes("second node"));

        System.out.println(mutableNetwork);
    }


    @Test
    public void test3() {

        TopologyGraphPainter<String,String> topologyGraphPainter = new TopologyGraphPainter();

        Map<String,Object> objectMap = new HashMap<>();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("second node");
        jsonArray.add("third node");
        jsonObject.put(NEXT_NODE_IDS,jsonArray);
        objectMap.put("start Node",jsonObject);

        JSONObject jsonObject2 = new JSONObject();
        JSONArray jsonArray2 = new JSONArray();
        jsonArray2.add("fouth node");
        jsonArray2.add("fith node");
        jsonObject2.put(NEXT_NODE_IDS,jsonArray2);
        objectMap.put("second node",jsonObject2);

        JSONTopologyGraphAdapter jsonTopologyGraphAdapter = new JSONTopologyGraphAdapter();

        MutableNetwork mutableNetwork = jsonTopologyGraphAdapter.transfer(objectMap);

        TopologyGraphSelector topologyGraphSelector = new TopologyGraphSelector(mutableNetwork,"test");

        System.out.println(topologyGraphSelector.getNextNodes("start Node"));
        System.out.println(topologyGraphSelector.getNextNodes("second node"));
        System.out.println(topologyGraphSelector.getPreNodes("start Node"));
        System.out.println(topologyGraphSelector.getPreNodes("second node"));
    }


    @Test
    public void test4() {

        try{
            System.out.println("1");

        }finally {
            System.out.println("2");
        }
        try{
            System.out.println("3");

        }finally {
            System.out.println("4");
        }
    }

}
