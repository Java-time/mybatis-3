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
import java.lang.reflect.Method;

import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.mapping.StatementType;

/**
 * 对应于 XML 配置文件中的 selectKey 节点;
 *
 * 用于在 **运行 SQL 语句前/后** 给 POJO 中的一些属性注入值;
 *
 * 当 MyBatis 给 PreparedStatement 设置参数的时候, 会调用 POJO 中相应的 getter 方法获取属性值,
 * 如果这些属性值不是在 Java 中设置的, 而是在数据库获得的, 那就应该使用这个注解, 举个例子:
 *
 * - Insert 操作时, 需要设置 id, 该 id 是根据数据库中的某个增长序列 id_seq 获得的, 那么需要在插入数据库
 * 之前先把 POJO 的 id 字段设置好, 此时应该将 {@link #before()} 设置为 true, 因为必须在实际进行
 * insert 操作之前就填充好这个 key 值, 并且设置 {@link #statement()} 语句用于预先填充 POJO 字段值;
 *
 * - 如果 POJO 的某个属性是在插入/更新完数据库之后由数据库生成的, 我们希望把这个生成的值设置回 POJO 中,
 * 就需要把 {@link #before()} 设置为 false;
 *
 * 值得注意的是:
 *
 * - {@link #keyProperty()} 和 {@link #keyColumn()} 都允许由多个字段, 中间用逗号分隔;
 *
 * - POJO 的 key 不一定非要是基本数据类型, 可以是自定义类型, 此时要指定 {@link #resultType()};
 *
 * 该注解的优先级大于 XML 配置文件中的 selectKey 节点和 {@link Options} 注解,
 * MyBatis 在解析注解时遇到 {@link SelectKey} 就不会解析 {@link Options} 中的
 * {@link Options#useGeneratedKeys()} 等相关信息;
 *
 * 详见: {@link MapperAnnotationBuilder#parseStatement(Method)};
 *
 * 例子: 单元测试中的 org.apache.ibatis.submitted.selectkey.SelectKeyTest
 *
 * @author Clinton Begin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SelectKey {
  String[] statement();

  String keyProperty();

  String keyColumn() default "";

  boolean before();

  Class<?> resultType();

  StatementType statementType() default StatementType.PREPARED;
}
