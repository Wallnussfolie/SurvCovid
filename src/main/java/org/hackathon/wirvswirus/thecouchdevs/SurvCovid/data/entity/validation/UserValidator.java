package org.hackathon.wirvswirus.thecouchdevs.SurvCovid.data.entity.validation;

import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.data.entity.User;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.data.entity.exception.NoValidUserException;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.data.entity.request.UserUpdateRequest;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.game.logic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;

@Service
public class UserValidator {

	PasswordEncoder decoder;

	@Resource
	private UserService userService;
	
	private static final String EMAIL_IN_USE = "This email is already is use, please provide another mail address!";
	
	private static final String USERNAME_IN_USE = "This username is already in use, please provide another username!";
	
	private static final String BINDING_FAILED = "We could not recognize the given input as valid user data!";

	/**
	 * Validates a given user object and checks whether it can be persisted inside the database.
	 * 
	 * @param user - the user object to check for sanity
	 * @param bindResult - the binding done by Spring to a user object, can be null if none is existing
	 * 
	 * @throws NoValidUserException if the given user is not valid
	 */
	public void validateNewUser(User user, BindingResult bindingResult) throws NoValidUserException {
		
		if (bindingResult != null && bindingResult.hasErrors()){
			throw new NoValidUserException(BINDING_FAILED);
		}
		
		/* should be checked by the binding result, but I leave it here for now
		if (user == null) {
			throw new NoValidUserException("Please provide a user object!");
		}
		
		if (user.getPassword().isEmpty()) {
			throw new NoValidUserException("Please provide a valid password!");
		}
		*/
		
		if (userService.isExistingUserName(user.getUserName())==true){
			throw new NoValidUserException(USERNAME_IN_USE);
		}
		
		if (userService.isExistingMail(user.getEmail())==true) {
			throw new NoValidUserException(EMAIL_IN_USE);
		}
	}

	/**
	 * Makes sure that given update data to a user are valid. Throws an exception, if not.
	 *
	 * @param potentialUpdatedUser the altered user object
	 * @param userUpdate the object holding the new data
	 * @param existingUser the existing user in the database, not changed yet
	 * @param bindingResult the binding result of the controller
	 *
	 * @throws NoValidUserException gets throws if the update is not valid
	 */
	public void validateUserUpdate(User potentialUpdatedUser, UserUpdateRequest userUpdate, User existingUser, BindingResult bindingResult) throws NoValidUserException {
		
		if (bindingResult != null && bindingResult.hasErrors()){
			throw new NoValidUserException(BINDING_FAILED);
		}
		
		if (userUpdate.getUserName() != null 
				&& !userUpdate.getUserName().isBlank() 
				&& !userUpdate.getUserName().equals(existingUser.getUserName())
				&& userService.isExistingUserName(potentialUpdatedUser.getUserName())){
			throw new NoValidUserException(USERNAME_IN_USE);
		}
	
		if (userUpdate.getEmail() != null 
				&& !userUpdate.getEmail().isBlank() 
				&& !userUpdate.getEmail().equals(existingUser.getEmail())
				&& userService.isExistingMail(potentialUpdatedUser.getEmail())) {
			throw new NoValidUserException(EMAIL_IN_USE);
		}
		
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
