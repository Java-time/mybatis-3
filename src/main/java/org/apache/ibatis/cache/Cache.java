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
package org.apache.ibatis.cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * SPI for cache providers.
 * 
 * One instance of cache will be created for each namespace.
 * 
 * The cache implementation must have a constructor that receives the cache id as an String parameter.
 * 
 * MyBatis will pass the namespace as id to the constructor.
 * 
 * <pre>
 * public MyCache(final String id) {
 *  if (id == null) {
 *    throw new IllegalArgumentException("Cache instances require an ID");
 *  }
 *  this.id = id;
 *  initialize();
 * }
 * </pre>
 *
 * MyBatis 用于缓存的接口, 除了具备常见缓存接口的几个方法:
 *
 * - {@link #putObject(Object, Object)}: 将对象放入缓存;
 * - {@link #getObject(Object)}: 从缓存取出数据;
 * - {@link #removeObject(Object)}: 将数据从缓存删除;
 *
 * 该接口还要求缓存必须有一个 id,
 * 而 MyBatis 将会使用 Mapper 的命名空间(通常就是类的全限定名)作为 id
 * 作为构造函数参数来构建缓存;
 *
 * 构建完 Cache 之后可以通过 {@link org.apache.ibatis.session.Configuration#addCache(Cache)} 方法
 * 添加缓存管理器到配置中, 相同 id (即命名空间) 的缓存只会被添加到 Configuration 中一次,
 * 这是通过自定义的 Map {@link org.apache.ibatis.session.Configuration.StrictMap} 实现的;
 *
 * {@link Cache} 的实现中最重要的是
 *
 * - {@link org.apache.ibatis.cache.impl.PerpetualCache}: 顾名思义, 这是一个永久缓存,
 * 除非手动删除其中的数据, 否则都会一直保存在缓存中; 其内部仅仅组合了一个 HashMap 字段, 再加上
 * id 字段而已;
 *
 * {@link Cache} 还有一些基于 {@link org.apache.ibatis.cache.impl.PerpetualCache},
 * 使用装饰者模式的实现类:
 *
 * - {@link org.apache.ibatis.cache.decorators.SoftCache}: 软引用缓存;
 * - {@link org.apache.ibatis.cache.decorators.WeakCache}: 弱引用缓存;
 * - {@link org.apache.ibatis.cache.decorators.LruCache}: 基于"最近最少使用"算法的缓存;
 * - {@link org.apache.ibatis.cache.decorators.FifoCache}: 基于"先进先出"算法的缓存;
 * - {@link org.apache.ibatis.cache.decorators.ScheduledCache}: 定时刷新的缓存;
 *
 * @author Clinton Begin
 */

public interface Cache {

  /**
   * @return The identifier of this cache
   */
  String getId();

  /**
   * @param key Can be any object but usually it is a {@link CacheKey}
   * @param value The result of a select.
   */
  void putObject(Object key, Object value);

  /**
   * @param key The key
   * @return The object stored in the cache.
   */
  Object getObject(Object key);

  /**
   * As of 3.3.0 this method is only called during a rollback 
   * for any previous value that was missing in the cache.
   * This lets any blocking cache to release the lock that 
   * may have previously put on the key.
   * A blocking cache puts a lock when a value is null 
   * and releases it when the value is back again.
   * This way other threads will wait for the value to be 
   * available instead of hitting the database.
   *
   * 
   * @param key The key
   * @return Not used
   */
  Object removeObject(Object key);

  /**
   * Clears this cache instance
   */  
  void clear();

  /**
   * Optional. This method is not called by the core.
   * 
   * @return The number of elements stored in the cache (not its capacity).
   */
  int getSize();
  
  /** 
   * Optional. As of 3.2.6 this method is no longer called by the core.
   *  
   * Any locking needed by the cache must be provided internally by the cache provider.
   * 
   * @return A ReadWriteLock 
   */
  ReadWriteLock getReadWriteLock();

}