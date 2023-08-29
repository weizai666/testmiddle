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

import com.google.common.graph.*;

import java.util.Comparator;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.topology.factory
 * @author:LiBo/Alex
 * @create-date:2022-06-14 21:13
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class TopologyFactory {

    /**
     * 创建拓扑
     * @param isCycle
     * @param nodeNumber
     * @param comparable
     * @param <NODE>
     * @param <VALUE>
     * @return
     */
    public static <NODE,VALUE> MutableValueGraph createMutableValueGraph(boolean isCycle, int nodeNumber, Comparator comparable){
        return ValueGraphBuilder.directed().allowsSelfLoops(isCycle)
                .nodeOrder(ElementOrder.sorted(comparable)).build();
    }

    /**
     * 创建拓扑
     * @param nodeNumber
     * @param comparable
     * @param <NODE>
     * @param <VALUE>
     * @return
     */
    public static <NODE,VALUE> MutableValueGraph createMutableValueGraph( int nodeNumber, Comparator comparable){
        return createMutableValueGraph(Boolean.TRUE,nodeNumber,comparable);
    }

    /**
     * 创建拓扑
     * @param comparable 排序比较器
     * @param <NODE> 节点
     * @param <VALUE> 数值
     * @return
     */
    public static <NODE,VALUE> MutableValueGraph createMutableValueGraph(Comparator comparable){
        return createMutableValueGraph(10000,comparable);
    }

    /**
     * 创建拓扑
     * @param <NODE>
     * @param <VALUE>
     * @return
     */
    public static <NODE,VALUE> MutableValueGraph createMutableValueGraph(){
        return createMutableValueGraph(Comparator.naturalOrder());
    }

    /**
     * 创建拓扑
     * @param isCycle
     * @param nodeNumber
     * @param comparable
     * @param <NODE>
     * @param <VALUE>
     * @return
     */
    public static <NODE,VALUE> MutableGraph createMutableGraph(boolean isCycle, int nodeNumber, Comparator comparable){
        return GraphBuilder.directed().allowsSelfLoops(isCycle)
                .nodeOrder(ElementOrder.sorted(comparable)).build();
    }

    /**
     * 创建拓扑
     * @param nodeNumber
     * @param comparable
     * @param <NODE>
     * @param <VALUE>
     * @return
     */
    public static <NODE,VALUE> MutableGraph createMutableGraph(int nodeNumber, Comparator comparable){
        return createMutableGraph(Boolean.TRUE,nodeNumber,comparable);
    }

    /**
     * 创建拓扑
     * @param comparable 排序比较器
     * @param <NODE> 节点
     * @param <VALUE> 数值
     * @return
     */

    public static <NODE,VALUE> MutableGraph createMutableGraph(Comparator comparable){
        return createMutableGraph(10000,comparable);
    }

    /**
     * 创建拓扑
     * @param <NODE>
     * @param <VALUE>
     * @return
     */
    public static <NODE,VALUE> MutableGraph createMutableGraph(){
        return createMutableGraph(Comparator.naturalOrder());
    }


    /**
     * 创建拓扑
     * @param isCycle
     * @param nodeNumber
     * @param comparable
     * @param <NODE>
     * @param <VALUE>
     * @return
     */
    public static <NODE,VALUE> MutableNetwork createMutableNetWork(boolean isCycle, int nodeNumber,int lineNumber, Comparator comparable){
        return NetworkBuilder.directed()
                .allowsParallelEdges(true)
                .nodeOrder(ElementOrder.sorted(comparable))
                .expectedNodeCount(nodeNumber)
                .expectedEdgeCount(lineNumber)
                .build();
    }

    /**
     * 创建拓扑
     * @param comparable
     * @param <NODE>
     * @param <VALUE>
     * @return
     */
    public static <NODE,VALUE> MutableNetwork createMutableNetWork(Comparator comparable){
        return createMutableNetWork(Boolean.TRUE,10000,10000,comparable);
    }


    /**
     * 创建拓扑
     * @param <NODE> 节点
     * @param <VALUE> 数值
     * @return
     */
    public static <NODE,VALUE> MutableNetwork createMutableNetWork(){
        return createMutableNetWork(Comparator.naturalOrder());
    }

}
