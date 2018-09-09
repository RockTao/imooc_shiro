package imooc.shiro.Test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.imooc.shiro.CustomRealm;

/**
 * 自定义测试类
 * @author Tao-Three
 *@date:   2018年9月9日 下午2:45:29
 */
public class CustomRealmTest {

	@Test
	public void testCustomRealmTest() {
		
		CustomRealm customRealm = new 		CustomRealm();
		//1、构建SecurityManager 环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(customRealm);
		// 加密
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName("md5");//设置加密的名称
		matcher.setHashIterations(1);//s设置加密的次数
		customRealm.setCredentialsMatcher(matcher);
		
		// 2 、主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject=SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken("Mark", "123456");
		subject.login(token);
		System.out.println("isAuthenticated=" + subject.isAuthenticated());

//		subject.checkRoles("admin","user1");
//		subject.logout();
//		System.out.println("isAuthenticated=" + subject.isAuthenticated());

		
		
		
		
		subject.checkRole("admin");
		subject.checkPermission("user:delete");
		
	}
	
	
	
}
