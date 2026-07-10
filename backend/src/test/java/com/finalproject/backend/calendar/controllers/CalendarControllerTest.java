package com.finalproject.backend.calendar.controllers;

import com.finalproject.backend.calendar.dtos.WeekCalendarDTO;
import com.finalproject.backend.calendar.model.CalendarHours;
import com.finalproject.backend.calendar.services.CalendarService;
import com.finalproject.backend.services.CookieService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CalendarControllerTest {

    @Mock
    private CalendarService calendarService;

    @Mock
    private CookieService cookieService;

    private MockMvc mockMvc;

    private static final LocalDate MONDAY = LocalDate.of(2026, 7, 6);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CalendarController(calendarService, cookieService))
                .build();
    }

    private static WeekCalendarDTO emptyWeek() {
        return new WeekCalendarDTO(MONDAY, MONDAY.plusDays(6),
                CalendarHours.OPEN, CalendarHours.CLOSE, List.of());
    }

    @Test
    void serviceWeekReturns200WithTheWeekJson() throws Exception {
        when(calendarService.getServiceWeek(1, 0)).thenReturn(Optional.of(emptyWeek()));

        mockMvc.perform(get("/api/calendar/service/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weekStart").value("2026-07-06"))
                .andExpect(jsonPath("$.weekEnd").value("2026-07-12"));
    }

    @Test
    void serviceWeekPassesTheWeekOffsetThrough() throws Exception {
        when(calendarService.getServiceWeek(1, 3)).thenReturn(Optional.of(emptyWeek()));

        mockMvc.perform(get("/api/calendar/service/1").param("weekOffset", "3"))
                .andExpect(status().isOk());

        verify(calendarService).getServiceWeek(1, 3);
    }

    @Test
    void serviceWeekReturns404ForUnknownService() throws Exception {
        when(calendarService.getServiceWeek(99, 0)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/calendar/service/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void ownerLookupReturnsTheOwnerId() throws Exception {
        when(calendarService.getServiceOwner(1)).thenReturn(Optional.of(7));

        mockMvc.perform(get("/api/calendar/service/1/owner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerId").value(7));
    }

    @Test
    void ownerLookupReturns404ForUnknownService() throws Exception {
        when(calendarService.getServiceOwner(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/calendar/service/99/owner"))
                .andExpect(status().isNotFound());
    }

    @Test
    void userWeekWithoutCookieIs401() throws Exception {
        mockMvc.perform(get("/api/calendar/user/7"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userWeekWithInvalidCookieIs401() throws Exception {
        when(cookieService.checkCookie("bad-token")).thenThrow(new RuntimeException("expired"));

        mockMvc.perform(get("/api/calendar/user/7").cookie(new Cookie("jwt_token", "bad-token")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userWeekOfAnotherUserIs403() throws Exception {
        when(cookieService.checkCookie("token")).thenReturn(8);

        mockMvc.perform(get("/api/calendar/user/7").cookie(new Cookie("jwt_token", "token")))
                .andExpect(status().isForbidden());
    }

    @Test
    void userWeekOfOneselfIs200() throws Exception {
        when(cookieService.checkCookie("token")).thenReturn(7);
        when(calendarService.getUserWeek(7, 0)).thenReturn(emptyWeek());

        mockMvc.perform(get("/api/calendar/user/7").cookie(new Cookie("jwt_token", "token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weekStart").value("2026-07-06"));
    }
}
