package com.spring.mydiv.Entity;

import javax.persistence.*;

//import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * @author 12nov
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", length = 20)
	private Long id;
	
	@Column(name = "user_name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "user_email", length = 100, nullable = false, unique = true)
	private String email;
	
	@Column(name = "user_password", length = 200, nullable = false)
	private String password;
	
	@Column(name = "user_account", length = 20, nullable = false)
	private String account;

	@Column(name = "user_bank", length = 50, nullable = false)
	private String bank;

}