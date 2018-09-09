package imooc.shiro.Test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;

public class JdbcRelameTest {
	DruidDataSource dataSource = new DruidDataSource();
	{
		dataSource.setUrl("jdbc:mysql://localhost:3306/imooc_shiro");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		
	}
	
	@Test
	public void testJdbcRelameTest() {
		JdbcRealm jdbcRealm = new JdbcRealm();
		jdbcRealm.setDataSource(dataSource);
		jdbcRealm.setPermissionsLookupEnabled(true);

			
		String sql="select password from test_user where user_name = ?";
		jdbcRealm.setAuthenticationQuery(sql);
		String roleSql="select role_name from test_user_role where user_name= ?";
		jdbcRealm.setUserRolesQuery(roleSql);
		//1、构建SecurityManager 环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(jdbcRealm);
		
		// 2 、主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject=SecurityUtils.getSubject();

		UsernamePasswordToken token = new UsernamePasswordToken("xiaoming", "654321");
		subject.login(token);
		System.out.println("isAuthenticated=" + subject.isAuthenticated());
//		subject.checkRoles("admin","user");
//		subject.checkPermission("user:select");
//		subject.checkPermissions("user:update");
		
		
		
		subject.checkRole("user");
	}

	
}
