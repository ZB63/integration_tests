package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;
    final String NOT_IMPORTANT = "xD";

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0)
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }


    @Test
    public void findUserByFirstName() {
        repository.save(user);
        List<User> usersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                user.getFirstName(), NOT_IMPORTANT, NOT_IMPORTANT);
        assertThat(usersList, hasSize(1));
    }

    @Test
    public void findUserByLastName() {
        repository.save(user);
        List<User> usersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                NOT_IMPORTANT, user.getLastName(), NOT_IMPORTANT);
        assertThat(usersList, hasSize(1));
    }

    @Test
    public void findUserByEmail() {
        repository.save(user);
        List<User> usersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                NOT_IMPORTANT, NOT_IMPORTANT, user.getEmail());
        assertThat(usersList, hasSize(1));
    }

    @Test
    public void findUserByFirstNameUpperCase() {
        repository.save(user);
        List<User> usersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                user.getFirstName().toUpperCase(), NOT_IMPORTANT, NOT_IMPORTANT);
        assertThat(usersList, hasSize(1));
    }

    @Test
    public void findUserByFirstNameLowerCase() {
        repository.save(user);
        List<User> usersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                user.getFirstName().toLowerCase(), NOT_IMPORTANT, NOT_IMPORTANT);
        assertThat(usersList, hasSize(1));
    }

    @Test
    public void findTwoUsersWithTheSameFirstName() {
        User similarUser;
        similarUser = new User();
        similarUser.setFirstName(user.getFirstName());
        similarUser.setLastName("Nowak");
        similarUser.setEmail("destroyer@interia.com");
        similarUser.setAccountStatus(AccountStatus.NEW);

        repository.save(user);
        repository.save(similarUser);

        repository.save(user);
        List<User> usersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                user.getFirstName(), NOT_IMPORTANT, NOT_IMPORTANT);
        assertThat(usersList, hasSize(2));
    }

    @Test
    public void shouldNotFindAnyUser() {
        repository.save(user);
        String strangeName = user.getFirstName() + "impossibletohavethatname";
        List<User> usersList = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                strangeName, NOT_IMPORTANT, NOT_IMPORTANT);
        assertThat(usersList, hasSize(0));
    }
}
