package com.spring.mydiv.Entity;

import javax.persistence.*;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "participant")
public class Participant implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "participant_id", length = 20)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "person_id", referencedColumnName = "person_id")
	private Person person;

	@ManyToOne
	@JoinColumn(name = "event_id", referencedColumnName = "event_id")
	private Event event;
	
	@Column(name = "participant_eventrole", length = 1)
	private Boolean eventRole;
	/**Boolean eventRole
	 * 1, true: payer
	 * 0, false: -
	 */

	@Column(name = "participant_chargedprice")
	private Double chargedPrice;

}