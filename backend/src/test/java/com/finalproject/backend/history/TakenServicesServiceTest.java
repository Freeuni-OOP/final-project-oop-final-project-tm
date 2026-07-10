package com.finalproject.backend.history;

import com.finalproject.backend.entities.User;
import com.finalproject.backend.profile.DTO.ServiceDTO;
import com.finalproject.backend.profile.history.TakenServicesService;
import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TakenServicesServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TakenServicesService takenServicesService;

    @Test
    void getOfferedServices_UserExists_ReturnsMappedDTOs() {
        Integer userId = 1;
        User mockUser = new User();
        com.finalproject.backend.entities.Service service = new com.finalproject.backend.entities.Service();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(serviceRepository.findAllByProviderId(mockUser)).thenReturn(List.of(service));

        List<ServiceDTO> result = takenServicesService.getOfferedServices(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(serviceRepository).findAllByProviderId(mockUser);
    }

    @Test
    void getOfferedServices_UserNotFound_ThrowsException() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> takenServicesService.getOfferedServices(userId));
    }

    @Test
    void getRegisteredServices_ReturnsMappedDTOs() {
        Integer userId = 1;
        com.finalproject.backend.entities.Service service = new com.finalproject.backend.entities.Service();
        when(serviceRepository.findRegisteredServices(userId)).thenReturn(List.of(service));

        List<ServiceDTO> result = takenServicesService.getRegisteredServices(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(serviceRepository).findRegisteredServices(userId);
    }
}