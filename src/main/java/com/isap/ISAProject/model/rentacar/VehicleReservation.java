package com.isap.ISAProject.model.rentacar;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;

import com.isap.ISAProject.model.user.Reservation;


@Entity
@Table(name = "vehicle_reservation")
//@EntityListeners(AuditingEntityListener.class)
/*@JsonIgnoreProperties(value = {"beginDate", "endDate"}, 
        allowGetters = true)*/
public class VehicleReservation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	//@Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
	private Date beginDate;
	
	@Column(nullable = false)
	//@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	@Column(nullable = false)
	private double price;
	
	@ManyToOne
	private Vehicle vehicle;
	
	//TODO: otkomentarisati
	//@OneToOne
	//private Reservation reservation;
	
	@Version
	private Long version;
	
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public double getPrice() {
		return TimeUnit.DAYS.convert(this.getEndDate().getTime() - this.getBeginDate().getTime(), TimeUnit.MILLISECONDS) * this.vehicle.getPricePerDay();
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	//TODO: otkomentarisati
	/*public Reservation getReservation() {
		return reservation;
	}
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}*/
	@Override
	public String toString() {
		return "VehicleReservation [id=" + id + "]";
	}

}
