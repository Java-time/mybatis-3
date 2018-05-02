/**
 *    Copyright 2009-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定 Mapper 方法映射的 SQL 语句;
 *
 * 类似的注解包括:
 *
 * - {@link Delete}, {@link DeleteProvider}
 * - {@link Insert}, {@link InsertProvider}
 * - {@link Select}, {@link SelectProvider}
 * - {@link Update}, {@link UpdateProvider}
 *
 * 以上每一对注解, 前者都是直接写 SQL 语句, 后者则使用一个生成器来生成 SQL 语句,
 * 生成器是一个普通的方法, 通过 {@link DeleteProvider#type()} 指定所属的类,
 * 通过 {@link DeleteProvider#method()} 指定所用的方法, 该方法可以是普通方法
 * 也可以是静态方法, 前者在使用时会实例化对象, 后者直接调用静态方法, 见
 * {@link org.apache.ibatis.builder.annotation.ProviderSqlSource#invokeProviderMethod(Object...)};
 *
 * 相关类: {@link org.apache.ibatis.mapping.SqlSource}
 *
 * @since 3.4.5, 为了在 XXXProvider 中的方法能够引用到该生成器的上下文信息,
 * 如 该生成器被哪个 Mapper 的哪个 方法 使用, 四种 XXXProvider 注解中 method 指定的方法的参数都
 * 可以添加一个 {@link org.apache.ibatis.builder.annotation.ProviderContext} 参数, 该生成器方法
 * 在被 MyBatis 调用时, 其 ProviderContext 会被注入;
 *
 * 示例见单元测试: org/apache/ibatis/submitted/sqlprovider/OurSqlBuilder
 *
 * @author Clinton Begin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Delete {
  String[] value();
}
