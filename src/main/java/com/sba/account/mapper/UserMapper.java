package com.sba.account.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.sba.account.model.User;

@Mapper
public interface UserMapper {
	
	@Insert("insert into sba_account.account(name,username,password,role,status) values(#{name},#{username},#{password},#{role},#{status})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	void addUser(User user);
	
	@Select("SELECT id FROM sba_account.account where username=#{username}")
	Integer checkUser(@Param("username") String username);
	
	@Select("SELECT * FROM sba_account.account where username=#{username}")
	User findUser(@Param("username") String username);

}
