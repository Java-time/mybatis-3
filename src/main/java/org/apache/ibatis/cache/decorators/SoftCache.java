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
package org.apache.ibatis.cache.decorators;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.ibatis.cache.Cache;

/**
 * Soft Reference cache decorator
 * Thanks to Dr. Heinz Kabutz for his guidance here.
 *
 * 基于"软引用"的缓存管理器, 在 {@link org.apache.ibatis.cache.impl.PerpetualCache}
 * 永久缓存之上进行包装, 典型的装饰者模式应用;
 *
 * {@link #SoftCache(Cache)} 构造函数接受另一个 {@link Cache} 对象
 * (一般都是 PerpetualCache, 当然也可是 Cache 的其他实现),
 * 并将所有操作都委托给该对象进行处理, 在此基础上添加了对 软引用 的处理功能:
 *
 * - {@link #putObject(Object, Object)}: 对于将要存放入缓存的对象 value, 该类将 value 进行再次包装,
 *   包装成 {@link SoftEntry}, SoftEntry 继承了 {@link SoftReference}, 并将 value 作为 SoftReference
 *   的引用, 还使用了 {@link #queueOfGarbageCollectedEntries} 字段作为 SoftReference 的 ReferenceQueue,
 *   在 SoftReference 中的引用(即 value)不可达, 即将被 GC 回收时, 可在 {@link #queueOfGarbageCollectedEntries}
 *   中找到对应的 {@link SoftEntry}, 虽然此时的 {@link SoftEntry} 的 value 已经被回收, 但仍然可以获得 {@link SoftEntry}
 *   的 key 值, 并将 key 对应的缓存从 {@link #delegate} 中删除, 达到缓存刷新的作用;
 *
 * - {@link #getObject(Object)}: 对于获取缓存数据, 其实可以直接从 {@link #delegate} 中获取出 {@link SoftEntry},
 *   取出 value 返回就行了, 但是该类(包括 {@link WeakCache})都使用了一个 {@link #hardLinksToAvoidGarbageCollection}
 *   字段, 用来存放缓存中最近被访问的一些 value, 使其有被引用而不会被回收; 原因不详, 个人对此猜测如下:
 *
 * > 由于缓存的作用就是用来存放一些经常被访问的数据, 在其再次被访问时能够快速获得返回, 如果该类(和 {@link WeakCache})
 *   不使用 {@link #queueOfGarbageCollectedEntries}, 在一段时间后(甚至更快), 缓存中就会只有 1 个或 0 个被缓存的对象,
 *   这种情况下缓存的目的就达不到了, 因此 {@link #queueOfGarbageCollectedEntries} 的作用是在于把最近访问的
 *   {@link #numberOfHardLinks} 个对象强行挂到 {@link #hardLinksToAvoidGarbageCollection} 这条引用链上,
 *   即使在外部没有对象可达这些对象了, 但由于内部 {@link #hardLinksToAvoidGarbageCollection} 仍然可达, 这些对象
 *   就不会被回收, 起到缓存作用; 但是缓存的容量不可能无限大, 因此不能任由 {@link #hardLinksToAvoidGarbageCollection} 增长,
 *   所以此处使用了 {@link #numberOfHardLinks} 限制;
 *
 * > 缓存本来就是一个 "时空代价" 的折衷
 *
 * @author Clinton Begin
 */
public class SoftCache implements Cache {
  private final Deque<Object> hardLinksToAvoidGarbageCollection;
  private final ReferenceQueue<Object> queueOfGarbageCollectedEntries;
  private final Cache delegate;
  private int numberOfHardLinks;

  public SoftCache(Cache delegate) {
    this.delegate = delegate;
    this.numberOfHardLinks = 256;
    this.hardLinksToAvoidGarbageCollection = new LinkedList<Object>();
    this.queueOfGarbageCollectedEntries = new ReferenceQueue<Object>();
  }

  @Override
  public String getId() {
    return delegate.getId();
  }

  @Override
  public int getSize() {
    removeGarbageCollectedItems();
    return delegate.getSize();
  }


  public void setSize(int size) {
    this.numberOfHardLinks = size;
  }

  @Override
  public void putObject(Object key, Object value) {
    removeGarbageCollectedItems();
    delegate.putObject(key, new SoftEntry(key, value, queueOfGarbageCollectedEntries));
  }

  @Override
  public Object getObject(Object key) {
    Object result = null;
    @SuppressWarnings("unchecked") // assumed delegate cache is totally managed by this cache
    SoftReference<Object> softReference = (SoftReference<Object>) delegate.getObject(key);
    if (softReference != null) {
      result = softReference.get();
      if (result == null) {
        delegate.removeObject(key);
      } else {
        // See #586 (and #335) modifications need more than a read lock 
        synchronized (hardLinksToAvoidGarbageCollection) {
          hardLinksToAvoidGarbageCollection.addFirst(result);
          if (hardLinksToAvoidGarbageCollection.size() > numberOfHardLinks) {
            hardLinksToAvoidGarbageCollection.removeLast();
          }
        }
      }
    }
    return result;
  }

  @Override
  public Object removeObject(Object key) {
    removeGarbageCollectedItems();
    return delegate.removeObject(key);
  }

  @Override
  public void clear() {
    synchronized (hardLinksToAvoidGarbageCollection) {
      hardLinksToAvoidGarbageCollection.clear();
    }
    removeGarbageCollectedItems();
    delegate.clear();
  }

  @Override
  public ReadWriteLock getReadWriteLock() {
    return null;
  }

  private void removeGarbageCollectedItems() {
    SoftEntry sv;
    while ((sv = (SoftEntry) queueOfGarbageCollectedEntries.poll()) != null) {
      delegate.removeObject(sv.key);
    }
  }

  private static class SoftEntry extends SoftReference<Object> {
    private final Object key;

    SoftEntry(Object key, Object value, ReferenceQueue<Object> garbageCollectionQueue) {
      super(value, garbageCollectionQueue);
      this.key = key;
    }
  }

}