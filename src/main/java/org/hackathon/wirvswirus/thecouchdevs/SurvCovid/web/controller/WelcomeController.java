package org.hackathon.wirvswirus.thecouchdevs.SurvCovid.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="*", maxAge=3600)
public class WelcomeController {
	

	@GetMapping("/")
	public String welcome() {

		return "This is public content!";
	}


}
