package com.spring.mydiv.Repository;

import com.spring.mydiv.Dto.ParticipantDto;
import com.spring.mydiv.Entity.Event;
import com.spring.mydiv.Entity.Participant;
import com.spring.mydiv.Entity.Person;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author 12nov
 */
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByPerson_Id(Long id);
    List<Participant> findByEvent_Id(Long id);
    void delete(Participant participant);
    Optional<Participant> findByPerson_IdAndEvent_Id(Long id, Long id1);

    Optional<Participant> findByEvent_IdAndEventRole(Long id, Boolean eventRole);

}
