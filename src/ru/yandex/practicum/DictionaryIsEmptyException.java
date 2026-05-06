package ru.yandex.practicum;

public class DictionaryIsEmptyException extends CriticalGameException {

    public DictionaryIsEmptyException(String message) {
        super(message);
    }

    public DictionaryIsEmptyException() {
    }
}
