package ru.mhelper.dto;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class UserIdTimed {
    private final Long userId;
    private final LocalTime timeCreated;

    public UserIdTimed(Long userId) {
        this.userId = userId;
        this.timeCreated = LocalTime.now();
    }
}
