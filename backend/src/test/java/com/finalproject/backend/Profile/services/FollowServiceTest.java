package com.finalproject.backend.Profile.services;

import com.finalproject.backend.entities.Follower;
import com.finalproject.backend.entities.FollowerID;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.profile.FollowService;
import com.finalproject.backend.repositories.FollowerRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowerRepository followerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    private final Integer FOLLOWER_ID = 1;
    private final Integer FOLLOWING_ID = 2;

    @Test
    @DisplayName("Should return correct follower count")
    void getFollowers() {
        when(followerRepository.countByFollowID_Following(FOLLOWING_ID)).thenReturn(5L);

        int result = followService.getFollowers(FOLLOWING_ID);

        assertEquals(5, result);
        verify(followerRepository, times(1)).countByFollowID_Following(FOLLOWING_ID);
    }

    @Test
    @DisplayName("Should return correct following count")
    void getFollowings() {
        when(followerRepository.countByFollowID_Follower(FOLLOWER_ID)).thenReturn(10L);

        int result = followService.getFollowings(FOLLOWER_ID);

        assertEquals(10, result);
        verify(followerRepository, times(1)).countByFollowID_Follower(FOLLOWER_ID);
    }

    @Test
    @DisplayName("Should invoke repository delete with correct composite ID")
    void unfollow() {
        FollowerID expectedId = new FollowerID(FOLLOWER_ID, FOLLOWING_ID);

        followService.unfollow(FOLLOWER_ID, FOLLOWING_ID);

        verify(followerRepository, times(1)).deleteById(expectedId);
    }

    @Test
    @DisplayName("Should successfully save follow relation when both users exist")
    void follow() {
        User followerUser = new User();
        User followingUser = new User();
        FollowerID expectedId = new FollowerID(FOLLOWER_ID, FOLLOWING_ID);

        when(userRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(followerUser));
        when(userRepository.findById(FOLLOWING_ID)).thenReturn(Optional.of(followingUser));

        followService.follow(FOLLOWER_ID, FOLLOWING_ID);

        ArgumentCaptor<Follower> followerCaptor = ArgumentCaptor.forClass(Follower.class);
        verify(followerRepository, times(1)).save(followerCaptor.capture());

        Follower savedFollower = followerCaptor.getValue();
        assertNotNull(savedFollower);
        assertEquals(followerUser, savedFollower.getFollower());
        assertEquals(followingUser, savedFollower.getFollowing());
        assertEquals(expectedId, savedFollower.getFollowID());
    }

    @Test
    @DisplayName("Should throw exception when follower user is not found")
    void follow_ThrowsException() {
        when(userRepository.findById(FOLLOWER_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                followService.follow(FOLLOWER_ID, FOLLOWING_ID)
        );

        assertEquals("User Not Found", exception.getMessage());
        verify(userRepository, times(1)).findById(FOLLOWER_ID);
        verify(userRepository, never()).findById(FOLLOWING_ID);
        verifyNoInteractions(followerRepository);
    }

    @Test
    @DisplayName("Should throw exception when following user is not found")
    void follow_ThrowsExceptionWhenFollowingNotFound() {
        User followerUser = new User();
        when(userRepository.findById(FOLLOWER_ID)).thenReturn(Optional.of(followerUser));
        when(userRepository.findById(FOLLOWING_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                followService.follow(FOLLOWER_ID, FOLLOWING_ID)
        );

        assertEquals("User Not Found", exception.getMessage());
        verify(userRepository, times(1)).findById(FOLLOWER_ID);
        verify(userRepository, times(1)).findById(FOLLOWING_ID);
        verifyNoInteractions(followerRepository);
    }

    @Test
    @DisplayName("Should return true when follower relationship exists")
    void isAFollower() {
        FollowerID id = new FollowerID(FOLLOWER_ID, FOLLOWING_ID);
        when(followerRepository.existsById(id)).thenReturn(true);

        Boolean result = followService.isAFollower(FOLLOWER_ID, FOLLOWING_ID);

        assertTrue(result);
        verify(followerRepository, times(1)).existsById(id);
    }

    @Test
    @DisplayName("Should return false when follower relationship does not exist")
    void isAFollower_ReturnsFalse() {
        FollowerID id = new FollowerID(FOLLOWER_ID, FOLLOWING_ID);
        when(followerRepository.existsById(id)).thenReturn(false);

        Boolean result = followService.isAFollower(FOLLOWER_ID, FOLLOWING_ID);

        assertFalse(result);
        verify(followerRepository, times(1)).existsById(id);
    }
}