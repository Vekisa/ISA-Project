package com.isap.ISAProject.model.airline;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "flight")
public class Flight {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;
	
	@JsonIgnore
	@ManyToOne
	private Destination startDestination;

	@JsonIgnore
	@ManyToOne
	private Destination finishDestination;

	@JsonIgnore
	@OneToMany(mappedBy = "flight", orphanRemoval = true)
	@Cascade(CascadeType.ALL)
	private List<FlightSeat> seats;

	@JsonIgnore
	@ManyToOne
	private FlightConfiguration configuration;

	@Column(nullable = false)
	private String transfers;

	@Column(nullable = false)
	private boolean finished;

	@Column(nullable = false)
	private Date departureTime;

	@Column(nullable = false)
	private Date arrivalTime;

	@Column(nullable = false)
	private double flightLength;

	@Column(nullable = false)
	private double basePrice;

	public boolean isFinished() { return finished; }

	public void setFinished(boolean finished) { this.finished = finished; }

	public Date getDepartureTime() { return departureTime; }

	public void setDepartureTime(Date departureTime) { this.departureTime = departureTime; }

	public Date getArrivalTime() { return arrivalTime; }

	public void setArrivalTime(Date arrivalTime) { this.arrivalTime = arrivalTime; }

	public double getFlightLength() { return flightLength; }

	public void setFlightLength(double flightLength) { this.flightLength = flightLength; }

	public double getBasePrice() { return basePrice; }

	public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

	public String getTransfers() { return transfers; }

	public void setTransfers(String transfers) { this.transfers = transfers; }

	public void setStartDestination(Destination destination) { this.startDestination = destination; }

	public void setFinishDestination(Destination destination) { this.finishDestination = destination; }

	public  List<FlightSeat> getSeats() { return this.seats; }

	public Long getId() { return this.id; }

	public Destination getStartDestination() { return startDestination; }

	public Destination getFinishDestination() { return finishDestination; }

	public FlightConfiguration getConfiguration() { return configuration; }

	public void setConfiguration(FlightConfiguration configuration) { this.configuration = configuration; }

}
