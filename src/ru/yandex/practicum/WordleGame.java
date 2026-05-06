package ru.yandex.practicum;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    public static final int WORD_SIZE = 5;
    public static final String WINNING_RESULT = "+++++";
    private final String answer;
    private final WordleDictionary dictionary;
    private final PrintWriter log;
    private int steps;
    private String lastResult;
    private List<String> hintList;

    private Map<String, Integer> correctLetter;
    private Set<String> incorrectLetter;
    private Set<String> otherPositionLetter;


    public WordleGame(Path directory, int steps, PrintWriter log) throws CriticalGameException {
        this.steps = steps;
        this.log = log;
        this.dictionary = WordleDictionaryLoader.prepareDictionary(directory, log);
        this.answer = dictionary.selectAnswer();
        lastResult = "";
        hintList = new ArrayList<>();

        correctLetter = new HashMap<>();
        incorrectLetter = new HashSet<>();
        otherPositionLetter = new HashSet<>();

        validateInitialBlock();
    }

    public Set<String> getIncorrectLetter() {
        return incorrectLetter;
    }

    public Set<String> getOtherPositionLetter() {
        return otherPositionLetter;
    }

    public Map<String, Integer> getCorrectLetter() {
        return correctLetter;
    }

    public int getSteps() {
        return steps;
    }

    public WordleGame(Path directory, int steps, PrintWriter log, String answer) throws CriticalGameException {
        this.steps = steps;
        this.log = log;
        this.dictionary = WordleDictionaryLoader.prepareDictionary(directory, log);
        this.answer = answer;
        lastResult = "";
        hintList = new ArrayList<>();

        correctLetter = new HashMap<>();
        incorrectLetter = new HashSet<>();
        otherPositionLetter = new HashSet<>();

        validateInitialBlock();
    }

    public String getAnswer() {
        return answer;
    }

    public String getLastResult() {
        return lastResult;
    }

    public List<String> getHintList() {
        return hintList;
    }

    public String checkWord(String inputWord) throws WordleException {
        inputWord = validateWord(inputWord);
        if (inputWord.equals(answer)) {
            return WINNING_RESULT;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < WORD_SIZE; i++) {
            if (answer.charAt(i) == inputWord.charAt(i)) {
                result.append("+");
                correctLetter.put(inputWord.substring(i, i + 1), i);

            } else if (answer.indexOf(inputWord.charAt(i)) >= 0) {
                result.append("^");
                otherPositionLetter.add(inputWord.substring(i, i + 1));

            } else {
                result.append("-");
                incorrectLetter.add(inputWord.substring(i, i + 1));
            }
        }
        return result.toString();
    }

    public void makeMove(String input) throws WordleException {
        log.printf("Ход <%d>\n", (7 - steps));
        if (input.isBlank()) {
            log.println("Запрошены подсказки");
            updateHint();
            log.println("Текущие правильные буквы: " + correctLetter);
            log.println("Текущие буквы не на своих местах: " + otherPositionLetter);
            log.println("Исключенные буквы: " + incorrectLetter);
            log.println("Найдено подсказок: " + hintList.size());
            log.flush();
            throw new PrintHintException();
        } else {
            log.println("Введено слово: " + input);
            lastResult = checkWord(input);
            steps--;
            log.println("Результат: " + lastResult);
            log.println("Осталось попыток: " + steps);
        }
        log.flush();
    }

    public boolean isLose() {
        return steps == 0;
    }

    public boolean isWin() {
        return lastResult.equals(WINNING_RESULT);
    }

    private String validateWord(String word) throws WordleException {
        if (word == null) {
            throw new EmptyInputException();
        }
        word = word.trim().toLowerCase().replace('ё', 'е');

        if (word.length() != WORD_SIZE) {
            throw new IncorrectWordException("Длина слова должна равняться " + WORD_SIZE);
        }
        if (!word.matches("[а-яё]+")) {
            throw new IncorrectWordException("Слово должно состоять из русских букв");
        }

        if (!dictionary.contains(word)) {
            throw new IncorrectWordException("Слово не содержится в словаре");
        }

        return word;
    }

    public void updateHint() {
        if (dictionary == null || dictionary.getWords() == null) {
            throw new RuntimeException("Словарь не инициализирован");
        }
        List<String> list = getHintByIncorrectLetter();
        list = getHintByIncorrectPosition(list);
        list = getHintByCorrectLetter(list);

        hintList = list;

    }

    private List<String> getHintByIncorrectLetter() {
        List<String> list = new ArrayList<>();

        for (String str : dictionary.getWords()) {
            boolean hasIncorrect = false;
            for (String letter : incorrectLetter) {
                if (str.contains(letter)) {
                    hasIncorrect = true;
                    break;
                }
            }

            if (!hasIncorrect) {
                list.add(str);
            }
        }

        return list;
    }

    private List<String> getHintByIncorrectPosition(List<String> list) {
        List<String> newList = new ArrayList<>();

        for (String str : list) {
            boolean isIncorrectPosition = true;
            for (String letter : otherPositionLetter) {
                if (!str.contains(letter)) {
                    isIncorrectPosition = false;
                    break;
                }
            }

            if (isIncorrectPosition) {
                newList.add(str);
            }
        }
        return newList;
    }

    private List<String> getHintByCorrectLetter(List<String> list) {
        List<String> newList = new ArrayList<>();

        for (String str : list) {
            boolean isCorrect = true;
            for (Map.Entry<String, Integer> entry : correctLetter.entrySet()) {

                if (entry.getValue() >= str.length()) {
                    throw new RuntimeException(
                            String.format("Позиция %d выходит за границы слова '%s'",
                                    entry.getValue(), str)
                    );
                }

                if (str.charAt(entry.getValue()) != entry.getKey().charAt(0)) {
                    isCorrect = false;
                    break;
                }
            }
            if (isCorrect) {
                newList.add(str);
            }
        }
        return newList;
    }

    private void validateInitialBlock() {
        if (dictionary == null) {
            throw new RuntimeException("Словарь не инициализирован");
        }
        if (dictionary.getWords().isEmpty()) {
            throw new RuntimeException("Словарь пуст");
        }
        if (answer == null || answer.length() != WORD_SIZE) {
            throw new RuntimeException("Ответ имеет неверную длину: " + answer);
        }
        if (log == null) {
            throw new RuntimeException("Лог файл не инициализирован");
        }
    }
}
