/**
 *    Copyright 2009-2015 the original author or authors.
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
package org.apache.ibatis.session;

import org.apache.ibatis.executor.result.DefaultResultHandler;

/**
 * 结果处理器, 当 Mapper 函数的返回值类型为 void 时, 可以通过给函数传递一个 {@link ResultHandler} 来自己处理查询结果;
 * 当 MyBatis 执行完 SQL 语句, 按照 {@link org.apache.ibatis.executor.resultset.ResultSetHandler} 的逻辑将查询结果集
 * 映射回 Java 对象后, 发现映射函数有 {@link ResultHandler} 参数时, 会将处理结果 "存储" 在一个
 * {@link ResultContext} 对象中, 然后会使用该 {@link ResultContext} 对象回调 Mapper 函数中的 {@link ResultHandler} 对象;
 *
 * MyBatis 内置该类的实现是 {@link org.apache.ibatis.executor.result.DefaultResultHandler}, 在 Mapper 函数返回值定义为
 * 非 void 的时候, MyBatis 会先使用 {@link DefaultResultHandler#handleResult(ResultContext)}
 * 方法将结果集存储为 {@link java.util.List} 结构, 然后使用 {@link DefaultResultHandler#getResultList()} 把结果取出,
 * 再返回;
 *
 * 具体例子见: src/test/java/org/apache/ibatis/submitted/nestedresulthandler_association/NestedResultHandlerAssociationTest.java
 *
 * @author Clinton Begin
 */
public interface ResultHandler<T> {

  void handleResult(ResultContext<? extends T> resultContext);

}
