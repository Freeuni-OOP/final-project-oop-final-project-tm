package com.finalproject.backend.login_register.config;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginLimit {
    //5 wrong attempts in 15 mins
    private static final int MAX_ATTEMPTS = 5;
    private static final int TIME_WINDOW_MILLIS = 900000;

    private record AttemptData(int count, long startTime) {}
    private final Map<String, AttemptData> attempts = new ConcurrentHashMap<>();

    public void resetAttempts(String IP) {
        attempts.remove(IP);
    }

    //keeps track of the time that has passed since the user started trying to log in
    public boolean isBlocked(String IP) {
        AttemptData data = attempts.get(IP);
        if(data == null) {
            return false;
        }
        long curr = Instant.now().toEpochMilli();

        if(TIME_WINDOW_MILLIS < curr - data.startTime()) {
            resetAttempts(IP);
            return false;
        }

        return data.count >=MAX_ATTEMPTS;
    }

    public void markFailedAttempt(String IP) {
        long curr = Instant.now().toEpochMilli();
        //ensures we don't have to keep track of failed attempts separately
        attempts.compute(IP, (key, existing) -> {
            if(existing == null || curr - existing.startTime >TIME_WINDOW_MILLIS) {
                return new AttemptData(1, curr);
            }
            return new AttemptData(existing.count()+ 1, existing.startTime());
            }
        );
    }

}
