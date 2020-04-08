package org.hackathon.wirvswirus.thecouchdevs.SurvCovid.data.entity;

import javax.persistence.*;

@Entity
@Table(name="USER")
public class User {
	
	@Column(name="USER_ID")
	private String userId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long userNumber;
	
	@Column(name="USER_NAME")
	private String userName;

//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//	private InventoryItem inventoryItem;
	
	public User() {}
	
	public User(String userName) {
		this.userName = userName;
		// TODO: write a random string generator
		this.userId = userName + "_" + userNumber;
	}

	public long getUserNumber() {
		return this.userNumber;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	// TODO: Implement with new structure
//	public InventoryItem getInventoryItem() {
//		return this.inventoryItem;
//	}

}
