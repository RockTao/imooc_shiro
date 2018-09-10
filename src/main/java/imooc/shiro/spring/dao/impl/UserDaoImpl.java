package imooc.shiro.spring.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import imooc.shiro.spring.dao.UserDao;
import imooc.shiro.spring.vo.User;


@Component
public class UserDaoImpl  implements UserDao{
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public User getUserByUserName(String username) {
		String sql = "select username,password from users where username =?";
		//		List<User> list = jdbcTemplate.query(sql, new String[]{username},new RowMapper<User>{
		List<User> list = 	jdbcTemplate.query(sql, new String[] {username}, new RowMapper<User>(){
			public User mapRow(ResultSet resultSet, int  i) throws SQLException {
				User user =new User();
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("password"));
				return user;
			}
		});

		if(CollectionUtils.isEmpty(list)) {
			return null;
		}

		return list.get(0);
	}

	@Override
	public List<String> queryRoleByUserName(String username) {
		String sql = "select role_name from user_roles where username =?";

		return jdbcTemplate.query(sql,	 new String[] {username},new  RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("role_name");

			}});

	}
}