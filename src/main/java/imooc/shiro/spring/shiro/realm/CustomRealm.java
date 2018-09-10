package imooc.shiro.spring.shiro.realm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.omg.CORBA.INITIALIZE;
import org.springframework.beans.factory.annotation.Autowired;

import imooc.shiro.spring.dao.UserDao;
import imooc.shiro.spring.vo.User;


/**
 * 自定义的Realm
 * @author Tao-Three
 *@date:   2018年9月9日 下午2:36:40
 */
public class CustomRealm extends AuthorizingRealm {
	@Resource
	private UserDao userDao;

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String  user=(String) principals.getPrimaryPrincipal();
//		通过用户获取角色数据   从数据库中获取角色数据
		Set<String> roles = getRoleByUserName(user);
		Set<String> permissions = getPermissionsByUserName(user);
		SimpleAuthorizationInfo simpleAuthorizationInfo= 
				new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setStringPermissions(permissions);
		simpleAuthorizationInfo.setRoles(roles);
		
		return simpleAuthorizationInfo;
	}
	/**
	 * 模拟数据库数据
	 * @param user
	 * @return
	 *	@date:   2018年9月9日 下午2:55:22
	 */
	private Set<String> getPermissionsByUserName(String user) {
		Set<String> sets = new HashSet<>();
		sets.add("user:add");
		sets.add("user:delete");
		return sets;
	}
	/**
	 * 模拟数据库数据
	 */
	private Set<String> getRoleByUserName(String username){
		System.out.println("从数据库中获取授权数据 ");
		List<String> list = userDao.queryRoleByUserName(username);
		Set<String> sets = new HashSet<>(list);
		return sets;
	}
	
	
	
	/**
	 * 认证
	 *  */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1、从主体传过来的认证信息中，获得用户名
		String  user= (String) token.getPrincipal();
		// 2 、通过用户名到数据库取得凭证
		String password = getPasswordByUserName(user);
		if(password == null) {
			return null;
		}
		
		
		SimpleAuthenticationInfo auth = new SimpleAuthenticationInfo("Mark", password,  "customRealm");
		auth.setCredentialsSalt(ByteSource.Util.bytes("Mark"));// 加盐
		return auth;
				
	}
	
	
	/**
	 * 模拟数据库操作
	 * @param username
	 * @return
	 *	@date:   2018年9月9日 下午2:41:14
	 */
	private String getPasswordByUserName(String username) {
		User user = userDao.getUserByUserName(username);
		if(user !=null) {
			return user.getPassword();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		Md5Hash md5Hash= new Md5Hash("123456","Mark");
		System.out.println(md5Hash.toString());
		
	}


}
