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
package org.apache.ibatis.executor.statement;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 语句处理器, 实际最终运行 SQL 查询的地方;
 *
 * MyBatis 认为 SQL 语句分为 3 类, 并将三种语句的返回结果分别由 3 个类处理:
 *
 * - {@link SimpleStatementHandler}: 普通的 SQL 语句, 所有参数都拼接在 SQL 语句中, 就是可能出现 SQL 注入的那种情况;
 *
 * - {@link PreparedStatementHandler}: 预先准备的 SQL 语句, 参数使用占位符, 实际参数另外设置;
 *
 * - {@link CallableStatementHandler}: 调用存储过程或函数的语句;
 *
 * 以上:
 *
 * - 实际运行 SQL 前可能需要设置参数, 如第 2, 3 种情况, 此时调用 {@link #parameterize(Statement)} 方法设置参数,
 *   {@link #parameterize(Statement)} 方法又将参数的处理委托给了 {@link ParameterHandler} 参数处理器处理;
 *
 * - 实际运行 SQL 后可能需要处理返回结果, 此时调用 {@link #query(Statement, ResultHandler)} 和
 *   {@link #queryCursor(Statement)} 时又将结果集的处理委托给了
 *   {@link org.apache.ibatis.executor.resultset.ResultSetHandler};
 *
 * 其他:
 *
 * - 为了处理以上 3 种情况的共同操作, MyBatis 提供了 {@link BaseStatementHandler} 抽象类来实现一些共同的操作;
 *
 * - 为了方便根据不同语句类型创建 {@link StatementHandler}, MyBatis 还提供了 {@link RoutingStatementHandler}
 *   根据不同语句将语句处理器的创建路由到不同的类进行;
 *
 * 使用:
 *
 * {@link StatementHandler} 是在 {@link org.apache.ibatis.executor.Executor} 中被使用的;
 *
 * @author Clinton Begin
 */
public interface StatementHandler {

  Statement prepare(Connection connection, Integer transactionTimeout)
      throws SQLException;

  void parameterize(Statement statement)
      throws SQLException;

  void batch(Statement statement)
      throws SQLException;

  int update(Statement statement)
      throws SQLException;

  <E> List<E> query(Statement statement, ResultHandler resultHandler)
      throws SQLException;

  <E> Cursor<E> queryCursor(Statement statement)
      throws SQLException;

  BoundSql getBoundSql();

  ParameterHandler getParameterHandler();

}
