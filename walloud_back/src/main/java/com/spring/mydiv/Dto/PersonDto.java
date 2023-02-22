package com.spring.mydiv.Dto;

import javax.validation.constraints.NotNull;

import com.spring.mydiv.Entity.Person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

public class PersonDto {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Request {
		@NotNull
		private UserDto.Response UserDto;
		@NotNull
		private TravelDto.Response TravelDto;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class ResponseIds {
		private Long PersonId;
		private Long UserId;
		private Long TravelId;

		public static ResponseIds fromEntity(Person person) {
			return ResponseIds.builder()
					.PersonId(person.getId())
					.UserId(person.getUser().getId())
					.TravelId(person.getTravel().getId())
					.build();
		}
	}

	@Getter
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
		private String ImageUrl;

		public static HomeView fromEntity(Person person) {
			return HomeView.builder()
					.PersonId(person.getId())
					.Name(person.getUser().getName())
					.Role(person.getRole())
					.Difference(person.getDifference())
					.UserId(person.getUser().getId())
					.isSettled(person.getIsSettled())
					.ImageUrl(person.getUser().getInfo())
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
		private UserDto.Response UserDto;
		@NotNull
		private List<EventDto.Detail> EventList;
		@NotNull
		private List<OrderMessage> PersonInTravelList;
		public static Detail fromEntity(Person person) {
			return PersonDto.Detail.builder()
					.PersonId(person.getId())
					.SumSend(person.getSumSend())
					.SumGet(person.getSumGet())
					.Difference(person.getDifference())
					.TravelRole(person.getRole())
					.isSettled(person.getIsSettled())
					.UserDto(com.spring.mydiv.Dto.UserDto.Response.
							fromEntity(person.getUser()))
					.build();
		}
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Update {
		private Long personId;
		private boolean eventRole;
		private Double eventPrice;
		private Double chargedPrice;
		private boolean creating;
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
}
