/**
 *    Copyright 2009-2017 the original author or authors.
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

import java.lang.annotation.*;

/**
 * The marker annotation that indicate a constructor for automatic mapping.
 *
 * 当把数据库查询结果集转换成 Java 对象, MyBatis 判断到需要使用构造函数进行构造对象时,
 * 该注解明确告诉 MyBatis 要使用哪个构造函数进行构造, 尽管不使用该注解时, MyBatis 也能
 * 比较精确地进行猜测, 但此时如果有多个适配的构造函数, MyBatis 会按声明的顺序选择第一个,
 * 这在构造函数中含有基本数据类型且其值可能为空时, 可能会导致抛异常;
 *
 * 举个例子, 如果类 A:
 *
 * <pre>
 * class A {
 *     private int a;
 *     public A (int a) {
 *         this.a = a;
 *     }
 *     public A (Integer a) {
 *         this.a = a == null ? 0 : a;
 *     }
 * }
 * </pre>
 *
 * 如果在数据库中 a 可能为空, 返回的结果集被转换成 Java 对象时,
 * MyBatis 会优先选择第一个构造函数, 此时 null 不能转换成 int,
 * 导致抛异常, 如果给第二个构造函数添加 {@link AutomapConstructor}
 * 注解的话, 就不会有这个问题;
 *
 * 详见: {@link org.apache.ibatis.executor.resultset.DefaultResultSetHandler}
 *
 * 示例: 见单元测试 org/apache/ibatis/autoconstructor/AutoConstructorTest
 *
 * @author Tim Chen
 * @since 3.4.3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR})
public @interface AutomapConstructor {
}
