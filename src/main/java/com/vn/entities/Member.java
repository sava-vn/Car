package com.vn.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String fullName;
	private LocalDate birthDay;
	private Integer nationalID;
	private String email;
	private String password;
	private String phone;
	private Integer cityID;
	private Integer districtID;
	private Integer wardID;
	private String street;
	private String drivingLicense;
	private Double wallet;
	private String role;
	@Column(length = 30)
	private String resetPasswordToken;
	@OneToMany(mappedBy = "member")
	private List<Booking> bookings;

}
