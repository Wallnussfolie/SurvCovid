package org.hackathon.wirvswirus.thecouchdevs.SurvCovid.web.controller;

import io.swagger.annotations.ApiOperation;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.data.entity.*;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.data.entity.enumeration.RoleName;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.data.entity.exception.UserNotExistingException;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.game.logic.manager.GameManager;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.game.logic.manager.submanager.ShopManager;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.game.logic.service.*;
import org.hackathon.wirvswirus.thecouchdevs.SurvCovid.web.security.SurvCovidUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class ShopController {

    @Autowired
    GameManager gameManager;

    @Autowired 
    UserService userService;
    
    @Autowired
	ShopService shopService;
    
    @Autowired
	ShopItemService shopItemService;
    
	@GetMapping("/api/shop/stock")
	@PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
	@ApiOperation(value = "List stock of a user's shop.",
			      notes = "Lists the items that are available in the shop for a specific user.")
	public ResponseEntity<List<ShopItem>> getShopStock(@ApiIgnore @AuthenticationPrincipal SurvCovidUserDetails userDetails,
													   @RequestParam(name="user_id", required=true)long userId) {
		
		User player;

		System.out.println("[DEBUG] ##### Accessing user shop endpoint to LIST STOCK.");
		System.out.println("[DEBUG] Authorities: ");
		for(GrantedAuthority auth: userDetails.getAuthorities())
			System.out.println("  - " + auth);

		System.out.println("[DEBUG] UserID: " + userDetails.getId() + " / " + userId);

		// Check if the user is an admin
		if(!userDetails.getAuthorities().contains(RoleName.ROLE_ADMIN)) {
			System.out.println("[DEBUG] User is not an admin");
			// If the user is not an admin, check if he try to access his own inventory
			if (userDetails.getId() != userId) {
				System.out.println("[DEBUG] User is not an admin and tries to access another user's shop stock!");
				// The user try to access another user's inventory => we do not allow this
				// Set HTTP status "401 Unauthorized"
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}
		}

		System.out.println("[DEBUG] User is allowed to access the inventory");
		
		try {
			player = userService.getUserById(userId);
		} 
		catch (UserNotExistingException unee) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}

		ShopManager shopManager = gameManager.getShopManager();

		if (shopManager == null) {
			// Set HTTP status "500 Internal Server Error"
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	    List<ShopItem> shopItems = shopManager.getOrCreateShopStock(player);
	    
	    if (shopItems == null) {

	        return null;
	        // TODO implement proper error handling
	    }

		return ResponseEntity.status(HttpStatus.OK).body(shopItems);
	}

}
