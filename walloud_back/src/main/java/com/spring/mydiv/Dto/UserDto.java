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
		/* 현재 create, update 모두에서 사용중,
		* create 의 경우, 전부 @NotNull 이 보장되어야 하지만
		* update 의 경우는 그렇지 않음.
		* 따라서 create 함수에만 @Valid 어노테이션을 사용해 별도로 관리함
		* TODO - 하지만 이 방법이 맞는지 확인할 필요 있음...!
		* */
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