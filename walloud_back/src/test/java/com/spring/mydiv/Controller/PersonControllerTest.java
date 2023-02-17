package com.spring.mydiv.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import com.spring.mydiv.Service.ParticipantService;
import com.spring.mydiv.Service.PersonService;
import com.spring.mydiv.Service.TravelService;
import com.spring.mydiv.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private TravelService travelService;
    @MockBean
    private PersonService personService;
    @MockBean
    private ParticipantService participantService;
    protected MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);
    @Autowired(required=true)
    private PersonController personController;




//    @Test
//    void getAllDevelopers() throws Exception{
//
//        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
//                .developerLevel(JUNIOR)
//                .developerSkillType(BACK_END)
//                .memberId("memberId1")
//                .build();
//        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
//                .developerLevel(SENIOR)
//                .developerSkillType(BACK_END)
//                .memberId("memberId2")
//                .build();
//        given(dMakerService.getAllEmployedDevelopers())
//                .willReturn(Arrays.asList(
//                        juniorDeveloperDto, seniorDeveloperDto));
//
//        mockMvc.perform(get("/developers").contentType(contentType))
//                .andExpect(status().isOk())
//                .andDo(print()) // 출력까지!
//                .andExpect(
//                        jsonPath("$.[0].developerSkillType",
//                                is(BACK_END.name()))
//                ).andExpect(
//                        jsonPath("$.[0].developerLevel",
//                                is(JUNIOR.name()))
//                ).andExpect(
//                        jsonPath("$.[1].developerSkillType",
//                                is(BACK_END.name()))
//                ).andExpect(
//                        jsonPath("$.[1].developerLevel",
//                                is(SENIOR.name()))
//                );
//    }
}