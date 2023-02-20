package com.spring.mydiv.Repository;

import com.spring.mydiv.Entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByPerson_Id(Long id);
    List<Participant> findByEvent_Id(Long id);
    Optional<Participant> findByPerson_IdAndEvent_Id(Long id, Long id1);

    void delete(Participant participant);



    Optional<Participant> findByEvent_IdAndEventRole(Long id, Boolean eventRole);

}
