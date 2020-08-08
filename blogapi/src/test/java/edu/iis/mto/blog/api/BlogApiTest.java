package edu.iis.mto.blog.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import edu.iis.mto.blog.domain.model.AccountStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.dto.Id;
import edu.iis.mto.blog.services.BlogService;
import edu.iis.mto.blog.services.DataFinder;

import javax.persistence.EntityNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogApi.class)
public class BlogApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BlogService blogService;

    @MockBean
    private DataFinder finder;

    private UserRequest user;

    @Before
    public void setUp() {
        user = new UserRequest();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
    }

    @Test
    public void postBlogUserShouldResponseWithStatusCreatedAndNewUserId() throws Exception {
        Long newUserId = 1L;
        when(blogService.createUser(user)).thenReturn(newUserId);
        String content = writeJson(user);

        mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON)
                                      .accept(MediaType.APPLICATION_JSON)
                                      .content(content))
           .andExpect(status().isCreated())
           .andExpect(content().string(writeJson(new Id(newUserId))));
    }

    @Test
    public void returnDataIntegrityViolationExceptionShouldGenerate409Answer() throws Exception {
        when(blogService.createUser(user)).thenThrow(DataIntegrityViolationException.class);
        String content = writeJson(user);

        mvc.perform(post("/blog/user").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().is(409));
    }

    private String writeJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writer()
                                 .writeValueAsString(obj);
    }

    @Test
    public void nonExistingUserRequestShouldReturn404() throws Exception {
        Long newUserId = 1L;
        when(finder.getUserData(newUserId)).thenThrow(EntityNotFoundException.class);
        mvc.perform(get("/blog/user/{id}", newUserId)).andExpect(status().isNotFound());
    }
}
