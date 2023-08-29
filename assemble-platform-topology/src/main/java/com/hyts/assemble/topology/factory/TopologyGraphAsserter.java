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
package com.hyts.assemble.topology.factory;

import com.google.common.graph.MutableNetwork;
import lombok.Getter;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.topology.factory
 * @author:LiBo/Alex
 * @create-date:2022-06-14 23:42
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 拓扑断言操作
 */
public class TopologyGraphAsserter<NODE,LINE> {

    /**
     * 拓扑模型对象
     */
    @Getter
    private MutableNetwork<NODE,LINE> networkTopology;

    /**
     * 拓扑断言操作
     * @param networkTopology
     */
    public TopologyGraphAsserter(MutableNetwork<NODE, LINE> networkTopology) {
        this.networkTopology = networkTopology;
    }

    /**
     * 是否链接
     * @param PRE
     * @param NEXT
     * @return
     */
    public boolean isConnected(NODE PRE,NODE NEXT){
        return networkTopology.hasEdgeConnecting(PRE,NEXT);
    }

    /**
     * 是否包含输出的Node
     * @return
     */
    public boolean containOutNode(NODE node,NODE targetNode){
        return networkTopology.successors(node).stream().anyMatch(param->param.equals(targetNode));
    }

    /**
     * 是否包含输出的线
     * @param node
     * @param targetLine
     * @return
     */
    public boolean containOutLine(NODE node,LINE targetLine){
        return networkTopology.outEdges(node).stream().anyMatch(param->param.equals(targetLine));
    }

    /**
     *
     * @param node
     * @param targetLine
     * @return
     */
    public boolean containInLine(NODE node,LINE targetLine){
        return networkTopology.inEdges(node).stream().anyMatch(param->param.equals(targetLine));
    }

    /**
     *
     * @param node
     * @param targetLine
     * @return
     */
    public boolean containCrossLine(NODE node,LINE targetLine){
        return networkTopology.incidentEdges(node).stream().anyMatch(param->param.equals(targetLine));
    }

    /**
     * 是否允许并行线路
     * @return
     */
    public boolean allowParallelLine(){
       return networkTopology.allowsParallelEdges();
    }

    /**
     * 是否允许环路操作
     * @return
     */
    public boolean allowCycle(){
        return networkTopology.allowsSelfLoops();
    }

}
