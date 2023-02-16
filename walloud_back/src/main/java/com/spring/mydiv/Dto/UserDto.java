package com.spring.mydiv.Dto;

import javax.validation.constraints.NotNull;

import com.spring.mydiv.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {
	@Getter
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
}