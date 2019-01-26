package com.isap.ISAProject.model.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

@Entity
@Table(name = "registered_user")
public class RegisteredUser extends SystemUser {
	
	private static final long serialVersionUID = 6745667384494012132L;

	@Column
	private int bonusPoints;
	
	@OneToMany(mappedBy="registeredUser")
	private List<Reservation> reservations;
	
	@OneToMany(mappedBy="sender")
	private List<FriendRequest> sentRequests;
	
	@OneToMany(mappedBy="receiver")
	private List<FriendRequest> receivedRequests;
	
	@ManyToMany
	@JoinTable(name = "friendships_between_users")
	private List<Friendship> friendships;
	
	public List<Friendship> getFriendships() {
		return friendships;
	}

	public void setFriendships(List<Friendship> friendships) {
		this.friendships = friendships;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public List<FriendRequest> getSentRequests() {
		return sentRequests;
	}

	public void setSentRequests(List<FriendRequest> sentRequests) {
		this.sentRequests = sentRequests;
	}

	public List<FriendRequest> getReceivedRequests() {
		return receivedRequests;
	}

	public void setReceivedRequests(List<FriendRequest> receivedRequests) {
		this.receivedRequests = receivedRequests;
	}
	
	public void add(@Valid Reservation reservation) {
		this.getReservations().add(reservation);
		reservation.setRegisteredUser(this);
	}
}
