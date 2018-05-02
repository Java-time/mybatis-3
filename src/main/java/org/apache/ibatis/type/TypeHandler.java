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
package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型解析器, 用于将自定义类型的对象设置到 {@link PreparedStatement} 中或从
 * {@link ResultSet} 中提取出封装成自定义对象;
 *
 * 该 Java 包下包含许多内置的类型处理器, 如八种基本数据类型, 还有常见的一些 Java 类型
 * 如 {@link java.util.Date}, 以及 Java 8 之后的 {@link java.time.LocalDate} 和
 * {@link java.time.LocalDateTime}, 数组 等类型处理器;
 *
 * PS: 就在我写这段注释时, GitHub 上新增了一个类型处理器 SqlxmlTypeHandler 的 PR, (PR #1221)
 *
 * @author Clinton Begin
 */
public interface TypeHandler<T> {

  void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

  T getResult(ResultSet rs, String columnName) throws SQLException;

  T getResult(ResultSet rs, int columnIndex) throws SQLException;

  T getResult(CallableStatement cs, int columnIndex) throws SQLException;

}
