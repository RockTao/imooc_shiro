package imooc.shiro.spring.dao;

import java.util.List;

import imooc.shiro.spring.vo.User;

public interface UserDao {

	User getUserByUserName(String username);

	List<String> queryRoleByUserName(String username);

}