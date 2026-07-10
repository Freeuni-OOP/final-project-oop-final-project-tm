package com.finalproject.backend.calendar.repositories;

import com.finalproject.backend.repositories.ServiceRepository;
import com.finalproject.backend.entities.Service;
import com.finalproject.backend.entities.Slot;
import com.finalproject.backend.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceRepositoryTest extends RepositoryTestBase {

    @Autowired
    private ServiceRepository serviceRepository;

    private static final LocalDate DAY = LocalDate.of(2030, 1, 7);

    @Test
    void findMaxCapacityReturnsTheStoredValue() {
        Service service = newService(newUser(), 3);
        em.flush();

        assertThat(serviceRepository.findMaxCapacityByServiceId(service.getId())).contains(3);
    }

    @Test
    void findMaxCapacityCoalescesNullToOne() {
        Service service = newService(newUser(), null);
        em.flush();

        assertThat(serviceRepository.findMaxCapacityByServiceId(service.getId())).contains(1);
    }

    @Test
    void findMaxCapacityIsEmptyForUnknownService() {
        assertThat(serviceRepository.findMaxCapacityByServiceId(999_999)).isEmpty();
    }

    @Test
    void lockAndGetCapacityMatchesTheUnlockedRead() {
        Service withValue = newService(newUser(), 4);
        Service withNull = newService(newUser(), null);
        em.flush();

        assertThat(serviceRepository.lockAndGetCapacity(withValue.getId())).contains(4);
        assertThat(serviceRepository.lockAndGetCapacity(withNull.getId())).contains(1);
        assertThat(serviceRepository.lockAndGetCapacity(999_999)).isEmpty();
    }

    @Test
    void findProviderIdResolvesTheOwner() {
        User owner = newUser();
        Service service = newService(owner, 1);
        em.flush();

        assertThat(serviceRepository.findProviderIdByServiceId(service.getId()))
                .contains(owner.getId());
        assertThat(serviceRepository.findProviderIdByServiceId(999_999)).isEmpty();
    }

    @Test
    void findOwnerEmailBySlotIdWalksTheJoinChain() {
        User owner = newUser();
        Service service = newService(owner, 1);
        Slot slot = newSlot(service, DAY.atTime(10, 0), DAY.atTime(11, 0));
        em.flush();

        assertThat(serviceRepository.findOwnerEmailBySlotId(slot.getSlotId()))
                .contains(owner.getEmail());
        assertThat(serviceRepository.findOwnerEmailBySlotId(999_999)).isEmpty();
    }

    @Test
    void findServiceCapacitiesByProviderListsOwnedServicesWithCoalescedCapacity() {
        User owner = newUser();
        Service withValue = newService(owner, 5);
        Service withNull = newService(owner, null);
        newService(newUser(), 9);
        em.flush();

        List<Object[]> rows = serviceRepository.findServiceCapacitiesByProvider(owner.getId());

        Map<Integer, Integer> capacities = rows.stream().collect(Collectors.toMap(
                row -> ((Number) row[0]).intValue(),
                row -> ((Number) row[1]).intValue()));
        assertThat(capacities).containsOnly(
                Map.entry(withValue.getId(), 5),
                Map.entry(withNull.getId(), 1));
    }
}
