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

import cn.hutool.core.util.NumberUtil;
import com.google.common.base.Preconditions;
import com.google.common.graph.Graph;
import com.google.common.graph.MutableNetwork;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.topology.factory
 * @author:LiBo/Alex
 * @create-date:2022-06-14 22:13
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class TopologyGraphSelector<NODE,LINE>{

    /**
     * 拓扑模型对象
     */
    private final MutableNetwork<NODE,LINE> networkTopology;

    /**
     * 拓扑名称/编码
     */
    private final String TopologyCode;

    public TopologyGraphSelector(MutableNetwork<NODE, LINE> networkTopology, String topologyCode) {
        this.networkTopology = networkTopology;
        TopologyCode = topologyCode;
    }

    /**
     * 起始节点集合
     * @return
     */
    public Set<NODE> getStartNodes(){
        Set<NODE> startNodes = networkTopology.nodes()
                .stream().filter(param->networkTopology.inDegree(param) == 0).collect(Collectors.toSet());
        return startNodes;
    }

    /**
     * 获取节点集合
     * @return
     */
    public Set<NODE> getNodes(){
        return networkTopology.nodes();
    }

    /**
     * 获取线路集合
     * @return
     */
    public Set<LINE> getLines(){
        return networkTopology.edges();
    }

    /**
     * 所有关联的节点
     * @param node
     * @return
     */
    public Set<NODE> crossNodes(NODE node){
        return networkTopology.adjacentNodes(node);
    }

    /**
     * 所有关联的线路
     * @param line
     * @return
     */
    public Set<LINE> crossLines(LINE line){
        return networkTopology.adjacentEdges(line);
    }

    /**
     * 所有关联的线路
     * @param node
     * @return
     */
    public Set<LINE> relateLines(NODE node){
        return networkTopology.incidentEdges(node);
    }

    /**
     * 入度+出度
     * @param node
     * @return
     */
    public int crossLineNumber(NODE node){
        return networkTopology.degree(node);
    }

    /**
     * 入度+出度
     * @param nodes
     * @return
     */
    public int crossLineNumber(Set<NODE> nodes){
        return nodes.stream().mapToInt(param -> crossLineNumber(param)).sum();
    }

    /**
     * 转换为入度+出度数量
     * @param nodes
     * @return
     */
    public List<Integer> toCrossLineNumber(Set<NODE> nodes){
        return nodes.stream().map(param->crossLineNumber(param)).collect(Collectors.toList());
    }

    /**
     * 入度
     * @param node
     * @return
     */
    public int inLineNumber(NODE node){
        return networkTopology.inDegree(node);
    }

    /**
     * 入度
     * @param nodes
     * @return
     */
    public int inLineNumber(Set<NODE> nodes){
        return nodes.stream().mapToInt(param -> inLineNumber(param)).sum();
    }

    /**
     * 转换为入度数量
     * @param nodes
     * @return
     */
    public List<Integer> toInLineNumber(Set<NODE> nodes){
        return nodes.stream().map(param->inLineNumber(param)).collect(Collectors.toList());
    }

    /**
     * 输出的出度
     * @param node
     * @return
     */
    public int outLineNumber(NODE node){
        return networkTopology.outDegree(node);
    }

    /**
     * 输出的出度
     * @param nodes
     * @return
     */
    public int outLineNumber(Set<NODE> nodes){
        return nodes.stream().mapToInt(param -> outLineNumber(param)).sum();
    }

    /**
     * 转换为出度数量
     * @param nodes
     * @return
     */
    public List<Integer> toOutLineNumber(Set<NODE> nodes){
        return nodes.stream().map(param->outLineNumber(param)).collect(Collectors.toList());
    }

    /**
     * 获取前置节点
     * @param node
     * @return
     */
    public Set<NODE> getPreNodes(NODE node){
        Set<NODE> preNodes = networkTopology.predecessors(node);
        return preNodes;
    }

    /**
     * 获取前置节点
     * @param nodes
     * @return
     */
    public Set<NODE> getPreNodes(Set<NODE> nodes){
        Preconditions.checkNotNull(nodes,"前置节点的节点数据不允许为空!");
        Set<NODE> preNodes = nodes.stream().
                flatMap(param->getPreNodes(param).stream()).
                collect(Collectors.toSet());
        return preNodes;
    }

    /**
     * 获取后置节点
     * @param node
     * @return
     */
    public Set<NODE> getNextNodes(NODE node){
        Set<NODE> nextNodes = networkTopology.successors(node);
        return nextNodes;
    }

    /**
     * 获取后置节点
     * @param nodes
     * @return
     */
    public Set<NODE> getNextNodes(Set<NODE> nodes){
        Set<NODE> nextNodes = nodes.stream().
                flatMap(param->getNextNodes(param).stream()).
                collect(Collectors.toSet());
        return nextNodes;
    }

    /**
     * 获取相关的graph图谱
     * @return
     */
    public Graph<NODE> getGraph(){
       return networkTopology.asGraph();
    }

    /**
     * 获取两者之间的线路
     * @param PRE
     * @param NEXT
     * @return
     */
    public LINE line(NODE PRE,NODE NEXT){
        return networkTopology.edgeConnecting(PRE,NEXT).get();
    }

    /**
     * 获取两者之间的线路
     * @param PRE
     * @param NEXT
     * @return
     */
    public Set<LINE> lines(NODE PRE,NODE NEXT){
        return networkTopology.edgesConnecting(PRE,NEXT);
    }

}
