package vn.entities;

import com.vn.entities.Car;
import com.vn.entities.Feedback;
import com.vn.entities.Member;
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
