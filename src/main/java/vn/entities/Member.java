package vn.entities;

import com.vn.entities.Booking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String fullName;
	private LocalDate birthDay;
	private Integer nationalID;
	private String email;
	private String phone;
	private String password;
	private Integer cityID;
	private Integer districtID;
	private Integer wardID;
	private String street;
	private String drivingLicense;
	private Double wallet;
	private String role;
	private boolean enabled = false;
	
	@OneToMany(mappedBy = "member")
	private List<Booking> bookings;
}
