package com.spring.mydiv.Dto;

import javax.validation.constraints.NotNull;

import com.spring.mydiv.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class UserDto {
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Login {
		@NotNull
		private String email;
		@NotNull
		private String password;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Request {
		@NotNull
		private String user_name;
		@NotNull
		private String user_email;
		@NotNull
		private String user_password;
		@NotNull
		private String user_account;
		@NotNull
		private String user_bank;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Response {
		@NotNull
		private Long UserId;
		@NotNull
		private String Name;
		@NotNull
		private String Email;
		@NotNull
		private String Password;
		@NotNull
		private String Account;
		@NotNull
		private String Bank;

		public static Response fromEntity(User user) {
			return Response.builder()
					.UserId(user.getId())
					.Name(user.getName())
					.Email(user.getEmail())
					.Password(user.getPassword())
					.Account(user.getAccount())
					.Bank(user.getBank())
					.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class WithTravel { // TODO - 아직 확인 못함
		@NotNull
		private Long Id;
		@NotNull
		private String Name;
		@NotNull
		private String Email;
		@NotNull
		private String Password;
		@NotNull
		private String Account;
		@NotNull
		private String Bank;

		private List<TravelDto.Response> TravelList;

		public static WithTravel fromEntity(User user) {
			return WithTravel.builder()
					.Id(user.getId())
					.Name(user.getName())
					.Email(user.getEmail())
					.Password(user.getPassword())
					.Account(user.getAccount())
					.Bank(user.getBank())
					.build();
		}
	}
}