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
package org.apache.ibatis.mapping;

import javax.sql.DataSource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;

import java.io.Reader;

/**
 * MyBatis 运行时环境, 对应于 mybatis-config.xml 配置文件中的 environment 节点;
 *
 * ## 简介
 * MyBatis 允许配置多种环境, 类似于 Spring 和 Maven 中的 profile;
 * MyBatis 多环境配置的使用场景似乎结合了 Spring 运行时的多数据源特点和 Maven 按环境编译的特点:
 *
 * - 区分 开发/测试/正式 环境配置, 即 {@link #id} 字段的作用;
 * - 运行时连接多个数据源/数据库, 即 {@link #dataSource} 字段的作用;
 * - 由于每一个 Environment 都对应一个数据源, 而事务与数据源绑定, 因此也需要一个 {@link #transactionFactory} 字段管理事务;
 *
 * ## 如何使用
 * 无论使用 mybatis-config.xml 配置文件还是使用 Java 代码来使用 MyBatis,
 * 都需要先创建 {@link org.apache.ibatis.session.SqlSessionFactory},
 * 创建 SqlSessionFactory 都需要使用 {@link org.apache.ibatis.session.SqlSessionFactoryBuilder}
 * Builder 设计模式来创建, SqlSessionFactoryBuilder 提供了多种创建 SqlSessionFactory 的方法.
 *
 * - 使用配置文件:
 * 在使用 mybatis-config.xml 配置文件的时候, 需要把配置文件资源传递给 SqlSessionFactoryBuilder,
 * SqlSessionFactoryBuilder 提供的各种 build 方法中, 除了
 * {@link org.apache.ibatis.session.SqlSessionFactoryBuilder#build(Configuration)}
 * 是直接传递一个 {@link Configuration} 对象之外 (这也是使用 Java 代码使用 MyBatis 的方法),
 * 都是传递诸如 {@link Reader}, {@link java.io.InputStream} 的 build 方法,
 * 总之都是传递 mybatis-config.xml 配置文件资源的.
 *
 * - 使用 Java 代码:
 * 如上所述, 使用 Java 代码创建 SqlSessionFactory 的时候, 需要传递 Configuration 对象,
 * 而 Configuration 对象的创建 {@link Configuration#Configuration(Environment)}
 * 也需要 Environment 对象参数.
 *
 * > 即使有 {@link Configuration#Configuration()} 无参构造方法,
 * 如果在构造完成后没有使用 {@link Configuration#setEnvironment(Environment)} 方法设置 Environment,
 * 运行时也会因为找不到 Environment 而抛出 NPE.
 * 详见 {@link DefaultSqlSessionFactory#openSessionFromDataSource(ExecutorType, TransactionIsolationLevel, boolean)} )}
 *
 * ## 值得注意的点
 * - Environment 类是不可变类(Immutable), 所以当然是线程安全的;
 * - 使用了 {@link Builder} 设计模式, 方便在测试的时候使用代码创建 Environment;
 *
 * @author Clinton Begin
 */
public final class Environment {
  private final String id;
  private final TransactionFactory transactionFactory;
  private final DataSource dataSource;

  public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
    if (id == null) {
      throw new IllegalArgumentException("Parameter 'id' must not be null");
    }
    if (transactionFactory == null) {
        throw new IllegalArgumentException("Parameter 'transactionFactory' must not be null");
    }
    this.id = id;
    if (dataSource == null) {
      throw new IllegalArgumentException("Parameter 'dataSource' must not be null");
    }
    this.transactionFactory = transactionFactory;
    this.dataSource = dataSource;
  }

  public static class Builder {
      private String id;
      private TransactionFactory transactionFactory;
      private DataSource dataSource;

    public Builder(String id) {
      this.id = id;
    }

    public Builder transactionFactory(TransactionFactory transactionFactory) {
      this.transactionFactory = transactionFactory;
      return this;
    }

    public Builder dataSource(DataSource dataSource) {
      this.dataSource = dataSource;
      return this;
    }

    public String id() {
      return this.id;
    }

    public Environment build() {
      return new Environment(this.id, this.transactionFactory, this.dataSource);
    }

  }

  public String getId() {
    return this.id;
  }

  public TransactionFactory getTransactionFactory() {
    return this.transactionFactory;
  }

  public DataSource getDataSource() {
    return this.dataSource;
  }

}
