package com.finalproject.backend.servicePages.logic;

import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.UserRepository;
import com.finalproject.backend.servicePages.model.ServiceCreationRequest;
import com.finalproject.backend.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceCreationManagerTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ServiceCreationManager manager;

    private static ServiceCreationRequest validRequest() {
        ServiceCreationRequest request = new ServiceCreationRequest();
        request.setTitle("Guitar Lessons");
        request.setCategory("Music");
        request.setPlace("Tbilisi");
        request.setBio("Learn guitar");
        request.setPrice("45.50");
        request.setDate("2030-01-07T10:00:00Z");
        request.setMaxCapacity(2);
        return request;
    }

    private void stubUserAndSave() {
        User user = new User();
        user.setId(7);
        when(userRepository.findById(7)).thenReturn(Optional.of(user));
        when(serviceRepository.save(any(Service.class))).thenAnswer(invocation -> {
            Service service = invocation.getArgument(0);
            service.setId(42);
            return service;
        });
    }

    @Test
    void createsServiceFromRequestAndReturnsItsId() throws Exception {
        stubUserAndSave();

        int id = manager.postServiceInformation(validRequest(), 7);

        assertThat(id).isEqualTo(42);
        ArgumentCaptor<Service> saved = ArgumentCaptor.forClass(Service.class);
        verify(serviceRepository).save(saved.capture());
        Service service = saved.getValue();
        assertThat(service.getTitle()).isEqualTo("Guitar Lessons");
        assertThat(service.getCategory()).isEqualTo("Music");
        assertThat(service.getAddress()).isEqualTo("Tbilisi");
        assertThat(service.getBio()).isEqualTo("Learn guitar");
        assertThat(service.getPrice()).isEqualTo(45.50);
        assertThat(service.getMaxCapacity()).isEqualTo(2);
        assertThat(service.getActive()).isTrue();
        assertThat(service.getStar()).isZero();
        assertThat(service.getProviderId().getId()).isEqualTo(7);
    }

    @Test
    void convertsTheUtcDateToTbilisiTime() throws Exception {
        stubUserAndSave();

        manager.postServiceInformation(validRequest(), 7);

        ArgumentCaptor<Service> saved = ArgumentCaptor.forClass(Service.class);
        verify(serviceRepository).save(saved.capture());
        assertThat(saved.getValue().getTimePosted())
                .isEqualTo(LocalDateTime.of(2030, 1, 7, 14, 0));
    }

    @Test
    void notifiesTheProviderWithALinkToTheNewService() throws Exception {
        stubUserAndSave();

        manager.postServiceInformation(validRequest(), 7);

        verify(notificationService).addNotification(eq(7), contains("/services/42"));
    }

    @Test
    void throwsWhenUserDoesNotExist() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manager.postServiceInformation(validRequest(), 99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
        verify(serviceRepository, never()).save(any());
    }

    @Test
    void throwsOnUnparseablePrice() {
        User user = new User();
        user.setId(7);
        when(userRepository.findById(7)).thenReturn(Optional.of(user));
        ServiceCreationRequest request = validRequest();
        request.setPrice("not-a-number");

        assertThatThrownBy(() -> manager.postServiceInformation(request, 7))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid price format");
        verify(serviceRepository, never()).save(any());
    }
}
