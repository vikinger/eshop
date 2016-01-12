package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import hska.iwi.eShopMaster.model.businessLogic.manager.UserManager;
import hska.iwi.eShopMaster.model.database.dataobjects.Role;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

public class UserManagerImpl implements UserManager {

	private static final String BASE_URI = "http://localhost:8020";
	private static final String USER_URI = BASE_URI + "/user";
	private static final String ROLE_URI = BASE_URI + "/role";
    private static RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public void registerUser(String username, String name, String lastname, String password, Role role) {
		User newUser = new User(username, name, lastname, password, role);
		restTemplate.postForLocation(USER_URI, newUser);
	}
	
//	@Override
//	public void registerUser(String username, String name, String lastname, String password, Role role) {
//
//		User user = new User(username, name, lastname, password, role);
//
//		helper.saveObject(user);
//	}
	
	@Override
	public User getUserByUsername(String username) {
		if (username == null || username.equals("")) {
			return null;
		}
		return restTemplate.getForObject(USER_URI + "/" + username, User.class);
	}

	@Override
	public boolean deleteUserById(int id) {
		restTemplate.delete(USER_URI + "/" + id);
		return true;
	}

	@Override
	public Role getRoleByLevel(int level) {
		return restTemplate.getForObject(ROLE_URI + "/" + level, Role.class);
	}

	@Override
	public boolean doesUserAlreadyExist(String username) {
		
    	User dbUser = this.getUserByUsername(username);
    	
    	if (dbUser != null){
    		return true;
    	}
    	else {
    		return false;
    	}
	}
	

//	public boolean validate(User user) {
//		if (user.getFirstname().isEmpty() || user.getPassword().isEmpty() || user.getRole() == null || user.getLastname() == null || user.getUsername() == null) {
//			return false;
//		}
//		
//		return true;
//	}

	@Override
	public List<User> getAllUsers() {	
		User[] users = restTemplate.getForObject(USER_URI + "s", User[].class);
	    return Arrays.asList(users);
	}

}
