package com.kbtg.bootcamp.posttest.user;

import com.kbtg.bootcamp.posttest.exeption.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	public User createUser(UserRequest request) {
		String name = request.getName();
		User user = new User(name);
		userRepository.save(user);
		return user;
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
	}


	public List<User> getAllUser() {
		List<User> users;
		users = userRepository.findAll();
	return users;
	}
}

