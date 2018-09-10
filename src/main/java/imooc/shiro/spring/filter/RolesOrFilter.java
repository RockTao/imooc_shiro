package imooc.shiro.spring.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;


/**
 *自定义filter   --- 对应的文件在spring里面
 *	 授权相关的filter 继承AuthorizationFilter
 *	 认证相关的filter 继承 AuthenticatingFilter
 * @author Tao-Three
 *@date:   2018年9月10日 上午10:48:08
 */
public  class RolesOrFilter extends AuthorizationFilter{

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		Subject subject = getSubject(request, response);
		String[] roles = (String[])mappedValue;
		if(roles == null || roles.length == 0) {
			return true;
		}
		for( String role : roles) {
			if(subject.hasRole(role)) {
				return true;
			}
			
		}
		return false;
		
	}

}
