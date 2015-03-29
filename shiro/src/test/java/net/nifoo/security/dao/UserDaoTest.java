package net.nifoo.security.dao;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import net.nifoo.security.entity.User;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "classpath:spring-beans.xml" })
@Transactional
@ActiveProfiles("test")
public class UserDaoTest {
	
	@Resource
	private UserDao userDao;

	@Test
	public void testCreateUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testCorrelationRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testUncorrelationRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindOne() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByUsername() {
		String username = "admin";
		User user = userDao.findByUsername(username);
		
		Assertions.assertThat(user.getUsername()).isEqualTo(username);
	}

	@Test
	public void testFindRoles() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindPermissions() {
		fail("Not yet implemented");
	}

}
