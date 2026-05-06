package ru.yandex.practicum;

public class CriticalGameException extends Exception {
    public CriticalGameException(String message) {
        super(message);
    }

    public CriticalGameException() {
    }
}
