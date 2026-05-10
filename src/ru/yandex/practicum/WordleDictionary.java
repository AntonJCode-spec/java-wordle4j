package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    private final List<String> words;
    private final PrintWriter log;

    public WordleDictionary(PrintWriter log) {
        words = new ArrayList<>();
        this.log = log;
    }

    public List<String> getWords() {
        return words;
    }


    public void add(String word) {
        if (word == null || word.isBlank()) {
            log.println("Попытка добавить пустое слово");
            return;
        }
        words.add(word);
    }

    public boolean contains(String str) {
        return words.contains(str);
    }

    public String selectAnswer() throws CriticalGameException {
        Random rand = new Random();
        String answer = words.get(rand.nextInt(words.size()));
        if (answer == null || answer.isBlank()) {
            throw new RuntimeException("Выбрано пустое слово-ответ");
        }
        if (answer.length() != WordleGame.WORD_SIZE) {
            throw new RuntimeException("Выбранное слово имеет неверную длину: " + answer);
        }
        return answer;
    }
}
