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
package org.apache.ibatis.session;

import java.sql.ResultSet;

/**
 * 返回结果行限制;
 *
 * 可以通过在 Mapper 函数的参数中添加 {@link RowBounds} 参数来指定返回偏移量为 {@link RowBounds#offset},
 * 只返回 {@link RowBounds#limit} 条记录数; 该类一般用于分页;
 *
 * 但需要注意的是, 使用 {@link RowBounds} 的分页并不是从数据库层进行限制的, 也就是说 MyBatis 会把数据库的所有数据
 * 都查出来, 然后根据 {@link RowBounds} 只取其中的一部分, 这在查询返回非常多数据的时候是很慢的;
 *
 * 根据 MyBatis (Core) 的设计理念, MyBatis 不会生成依赖特定数据库的 SQL 语句, 因此使用 MyBatis (Core) 进行本地化
 * 的分页是不可能的, 但可以使用一些 Plugin 实现, 详见: https://github.com/mybatis/mybatis-3/pull/861
 *
 * 详见:
 *
 * - {@link org.apache.ibatis.executor.resultset.DefaultResultSetHandler#shouldProcessMoreRows(ResultContext, RowBounds)}
 *
 * - {@link org.apache.ibatis.executor.resultset.DefaultResultSetHandler#skipRows(ResultSet, RowBounds)}
 *
 * @author Clinton Begin
 */
public class RowBounds {

  public static final int NO_ROW_OFFSET = 0;
  public static final int NO_ROW_LIMIT = Integer.MAX_VALUE;
  public static final RowBounds DEFAULT = new RowBounds();

  private final int offset;
  private final int limit;

  public RowBounds() {
    this.offset = NO_ROW_OFFSET;
    this.limit = NO_ROW_LIMIT;
  }

  public RowBounds(int offset, int limit) {
    this.offset = offset;
    this.limit = limit;
  }

  public int getOffset() {
    return offset;
  }

  public int getLimit() {
    return limit;
  }

}
