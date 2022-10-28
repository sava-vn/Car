package com.vn.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDate startDate;
	private LocalDate endDate;
	private Integer paymentMethod;
	private Integer status;
	
	@ManyToOne
	@JoinColumn(name = "driver_id")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "car_id")
	private Car car;
	
	@OneToOne(mappedBy = "booking")
	private Feedback feedback;
}
