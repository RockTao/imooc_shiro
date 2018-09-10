package imooc.shiro.spring.cache;

import java.util.*;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import imooc.shiro.spring.util.JedisUtil;

@Component
public class RedisCache<K,V>  implements Cache<K,V>{
	
	
	@Resource
	private JedisUtil jedisUtil;
	
	private final String CACHE_PRIFIX = "imooc_cache:";
	private byte[] getKey(K k) {
		if(k instanceof String) {
			return (CACHE_PRIFIX + k).getBytes();
		}
		return  SerializationUtils.serialize(k);
	}
	@Override
	public void clear() throws CacheException {
		
	}
	@Override
	public V get(K k) throws CacheException {
		//TODO  此处可以加个二级缓存，存放到本地，本都读取不到，在到redis中去读取，然后存入本地缓存中
		System.out.println("c从redis获取授权");
		byte[]  value = jedisUtil.get(getKey(k));
		if(value != null  ) {
			return (V) SerializationUtils.deserialize(value);
		}
		return null;
	}
	@Override
	public Set<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public V put(K k, V v) throws CacheException {
		byte[]  key = getKey(k);
		byte[]  value = SerializationUtils.serialize(v) ;
		jedisUtil.set(key, value);
		jedisUtil.expire(key, 600);
		return v;
	}
	@Override
	public V remove(K k) throws CacheException {
		byte[] key = getKey(k);
		byte[] value = jedisUtil.get(key);
		jedisUtil.del(key);
		if(value !=null) {
			return (V) SerializationUtils.deserialize(value);
		}
		return null;
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}


}