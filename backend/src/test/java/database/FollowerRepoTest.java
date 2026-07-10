package database;

import com.finalproject.backend.BackendApplication;
import com.finalproject.backend.entities.*;
import com.finalproject.backend.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@ContextConfiguration(classes = BackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FollowerRepoTest {
    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    private User buildUser(String email) {
        User u = new User();
        u.setFirstName("User");
        u.setLastName("Person");
        u.setEmail(email);
        u.setPassHash("123");
        u.setEnabled(true);
        return userRepository.save(u);
    }

    @Test
    void savesAndGetFollow() {
        User follower = buildUser("follower1@freeuni.com");
        User following = buildUser("following1@freeuni.com");

        Follower f = new Follower();
        f.setFollowID(new FollowerID(follower.getId(), following.getId()));
        f.setFollower(follower);
        f.setFollowing(following);
        followerRepository.save(f);

        Follower found = followerRepository.findById(new FollowerID(follower.getId(), following.getId())).orElseThrow();
        assertEquals("follower1@freeuni.com", found.getFollower().getEmail());
        assertEquals("following1@freeuni.com", found.getFollowing().getEmail());
    }

    @Test
    void unFollowRelationship() {
        User follower = buildUser("follower2@freeuni.com");
        User following = buildUser("following2@freeuni.com");

        Follower f = new Follower();
        f.setFollowID(new FollowerID(follower.getId(), following.getId()));
        f.setFollower(follower);
        f.setFollowing(following);
        followerRepository.save(f);

        FollowerID id = new FollowerID(follower.getId(), following.getId());
        followerRepository.deleteById(id);

        assertTrue(followerRepository.findById(id).isEmpty());
    }

    @Test
    void countsFollow() {
        User follower = buildUser("follower3@freeuni.com");
        User following = buildUser("following3@freeuni.com");

        Follower f = new Follower();
        f.setFollowID(new FollowerID(follower.getId(), following.getId()));
        f.setFollower(follower);
        f.setFollowing(following);
        followerRepository.save(f);

        assertTrue(followerRepository.count() >= 1);
    }

}
