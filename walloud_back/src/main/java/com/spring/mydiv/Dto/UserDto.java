package com.spring.mydiv.Dto;

import javax.validation.constraints.NotNull;

import com.spring.mydiv.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

public class UserDto {
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Login {
		@NotNull
		private String Email;
		@NotNull
		private String Password;
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
	public static class Response { // TODO 이미지 제외할 것 - 이미지만 주는거 => 두개로 할 것
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
	public static class ResponseWithImage {
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
		@Nullable
		private String Imageurl;

		public static ResponseWithImage fromEntity(User user) {
			return ResponseWithImage.builder()
					.UserId(user.getId())
					.Name(user.getName())
					.Email(user.getEmail())
					.Password(user.getPassword())
					.Account(user.getAccount())
					.Bank(user.getBank())
					.Imageurl(user.getInfo())
					.build();
		}
	}
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Image {
		@Nullable
		private String Imageurl;
		public static Image fromEntity(User user) {
			return Image.builder()
					.Imageurl(user.getInfo())
					.build();
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class WithTravel {
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