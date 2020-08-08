package edu.iis.mto.blog.domain;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlogPostRepository blogPostRepository;

    @MockBean
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogDataMapper dataMapper;

    @Autowired
    private BlogService blogService;

    @Captor
    private ArgumentCaptor<User> userParam;

    private User creatorUser;
    private User likeUserNew;
    private User likeUserConfirmed;
    private BlogPost blogPost;

    @Before
    public void setUp() throws Exception {
        creatorUser = new User();
        creatorUser.setId(1L);
        creatorUser.setAccountStatus(AccountStatus.CONFIRMED);
        when(userRepository.findById(creatorUser.getId())).thenReturn(Optional.of(creatorUser));

        likeUserNew = new User();
        likeUserNew.setId(2L);
        likeUserNew.setAccountStatus(AccountStatus.NEW);

        likeUserConfirmed = new User();
        likeUserConfirmed.setId(3L);
        likeUserConfirmed.setAccountStatus(AccountStatus.CONFIRMED);
        when(userRepository.findById(likeUserConfirmed.getId())).thenReturn(Optional.of(likeUserConfirmed));

        blogPost = new BlogPost();
        blogPost.setId(4L);
        blogPost.setUser(creatorUser);
        when(blogPostRepository.findById(blogPost.getId())).thenReturn(Optional.of(blogPost));
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void addLikeWithStatusNEWResultsInDomainErrorThrow() {
        assertThrows(DomainError.class, () -> {
            blogService.addLikeToPost(likeUserNew.getId(), blogPost.getId());
        });
    }

    @Test
    public void canAddLikeWithAccountStatusCONFIRMED() {
        blogService.addLikeToPost(likeUserConfirmed.getId(), blogPost.getId());
    }
}
