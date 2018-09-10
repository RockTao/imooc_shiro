package imooc.shiro.spring.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import imooc.shiro.spring.util.JedisUtil;

@Component
public class RedisSessionDao extends  AbstractSessionDAO{
	@Resource
	private JedisUtil jedisUtil;
	
	private final  String SHIRO_SESSION_PREFIX="imooc_session:";
	
	private byte[] getKey(String key) {
		return (SHIRO_SESSION_PREFIX + key).getBytes();
	}
	
	/**
	 * 保存session的方法
	 * @param session
	 *	@date:   2018年9月10日 下午12:52:55
	 */
	private void saveSession(Session session) {
		if(session != null && session.getId() !=null) {
		byte[] key = getKey(session.getId().toString());
		byte[] value = SerializationUtils.serialize(session);
		jedisUtil.set(key,value);
		jedisUtil.expire(key,600);
		}
	}
	@Override
	public void update(Session session) throws UnknownSessionException {
		saveSession(session);
	}

	@Override
	public void delete(Session session) {
		if(session == null || session.getId() == null) {
			return ;
		}
		byte[] key = getKey(session.getId().toString());
		jedisUtil.del(key);
	}

	@Override
	public Collection<Session> getActiveSessions() {
		Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
		Set<Session> sessions = new HashSet<>();
		if(CollectionUtils.isEmpty(keys)) {
			return sessions;
		}
		for(byte[] key:keys) {
			Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
			sessions.add(session);
		}
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId=generateSessionId(session);
//		byte[] key = getKey(session.getId().toString());
//		byte[] value = SerializationUtils.serialize(session);
//		jedisUtil.set(key,value);
//		jedisUtil.expire(key,600);
		assignSessionId(session, sessionId);
		saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		System.out.println("Read session " );
			if(sessionId == null ) {
				return null;
			}
			byte[] key = getKey(sessionId.toString());
			byte[] value = jedisUtil.get(key);
		return (Session) SerializationUtils.deserialize(value);
	}

}
