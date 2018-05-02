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
package org.apache.ibatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A parameter handler sets the parameters of the {@code PreparedStatement}
 *
 * 参数处理器, 用于往 {@link PreparedStatement} 中设置参数,
 * 参数处理器一般由语句处理器 {@link org.apache.ibatis.executor.statement.StatementHandler} 持有,
 * 在语句处理器执行实际处理语句前会先调用其
 * {@link org.apache.ibatis.executor.statement.StatementHandler#parameterize(Statement)} 方法,
 * 把参数都设置进去, 因此可以断定 {@link org.apache.ibatis.executor.statement.SimpleStatementHandler}
 * 中的 {@link org.apache.ibatis.executor.statement.SimpleStatementHandler#parameterize(Statement)} 方法是空,
 * 因为其不需要设置参数;
 *
 * @author Clinton Begin
 */
public interface ParameterHandler {

  Object getParameterObject();

  void setParameters(PreparedStatement ps)
      throws SQLException;

}
