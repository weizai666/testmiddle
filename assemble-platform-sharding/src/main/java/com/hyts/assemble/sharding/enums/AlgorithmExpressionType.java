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
package com.hyts.assemble.sharding.enums;

import com.google.common.base.Strings;
import com.hyts.assemble.sharding.model.inline.DataSourceInlineRangeStrategy;
import com.hyts.assemble.sharding.model.inline.InlineRangeStrategy;
import com.hyts.assemble.sharding.model.inline.TableInlineRangeStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import java.util.Objects;

/**
 * @project-name:standard-boot
 * @package-name:com.hyts.standard.sharding.enums
 * @author:LiBo/Alex
 * @create-date:2021-08-20 13:27
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@AllArgsConstructor
@Getter
public enum AlgorithmExpressionType {


    /**
     * HASH算法模式
     */
    HASH("%s","%","%d"){
        @Override
        public String algorithmExpression(Object param) {
            String template = "%s_${%s%s%d}";
            InlineRangeStrategy inlineRangeStrategy = (InlineRangeStrategy)param;
            Integer start = inlineRangeStrategy.getStartRangeNumber();
            Integer end = inlineRangeStrategy.getEndRangeNumber();
            if(Objects.nonNull(start) || Objects.nonNull(end)){
                int count = end - start + 1;
                if(count<= 1){
                    if(StringUtils.isNotBlank(inlineRangeStrategy.getAlgorithmExpression()))
                        return inlineRangeStrategy.getAlgorithmExpression();
                }else{
                    if(inlineRangeStrategy instanceof TableInlineRangeStrategy) {
                        TableInlineRangeStrategy tableInlineRangeStrategy = (TableInlineRangeStrategy)inlineRangeStrategy;
                        return String.format(template,tableInlineRangeStrategy.getLogicTableName(),
                                inlineRangeStrategy.getShardingColumns(),this.getToken(), count);
                    }else{
                        DataSourceInlineRangeStrategy dataSourceInlineRangeStrategy = (DataSourceInlineRangeStrategy)inlineRangeStrategy;
                        return String.format(template,dataSourceInlineRangeStrategy.getLogicDataSourceName(),
                                inlineRangeStrategy.getShardingColumns(),this.getToken(), count);
                    }
                }
            }else{
                return StringUtils.isNotBlank(inlineRangeStrategy.getAlgorithmExpression())?inlineRangeStrategy.getAlgorithmExpression()
                        : "";
            }
            return "";
        }
    };


    /**
     * 编码
     */
    private String targetObject;

    /**
     * 运算符
     */
    private String token;

    /**
     * 数值
     */
    private String value;


    /**
     * 计算相关的表达公式
     * @param param
     * @return
     */
    public abstract String algorithmExpression(Object param);




    public static void main(String[] args){
        TableInlineRangeStrategy tableInlineRangeStrategy = (TableInlineRangeStrategy)new TableInlineRangeStrategy();
        tableInlineRangeStrategy.setLogicTableName("test").setEndRangeNumber(1).setStartRangeNumber(1).setShardingColumns("id");
        System.out.println(AlgorithmExpressionType.HASH.algorithmExpression(tableInlineRangeStrategy));
    }

}
