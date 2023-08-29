/**
 * Copyright [2019] [LiBo/Alex of copyright liboware@gmail.com ]
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

import java.util.*;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.topology.factory
 * @author:LiBo/Alex
 * @create-date:2022-06-21 19:27
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class SelfDefinitation {

    /**
     * 十字链表
     *
     * 针对有向图
     */
    public static class AcrossLinkedList{
        //数组部分
        NodeElement[] elements = new NodeElement[12];

        /**
         * 用于描述节点
         */
        public static class NodeElement{
            //以该节点为出点的弧的链表的头节点
            EdgeElement outerEdgeElement;
            //以该节点为入点的边的链表的头节点
            EdgeElement innerEdgeElement;
            //该节点中存储的数据
            Object data;
            //记录是否被软删除
            boolean whetherRemove;

            public NodeElement(EdgeElement outerEdgeElement, EdgeElement innerEdgeElement, Object data, boolean whetherRemove) {
                this.outerEdgeElement = outerEdgeElement;
                this.innerEdgeElement = innerEdgeElement;
                this.data = data;
                this.whetherRemove = whetherRemove;
            }
        }

        /**
         * 用于描述边
         */
        public static class EdgeElement{
            //从哪个节点出
            NodeElement outerNode;
            //到哪个节点入
            NodeElement innerElement;
            //出链表中下一个边节点:即下一个出节点相同的边
            EdgeElement outerNextEdge;
            //入链表中下一个边节点:即下一个入节点相同的边
            EdgeElement innerNextEdge;
            //记录是否被软删除
            boolean whetherRemove;

            public EdgeElement(NodeElement outerNode, NodeElement innerElement, EdgeElement outerNextEdge, EdgeElement innerNextEdge, boolean whetherRemove) {
                this.outerNode = outerNode;
                this.innerElement = innerElement;
                this.outerNextEdge = outerNextEdge;
                this.innerNextEdge = innerNextEdge;
                this.whetherRemove = whetherRemove;
            }
        }

        public static void main(String[] args) {
            AcrossLinkedList acrossLinkedList = new AcrossLinkedList();
            NodeElement c1 = new NodeElement(null, null, "C1",false);
            NodeElement c2 = new NodeElement(null, null, "C2",false);
            NodeElement c3 = new NodeElement(null, null, "C3",false);
            NodeElement c4 = new NodeElement(null, null, "C4",false);
            NodeElement c5 = new NodeElement(null, null, "C5",false);
            NodeElement c6 = new NodeElement(null, null, "C6",false);
            NodeElement c7 = new NodeElement(null, null, "C7",false);
            NodeElement c8 = new NodeElement(null, null, "C8",false);
            NodeElement c9 = new NodeElement(null, null, "C9",false);
            NodeElement c10 = new NodeElement(null, null, "C10",false);
            NodeElement c11 = new NodeElement(null, null, "C11",false);
            NodeElement c12 = new NodeElement(null, null, "C12",false);


            EdgeElement c1_c4 = new EdgeElement(c1, c4, null, null,false);
            EdgeElement c1_c2 = new EdgeElement(c1, c2, c1_c4, null,false);
            EdgeElement c1_c3 = new EdgeElement(c1, c3, c1_c2, null,false);
            EdgeElement c1_c12 = new EdgeElement(c1, c12, c1_c3, null,false);
            EdgeElement c9_c12 = new EdgeElement(c9, c12, null, c1_c12,false);
            EdgeElement c9_c10 = new EdgeElement(c9, c10, c9_c12, null,false);
            EdgeElement c9_c11 = new EdgeElement(c9, c11, c9_c10, null,false);

            EdgeElement c4_c5 = new EdgeElement(c4, c5, null, null,false);
            EdgeElement c2_c3 = new EdgeElement(c2, c3, null, c1_c3,false);
            EdgeElement c10_c12 = new EdgeElement(c10, c12, null, c9_c12,false);
            EdgeElement c11_c6 = new EdgeElement(c11, c6, null, null,false);
            EdgeElement c3_c5 = new EdgeElement(c3, c5, null, c4_c5,false);
            EdgeElement c3_c7 = new EdgeElement(c3, c7, c3_c5, null,false);
            EdgeElement c3_c8 = new EdgeElement(c3, c8, c3_c7, null,false);
            EdgeElement c6_c8 = new EdgeElement(c6, c8, null, c3_c8,false);
            EdgeElement c5_c7 = new EdgeElement(c5, c7, null, c3_c7,false);

            c1.innerEdgeElement=null;
            c2.innerEdgeElement=c1_c2;
            c3.innerEdgeElement=c2_c3;
            c4.innerEdgeElement=c1_c4;
            c5.innerEdgeElement=c4_c5;
            c6.innerEdgeElement=c11_c6;
            c7.innerEdgeElement=c5_c7;
            c8.innerEdgeElement=c6_c8;
            c9.innerEdgeElement=null;
            c10.innerEdgeElement=c9_c10;
            c11.innerEdgeElement=c9_c11;
            c12.innerEdgeElement=c9_c12;

            c1.outerEdgeElement=c1_c12;
            c2.outerEdgeElement=c2_c3;
            c3.outerEdgeElement=c3_c8;
            c4.outerEdgeElement=c4_c5;
            c5.outerEdgeElement=c5_c7;
            c6.outerEdgeElement=c6_c8;
            c7.outerEdgeElement=null;
            c8.outerEdgeElement=null;
            c9.outerEdgeElement=c9_c11;
            c10.outerEdgeElement=c10_c12;
            c11.outerEdgeElement=c11_c6;
            c12.outerEdgeElement=null;

            acrossLinkedList.elements[0] = c1;
            acrossLinkedList.elements[1] = c2;
            acrossLinkedList.elements[2] = c3;
            acrossLinkedList.elements[3] = c4;
            acrossLinkedList.elements[4] = c5;
            acrossLinkedList.elements[5] = c6;
            acrossLinkedList.elements[6] = c7;
            acrossLinkedList.elements[7] = c8;
            acrossLinkedList.elements[8] = c9;
            acrossLinkedList.elements[9] = c10;
            acrossLinkedList.elements[10] = c11;
            acrossLinkedList.elements[11] = c12;

            System.out.println(acrossLinkedList.elements);

            // BFS(acrossLinkedList);
            //申请一个空序列，用于存放顶点
            List<NodeElement> nodeElements = new ArrayList<>();
            //当序列未包含所有顶点
            while (nodeElements.size()!=12){
                for (int i=0;i<acrossLinkedList.elements.length;i++){
                    NodeElement element = acrossLinkedList.elements[i];
                    //若节点已经被软删除，则越过
                    if (element.whetherRemove == true){
                        continue;
                    }
                    //若没有弧从该顶点进来，则将该顶点加入序列，且删除图中该顶点以及从该顶点出去的弧
                    if (element.innerEdgeElement == null || element.innerEdgeElement.whetherRemove == true) {
                        //将节点加入节点序列
                        nodeElements.add(element);
                        //软删除该节点
                        element.whetherRemove=true;
                        //软删除从节点出去的弧
                        EdgeElement outerNextEdge = element.outerEdgeElement;
                        while (outerNextEdge!=null){
                            outerNextEdge.whetherRemove = true;
                            outerNextEdge = outerNextEdge.outerNextEdge;
                        }
                    }
                }
            }

            //打印拓扑排序结果
            for (NodeElement nodeElement : nodeElements) {
                System.out.println(nodeElement.data);
            }
        }

        /**
         * 顺便复习一下BFS
         * 1）申请一个存放所有节点状态的序列  2）申请一个空队列
         * 3）遍历所有节点，先将A节点入队列
         * 4）若队列不为空，则弹出队列中元素，访问，当存在从该节点出去的边，则将令一端点加入队列
         * 5）循环步骤4，直到所有节点均被访问为止
         */
        public static void BFS(AcrossLinkedList acrossLinkedList){
            //申请一个节点被访问状态序列
            Map<String,Boolean> visited = new HashMap<>(4);
            visited.put("A",false);
            visited.put("B",false);
            visited.put("C",false);
            visited.put("D",false);
            //申请一个存放待处理节点的队列
            Queue<NodeElement> assistanceQueue = new LinkedList<>();
            //循环直到所有节点访问状态均为true为止
            for (NodeElement element : acrossLinkedList.elements) {
                assistanceQueue.offer(element);
                while (!assistanceQueue.isEmpty()){ //当队列不为空
                    //顺序弹出队列中的节点
                    NodeElement poll = assistanceQueue.poll();
                    //若已经访问过，则跳过
                    if (visited.get(poll.data)){
                        continue;
                    }
                    System.out.println(poll.data); // 输出
                    visited.put(poll.data.toString(),true);//将访问状态改为true
                    EdgeElement edge = poll.outerEdgeElement;
                    while (edge!=null){
                        //将边的另一头入队
                        assistanceQueue.offer(edge.innerElement);
                        edge = edge.outerNextEdge;
                    }
                }
            }
        }
    }


}
