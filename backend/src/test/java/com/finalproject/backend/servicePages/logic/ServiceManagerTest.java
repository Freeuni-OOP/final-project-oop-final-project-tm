package com.finalproject.backend.servicePages.logic;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.StarID;
import com.finalproject.backend.entities.Stars;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.StarRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceManagerTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private StarRepository starRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ServiceManager serviceManager;

    private static User user(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private static Service service(int providerId, int starCount) {
        Service service = new Service();
        service.setProviderId(user(providerId));
        service.setTitle("Guitar Lessons");
        service.setBio("Learn guitar");
        service.setImagePath("img.png");
        service.setPrice(45.0);
        service.setCategory("Music");
        service.setAddress("Tbilisi");
        service.setStar(starCount);
        return service;
    }

    @Test
    void serviceInformationContainsAllExpectedFields() {
        when(serviceRepository.findById(5)).thenReturn(Optional.of(service(7, 0)));

        Map<String, Object> info = serviceManager.getServiceInformation(5);

        assertThat(info)
                .containsEntry("serviceTitle", "Guitar Lessons")
                .containsEntry("serviceBio", "Learn guitar")
                .containsEntry("serviceImage", "img.png")
                .containsEntry("servicePrice", 45.0)
                .containsEntry("serviceCategory", "Music")
                .containsEntry("serviceAddress", "Tbilisi")
                .containsEntry("serviceProfileId", 7);
    }

    @Test
    void serviceInformationIsNullForUnknownService() {
        when(serviceRepository.findById(99)).thenReturn(Optional.empty());

        assertThat(serviceManager.getServiceInformation(99)).isNull();
    }

    @Test
    void serviceImagePathReturnsOnlyTheImagePath() {
        when(serviceRepository.findById(5)).thenReturn(Optional.of(service(7, 0)));

        assertThat(serviceManager.getServiceImagePath(5))
                .containsOnly(Map.entry("imagePath", "img.png"));
    }

    @Test
    void serviceImagePathIsNullForUnknownService() {
        when(serviceRepository.findById(99)).thenReturn(Optional.empty());

        assertThat(serviceManager.getServiceImagePath(99)).isNull();
    }

    @Test
    void starEssenceReflectsWhetherUserStarredTheService() {
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(true);

        assertThat(serviceManager.getStarEssence(5, 7)).containsEntry("stared", true);
    }

    @Test
    void starEssenceIsFalseWhenUserNeverStarred() {
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(false);

        assertThat(serviceManager.getStarEssence(5, 7)).containsEntry("stared", false);
    }

    @Test
    void starNumberReturnsTheCount() {
        Service service = service(7, 3);
        when(serviceRepository.findById(5)).thenReturn(Optional.of(service));
        when(starRepository.countByStarred(service)).thenReturn(3);

        assertThat(serviceManager.getStarNumber(5)).containsEntry("starNum", 3);
    }

    @Test
    void starNumberThrowsForUnknownService() {
        when(serviceRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.getStarNumber(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service Not Found");
    }

    @Test
    void addStarDoesNothingWhenAlreadyStarred() {
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(true);

        serviceManager.addStar(5, 7);

        verify(starRepository, never()).save(any());
        verify(serviceRepository, never()).save(any());
    }

    @Test
    void addStarSavesTheStarAndIncrementsTheCounter() {
        Service service = service(3, 3);
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(false);
        when(userRepository.findById(7)).thenReturn(Optional.of(user(7)));
        when(serviceRepository.findById(5)).thenReturn(Optional.of(service));

        serviceManager.addStar(5, 7);

        ArgumentCaptor<Stars> saved = ArgumentCaptor.forClass(Stars.class);
        verify(starRepository).save(saved.capture());
        assertThat(saved.getValue().getStarer().getId()).isEqualTo(7);
        assertThat(saved.getValue().getStarred()).isSameAs(service);
        assertThat(service.getStar()).isEqualTo(4);
        verify(serviceRepository).save(service);
    }

    @Test
    void addStarThrowsWhenUserMissing() {
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(false);
        when(userRepository.findById(7)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.addStar(5, 7))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User Not Found");
    }

    @Test
    void addStarThrowsWhenServiceMissing() {
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(false);
        when(userRepository.findById(7)).thenReturn(Optional.of(user(7)));
        when(serviceRepository.findById(5)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.addStar(5, 7))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service Not Found");
    }

    @Test
    void removeStarThrowsWhenUserMissing() {
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(true);
        when(userRepository.findById(7)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.removeStar(5, 7))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User Not Found");
    }

    @Test
    void removeStarThrowsWhenServiceMissing() {
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(true);
        when(userRepository.findById(7)).thenReturn(Optional.of(user(7)));
        when(serviceRepository.findById(5)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceManager.removeStar(5, 7))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service Not Found");
    }

    @Test
    void removeStarDoesNothingWhenNotStarred() {
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(false);

        serviceManager.removeStar(5, 7);

        verify(starRepository, never()).delete(any());
        verify(serviceRepository, never()).save(any());
    }

    @Test
    void removeStarDeletesTheStarAndDecrementsTheCounter() {
        Service service = service(3, 3);
        when(starRepository.existsById(new StarID(7, 5))).thenReturn(true);
        when(userRepository.findById(7)).thenReturn(Optional.of(user(7)));
        when(serviceRepository.findById(5)).thenReturn(Optional.of(service));

        serviceManager.removeStar(5, 7);

        verify(starRepository).delete(any(Stars.class));
        assertThat(service.getStar()).isEqualTo(2);
        verify(serviceRepository).save(service);
    }
}
