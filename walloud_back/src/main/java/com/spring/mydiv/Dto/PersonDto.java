package com.spring.mydiv.Dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Entity.Travel;
import com.spring.mydiv.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

public class PersonDto {

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class basic {
		private Long PersonId;
		private User User;
		private Travel Travel;
		private Double SumSend;
		private Double SumGet;
		private Double Difference;
		private Boolean Role;

		public static basic fromEntity(Person person) {
			return basic.builder()
					.PersonId(person.getId())
					.User(person.getUser())
					.Travel(person.getTravel())
					.SumSend(person.getSumSend())
					.SumGet(person.getSumGet())
					.Difference(person.getDifference())
					.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Request {
		@NotNull
		private UserDto.Response User;
		@NotNull
		private TravelDto.Response Travel;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Simple {
		private Long PersonId;
		private String Name;

		public static Simple fromEntity(Person person) {
			return Simple.builder()
					.PersonId(person.getId())
					.Name(person.getUser().getName())
					.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class HomeView {
		private Long PersonId;
		private String Name;
		private Boolean Role;
		private Double Difference;
		private Long UserId;
		private boolean isSettled;
		@Nullable
		private String Imageurl;

		public static HomeView fromEntity(Person person) {
			return HomeView.builder()
					.PersonId(person.getId())
					.Name(person.getUser().getName())
					.Role(person.getRole())
					.Difference(person.getDifference())
					.UserId(person.getUser().getId())
					.Imageurl(person.getUser().getInfo())
					.isSettled(person.getIsSettled())
					.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class OrderMessage {
		private Long PersonId;
		private String Name;
		private Double Difference;
		private boolean isSettled;

		public static OrderMessage fromEntity(Person person) {
			return OrderMessage.builder()
					.PersonId(person.getId())
					.Name(person.getUser().getName())
					.Difference(person.getDifference())
					.isSettled(person.getIsSettled())
					.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Detail {
		private Long PersonId;
		private Double SumSend;
		private Double SumGet;
		private Double Difference;
		private Boolean TravelRole;
		private boolean isSettled;
		@NotNull
		private UserDto.Response User;
		private List<EventDto.PersonView> EventList;
		private List<OrderMessage> PersonInTravelList;

		public static Detail fromEntity(Person person) {
			return Detail.builder()
					.PersonId(person.getId())
					.SumSend(person.getSumSend())
					.SumGet(person.getSumGet())
					.Difference(person.getDifference())
					.TravelRole(person.getRole())
					.isSettled(person.getIsSettled())
					.User(UserDto.Response.fromEntity(person.getUser()))
					.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class MoneyUpdateRequest {
		private boolean pervEventRole;
		private boolean currEventRole;
		private int prevPrice;
		private int currPrice;
		private Double prevChargedPrice;
		private Double currChargedPrice;
	}
}
