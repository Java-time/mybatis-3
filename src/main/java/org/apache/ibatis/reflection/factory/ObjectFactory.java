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
package org.apache.ibatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * MyBatis uses an ObjectFactory to create all needed new Objects.
 *
 * 对象工厂接口, MyBatis 用于在运行时创建所有所需的新对象, 典型的情况是当查询数据库返回后需要将
 * 查询结果映射成 POJO , 此时就会使用这个工厂类来创建, 使用的原理是反射(reflect);
 *
 * 该接口主要的方法为:
 * - {@link #create(Class)}: 使用默认构造函数创建对象,
 * 当我们定义的 POJO 类中含有默认构造函数的时候, MyBatis 就可以调用这个方法来新建对象;
 *
 * - {@link #create(Class, List, List)}: 使用特定的构造函数和构造函数参数创建对象,
 * 当我们在 XXXMapper.xml 文件中的 resultMap 节点中定义了子节点 constructor 的时候,
 * MyBatis 在最终数据库返回结果并转化为 POJO 时就会调用这个构造函数进行创建对象;
 *
 * 该接口的内置实现类只有 {@link DefaultObjectFactory};
 *
 * @author Clinton Begin
 */
public interface ObjectFactory {

  /**
   * Sets configuration properties.
   * @param properties configuration properties
   */
  void setProperties(Properties properties);

  /**
   * Creates a new object with default constructor. 
   * @param type Object type
   * @return
   */
  <T> T create(Class<T> type);

  /**
   * Creates a new object with the specified constructor and params.
   * @param type Object type
   * @param constructorArgTypes Constructor argument types
   * @param constructorArgs Constructor argument values
   * @return
   */
  <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);
  
  /**
   * Returns true if this object can have a set of other objects.
   * It's main purpose is to support non-java.util.Collection objects like Scala collections.
   * 
   * @param type Object type
   * @return whether it is a collection or not
   * @since 3.1.0
   */
  <T> boolean isCollection(Class<T> type);

}
