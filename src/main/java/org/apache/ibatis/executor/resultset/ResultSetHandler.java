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
package org.apache.ibatis.executor.resultset;

import org.apache.ibatis.cursor.Cursor;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 结果集处理器, 当数据库查询结果返回时, MyBatis 使用结果集处理器处理返回结果,
 * 由于数据库查询最终实际由 {@link org.apache.ibatis.executor.statement.StatementHandler}
 * 进行, 因此结果集处理器也由 StatementHandler 持有, 在查询完成后, StatementHandler 会使用
 * 结果集处理器进行处理数据;
 *
 * 从数据库返回结果集的情况有以下两种:
 *
 * - 普通查询语句: 如 select 语句, 此时根据需要的结果, 调用 {@link #handleResultSets(Statement)}
 *   得到解析后的 Java 对象列表, 或调用 {@link #handleCursorResultSets(Statement)} 得到 MyBatis 自定义
 *   的 {@link Cursor} 游标, {@link Cursor} 非常适用于查找大量的数据, 它使用迭代器渐进地获取数据库记录,
 *   详见 {@link Cursor};
 *
 * - 带 out 参数的存储过程: 此时调用 {@link #handleOutputParameters(CallableStatement)} 方法,
 *   将存储过程中的 out 参数设置回 Mapper 方法的参数中, 此时的 Mapper 方法参数只能是带有相关 getter/setter 方法
 *   的 Java 对象或 {@link java.util.Map};
 *
 * 示例: 见单元测试 org/apache/ibatis/submitted/sptests/SPTest
 *
 * @author Clinton Begin
 */
public interface ResultSetHandler {

  <E> List<E> handleResultSets(Statement stmt) throws SQLException;

  <E> Cursor<E> handleCursorResultSets(Statement stmt) throws SQLException;

  void handleOutputParameters(CallableStatement cs) throws SQLException;

}
