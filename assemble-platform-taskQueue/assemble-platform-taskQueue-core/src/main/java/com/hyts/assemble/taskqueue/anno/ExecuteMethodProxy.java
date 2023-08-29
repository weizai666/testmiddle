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
package com.hyts.assemble.taskqueue.anno;




import com.hyts.assemble.taskqueue.constant.DefaultExecuteTaskType;
import com.hyts.assemble.taskqueue.constant.ExecuteTaskType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.annotation
 * @author:LiBo/Alex
 * @create-date:2021-09-28 9:53
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, ANNOTATION_TYPE, TYPE_PARAMETER, TYPE_USE})
public @interface ExecuteMethodProxy {

    /**
     * 是否启动代理执行调用机制
     * @return
     */
    boolean enable() default true;

    /**
     * 执行的业务类型(TODO 保留功能。用于切换不同的子任务类型),由于枚举的限制，后续研究一下！
     * @return
     */
    Class<? extends ExecuteTaskType> executeTaskType() default DefaultExecuteTaskType.class;

}
