package com.vn.entities;

import com.vn.entitiess.Booking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer rating;
	private String content;
	private LocalDate dateTime;
	
	@OneToOne
	@JoinColumn(name = "booking_id")
	private Booking booking;
}
