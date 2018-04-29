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
package org.apache.ibatis.binding;

import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@link org.apache.ibatis.annotations.Mapper} 的注册表
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @author Lasse Voss
 */
public class MapperRegistry {

  private final Configuration config;
  /**
   * 实际存放 {@link org.apache.ibatis.annotations.Mapper} 注册信息的 {@link Map} 对象,
   * {@link Map.Entry#getKey()} 是注册的 {@link org.apache.ibatis.annotations.Mapper} {@link Class} 对象,
   * {@link Map.Entry#getValue()} 是 {@link org.apache.ibatis.annotations.Mapper} 对应的 {@link MapperProxyFactory} 对象,
   * {@link MapperProxyFactory#newInstance(SqlSession)} 方法会返回对应的 {@link org.apache.ibatis.annotations.Mapper}
   * 代理对象, 并且把该 {@link org.apache.ibatis.annotations.Mapper} 和 {@link SqlSession} 关联起来,
   * 由于我们声明的 {@link org.apache.ibatis.annotations.Mapper} 都是接口, 因此这里使用代理返回的是实现了该接口的代理对象,
   * 所以我们才可以直接调用我们在 {@link org.apache.ibatis.annotations.Mapper} 中定义的方法,
   * 这些方法在 MyBatis 中使用 {@link MapperMethod} 类表示, 当调用这些方法时,
   * 如果这些方法对应的的 xml 和/或 annotation 还没有被解析成 {@link MapperMethod},
   * {@link MapperProxy} 会解析他们, 然后缓存下来, 以后调用就不用再解析一次了.
   */
  private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<Class<?>, MapperProxyFactory<?>>();

  public MapperRegistry(Configuration config) {
    this.config = config;
  }

  /**
   * 既然这个类是一个 Mapper 的注册表, 那么就要能够从中获取 Mapper,
   * 如上所述, 一个 Mapper 是和一个 SqlSession 关联的,
   * 因此要获取一个 Mapper 就需要该 Mapper 的类型和与其相关联的 SqlSession.
   *
   * @param type 要获取的 Mapper 的 Class 类型
   * @param sqlSession 与 type 关联的 SqlSession 对象
   * @param <T> Mapper 的实际类型
   * @return 返回 type 指定的 Mapper 的 {@link MapperProxy} 代理对象
   * @throws BindingException 如果这个 Mapper 没有被注册过, 或者在生成代理对象时出错
   */
  @SuppressWarnings("unchecked")
  public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
    if (mapperProxyFactory == null) {
      throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
    }
    try {
      return mapperProxyFactory.newInstance(sqlSession);
    } catch (Exception e) {
      throw new BindingException("Error getting mapper instance. Cause: " + e, e);
    }
  }
  
  public <T> boolean hasMapper(Class<T> type) {
    return knownMappers.containsKey(type);
  }

  /**
   * 手动注册一个 Mapper,
   * 使用 {@link MapperAnnotationBuilder#parse()} 方法对 Mapper 的注解进行解析,
   * 而在 {@link MapperAnnotationBuilder#parse()} 方法的最开始又会调用
   * {@link org.apache.ibatis.builder.xml.XMLMapperBuilder} 对其关联的 xml 文件进行解析,
   * 因此在本方法完成后, 该 Mapper 对应的代理对象的相关配置(包括注解和 xml 配置) 就会解析完成;
   * 且 xml 的解析在 注解 解析之前, 因此可以知道如果一个配置在 xml 和 注解 中都有配置, 注解的配置应该会覆盖 xml 的配置;
   *
   * @param type 要注册的 Mapper 的 Class 对象
   * @param <T> 要注册的 Mapper 的类型
   * @throws BindingException 如果 Mapper 已经注册过了
   */
  public <T> void addMapper(Class<T> type) {
    if (type.isInterface()) {
      if (hasMapper(type)) {
        throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
      }
      boolean loadCompleted = false;
      try {
        knownMappers.put(type, new MapperProxyFactory<T>(type));
        // It's important that the type is added before the parser is run
        // otherwise the binding may automatically be attempted by the
        // mapper parser. If the type is already known, it won't try.
        MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
        parser.parse();
        loadCompleted = true;
      } finally {
        if (!loadCompleted) {
          knownMappers.remove(type);
        }
      }
    }
  }

  /**
   * @since 3.2.2
   */
  public Collection<Class<?>> getMappers() {
    return Collections.unmodifiableCollection(knownMappers.keySet());
  }

  /**
   * @since 3.2.2
   */
  public void addMappers(String packageName, Class<?> superType) {
    ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
    resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
    Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
    for (Class<?> mapperClass : mapperSet) {
      addMapper(mapperClass);
    }
  }

  /**
   * @since 3.2.2
   */
  public void addMappers(String packageName) {
    addMappers(packageName, Object.class);
  }
  
}
