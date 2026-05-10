package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

public class WordleDictionaryTest {

    WordleDictionary dictionary;

    @BeforeEach
    void initialDictionary() {
        dictionary = new WordleDictionary(new PrintWriter(System.out));
    }

    @Test
    public void dictionaryShouldNotAddWordIfNullTest() {
        Assertions.assertEquals(0, dictionary.getWords().size());
        String str = null;
        dictionary.add(str);
        Assertions.assertEquals(0, dictionary.getWords().size());
    }

    @Test
    public void dictionaryShouldNotAddIfWordEmptyTest() {
        Assertions.assertEquals(0, dictionary.getWords().size());
        String str = " ";
        dictionary.add(str);
        Assertions.assertEquals(0, dictionary.getWords().size());
    }

    @Test
    public void dictionaryShouldAddIfWordNotEmptyTest() {
        Assertions.assertEquals(0, dictionary.getWords().size());
        String str = "город";
        dictionary.add(str);
        Assertions.assertEquals(1, dictionary.getWords().size());
    }

    @Test
    public void containsShouldReturnTrueIfWordInDictionaryTest() {
        dictionary.add("город");
        dictionary.add("арбуз");
        Assertions.assertTrue(dictionary.contains("город"));
    }

    @Test
    public void containsShouldReturnFalseIfWordNotInDictionaryTest() {
        dictionary.add("город");
        dictionary.add("арбуз");
        Assertions.assertFalse(dictionary.contains("скала"));
    }

    @Test
    public void selectAnswerShouldBeNotNullIfDictHave1WordTest() throws CriticalGameException {
        dictionary.add("арбуз");
        Assertions.assertNotNull(dictionary.selectAnswer());
    }

    @Test
    public void selectAnswerShouldBeNotNullIfDictHave3WordTest() throws CriticalGameException {
        dictionary.add("арбуз");
        dictionary.add("город");
        dictionary.add("болид");
        Assertions.assertNotNull(dictionary.selectAnswer());
    }
}
