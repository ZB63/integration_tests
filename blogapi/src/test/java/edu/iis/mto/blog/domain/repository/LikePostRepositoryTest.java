package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;

    private User user;
    private BlogPost blogPost;
    private LikePost likePost;

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("Entry");

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

        entityManager.persist(user);
        entityManager.persist(blogPost);
        repository.save(likePost);
    }

    @Test
    public void shouldFindOneLikePost() {
        List<LikePost> likePostList = repository.findAll();
        assertThat(likePostList, hasSize(1));
    }

    @Test
    public void shouldFindOnePostWithRightBlogPost() {
        List<LikePost> likePostList = repository.findAll();
        assertTrue(likePostList.get(0).getPost().equals(blogPost));
    }

    @Test
    public void shouldFindOnePostWithRightUser() {
        List<LikePost> likePostList = repository.findAll();
        assertTrue(likePostList.get(0).getUser().equals(user));
    }

    @Test
    public void shouldFindByUserAndPost() {
        Optional<LikePost> foundLikePost = repository.findByUserAndPost(user,blogPost);
        assertTrue(foundLikePost.isPresent());
        assertEquals(likePost,foundLikePost.get() );
    }

    @Test
    public void shouldNotFindByFakeUserAndPost() {
        User fakeUser = new User();
        fakeUser.setFirstName("Tomek");
        fakeUser.setLastName("Nowak");
        fakeUser.setEmail("andrzejxd@domain.com");
        fakeUser.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(fakeUser);

        Optional<LikePost> foundLikePost = repository.findByUserAndPost(fakeUser,blogPost);
        assertFalse(foundLikePost.isPresent());
    }
}