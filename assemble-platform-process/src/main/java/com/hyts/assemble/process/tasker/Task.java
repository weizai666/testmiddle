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
package com.hyts.assemble.process.tasker;

import java.io.Serializable;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.process.tasker
 * @author:LiBo/Alex
 * @create-date:2022-06-01 13:34
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public class Task implements Runnable, Serializable {
    @Override
    public void run() {
        System.out.println("execute task :{}" + System.currentTimeMillis());
    }
}
