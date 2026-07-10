package com.finalproject.backend.calendar.controllers;

import com.finalproject.backend.calendar.services.BookingService;
import com.finalproject.backend.calendar.services.BookingService.BookingActionResult;
import com.finalproject.backend.calendar.services.BookingService.BookingCreated;
import com.finalproject.backend.calendar.services.EmailNotificationService;
import com.finalproject.backend.entities.User;
import com.finalproject.backend.repositories.UserRepository;
import com.finalproject.backend.services.CookieService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private EmailNotificationService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CookieService cookieService;

    private MockMvc mockMvc;

    private static final String BODY = """
            {"serviceId":1,"date":"2026-07-06","startTime":"10:00","endTime":"11:00"}""";
    private static final Cookie AUTH = new Cookie("jwt_token", "token");
    private static final LocalDate DATE = LocalDate.of(2026, 7, 6);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new BookingController(
                        bookingService, emailService, userRepository, cookieService))
                .build();
    }

    private void loggedInAs(int userId) {
        when(cookieService.checkCookie("token")).thenReturn(userId);
    }

    @Test
    void requestWithoutCookieIs401() throws Exception {
        mockMvc.perform(post("/api/bookings/request")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("You must be logged in to book."));
    }

    @Test
    void requestWithInvalidInputIs400WithTheServiceMessage() throws Exception {
        loggedInAs(7);
        when(bookingService.requestBooking(any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Start time must be before end time."));

        mockMvc.perform(post("/api/bookings/request")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY).cookie(AUTH))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Start time must be before end time."));
    }

    @Test
    void requestOnAFullWindowIs409() throws Exception {
        loggedInAs(7);
        when(bookingService.requestBooking(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/bookings/request")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY).cookie(AUTH))
                .andExpect(status().isConflict());
    }

    @Test
    void successfulRequestReturnsSlotIdAndPendingStatus() throws Exception {
        loggedInAs(7);
        when(bookingService.requestBooking(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7))
                .thenReturn(Optional.of(new BookingCreated(100, DATE.atTime(10, 0), DATE.atTime(11, 0))));
        when(userRepository.findById(7)).thenReturn(Optional.of(new User()));
        when(bookingService.getServiceOwnerEmail(100)).thenReturn(Optional.of("owner@example.com"));

        mockMvc.perform(post("/api/bookings/request")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY).cookie(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slotId").value(100))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void aFailedNotificationEmailDoesNotUndoTheBooking() throws Exception {
        loggedInAs(7);
        when(bookingService.requestBooking(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7))
                .thenReturn(Optional.of(new BookingCreated(100, DATE.atTime(10, 0), DATE.atTime(11, 0))));
        when(userRepository.findById(7)).thenReturn(Optional.of(new User()));
        when(bookingService.getServiceOwnerEmail(100)).thenReturn(Optional.of("owner@example.com"));
        doThrow(new RuntimeException("smtp down")).when(emailService)
                .sendBookingRequestNotification(anyString(), anyInt(), anyInt(), any(), any(), any(), any());

        mockMvc.perform(post("/api/bookings/request")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY).cookie(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slotId").value(100));
    }

    @Test
    void blockWithoutCookieIs401() throws Exception {
        mockMvc.perform(post("/api/bookings/block")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void blockByNonOwnerIs403() throws Exception {
        loggedInAs(7);
        when(bookingService.blockTime(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7))
                .thenReturn(false);

        mockMvc.perform(post("/api/bookings/block")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY).cookie(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    void blockByOwnerIs200() throws Exception {
        loggedInAs(7);
        when(bookingService.blockTime(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7))
                .thenReturn(true);

        mockMvc.perform(post("/api/bookings/block")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY).cookie(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    void blockWithInvalidInputIs400() throws Exception {
        loggedInAs(7);
        when(bookingService.blockTime(any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Service not found."));

        mockMvc.perform(post("/api/bookings/block")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY).cookie(AUTH))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Service not found."));
    }

    @Test
    void unblockByOwnerIs200() throws Exception {
        loggedInAs(7);
        when(bookingService.unblockTime(1, DATE, LocalTime.of(10, 0), LocalTime.of(11, 0), 7))
                .thenReturn(true);

        mockMvc.perform(post("/api/bookings/unblock")
                        .contentType(MediaType.APPLICATION_JSON).content(BODY).cookie(AUTH))
                .andExpect(status().isOk());
    }

    @Test
    void confirmLinkForHandledOrMissingBookingIs410() throws Exception {
        when(bookingService.confirmBooking(50, 7)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/confirm/50/7"))
                .andExpect(status().isGone())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    void confirmLinkReturnsAnHtmlPageWithTheClientName() throws Exception {
        when(bookingService.confirmBooking(50, 7)).thenReturn(Optional.of(
                new BookingActionResult("Ana Bee", "ana@example.com", DATE.atTime(10, 0))));

        String html = mockMvc.perform(get("/api/bookings/confirm/50/7"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn().getResponse().getContentAsString();

        assertThat(html).contains("Booking Confirmed!").contains("Ana Bee");
    }

    @Test
    void rejectLinkForHandledOrMissingBookingIs410() throws Exception {
        when(bookingService.rejectBooking(50, 7)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/reject/50/7"))
                .andExpect(status().isGone());
    }

    @Test
    void rejectLinkReturnsAnHtmlPageWithTheClientName() throws Exception {
        when(bookingService.rejectBooking(50, 7)).thenReturn(Optional.of(
                new BookingActionResult("Ana Bee", "ana@example.com", DATE.atTime(10, 0))));

        String html = mockMvc.perform(get("/api/bookings/reject/50/7"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(html).contains("Booking Rejected").contains("Ana Bee");
    }
}
