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

import com.google.common.base.Preconditions;
import com.google.common.graph.MutableNetwork;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.Set;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.topology.factory
 * @author:LiBo/Alex
 * @create-date:2022-06-14 22:13
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class TopologyGraphPainter<NODE,LINE>{

    /**
     * 拓扑模型对象
     */
    @Getter
    private MutableNetwork<NODE,LINE> networkTopology;

    /**
     * 创建节点处理操作
     */
    private volatile boolean havingHeadNode;

    /**
     * 当前绘画节点
     */
    private volatile NODE currentNode;

    /**
     * 当前绘画线路
     */
    private volatile LINE currentLine;

    /**
     * 初始化
     * @param networkTopology
     */
    public TopologyGraphPainter(MutableNetwork<NODE, LINE> networkTopology) {
        this.networkTopology = networkTopology;
        initTopology();
    }


    /**
     * 初始化
     */
    public TopologyGraphPainter() {
        initTopology();
    }

    /**
     * 初始化拓扑
     * @return
     */
    private TopologyGraphPainter initTopology(){
        networkTopology = Optional.ofNullable(networkTopology).orElseGet(()->TopologyFactory.createMutableNetWork());
        havingHeadNode = Boolean.TRUE;
        return this;
    }

    /**
     * 添加节点
     * @param node
     * @return
     */
    public TopologyGraphPainter addNode(NODE node){
        networkTopology.addNode(node);
        currentNode = node;
        return this;
    }

    /**
     * 添加节点
     * @param node
     * @return
     */
    public TopologyGraphPainter addNode(Set<NODE> node){
        node.stream().forEach(param->{
            addNode(node);
        });
        return this;
    }

    /**
     * 链接线
     * @param preNode
     * @param value
     * @param nextNode
     * @return
     */
    public TopologyGraphPainter joinLine(NODE preNode, LINE value, NODE nextNode){
        Preconditions.checkArgument(havingHeadNode,"连接线加入失败，因为还没有头结点","");
        networkTopology.addEdge(preNode,nextNode,value);
        currentLine = value;
        return this;
    }

    /**
     * 链接线
     * @param value
     * @param nextNode
     * @return
     */
    public TopologyGraphPainter joinLine(LINE value, NODE nextNode){
        Preconditions.checkArgument(havingHeadNode,"连接线加入失败，因为还没有头结点","");
        networkTopology.addEdge(currentNode,nextNode,value);
        currentLine = value;
        return this;
    }

    /**
     * 链接线
     * @param nextNode
     * @return
     */
    public TopologyGraphPainter joinLine(NODE nextNode){
        Preconditions.checkArgument(havingHeadNode,"连接线加入失败，因为还没有头结点","");
        Preconditions.checkNotNull(currentNode,"连接线加入失败，因为还没有头结点","");
        Preconditions.checkNotNull(currentLine,"连接线加入失败，因为还没有初始化任何线","");
        networkTopology.addEdge(currentNode,nextNode,currentLine);
        return this;
    }

    /**
     * 移除相关的节点
     * @param node
     * @return
     */
    public TopologyGraphPainter removeNode(NODE node){
        networkTopology.removeNode(node);
        return this;
    }

    /**
     * 移除相关的限流
     * @param line
     * @return
     */
    public TopologyGraphPainter removeLine(LINE line){
        networkTopology.removeEdge(line);
        return this;
    }

}
