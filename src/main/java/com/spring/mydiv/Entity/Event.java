package com.spring.mydiv.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "event")
public class Event implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id", length = 20)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "travel_id")
	private Travel travel;
	
	@Column(name = "event_name", length = 200)
	private String name;
	
	@Column(name = "event_date")
	private Date date;
	
	@Column(name = "event_price", length = 11)
	private int price;
	
	@Column(name = "event_image")
	private String image;

	@Column(name = "payer_personid")
	private Long payerPersonid;

}