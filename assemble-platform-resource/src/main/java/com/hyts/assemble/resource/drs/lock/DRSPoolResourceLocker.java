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
package com.hyts.assemble.resource.drs.lock;

import java.util.concurrent.TimeUnit;

/**
 * @project-name:lhy-report
 * @package-name:com.lhy.report.resource.pool
 * @author:LiBo/Alex
 * @create-date:2022-05-16 22:48
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public interface DRSPoolResourceLocker<T> {

    boolean lock(T key);

    boolean lock(T key, long timeout, TimeUnit timeUnit);

    boolean unLock(T key);

    boolean unLock(T key, long timeout, TimeUnit timeUnit);

}
