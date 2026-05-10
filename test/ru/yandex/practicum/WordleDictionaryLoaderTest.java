package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.nio.file.Paths;

public class WordleDictionaryLoaderTest {

    @Test
    public void loaderShouldReturnCorrectDictionaryTest() throws Exception {
        WordleDictionary dictionary = WordleDictionaryLoader.prepareDictionary(Paths.get("TestFile/words_ruTest.txt"),
                new PrintWriter(System.out));

        Assertions.assertEquals(5, dictionary.getWords().size());
        Assertions.assertTrue(dictionary.contains("город"));
        Assertions.assertTrue(dictionary.contains("болид"));
        Assertions.assertTrue(dictionary.contains("болиз"));
        Assertions.assertTrue(dictionary.contains("арбуз"));
        Assertions.assertTrue(dictionary.contains("елкии"));

    }

    @Test
    public void loaderShouldThrowExceptionIfPathIncorrectTest() {

        try {
            WordleDictionary dictionary = WordleDictionaryLoader.prepareDictionary(Paths.get("rds_ruTest.txt"),
                    new PrintWriter(System.out));
            Assertions.fail("Должно было выбросить исключение.");
        } catch (IncorrectPathException e) {
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void loaderShouldThrowExceptionIfFileIsEmpty() {

        try {
            WordleDictionary dictionary = WordleDictionaryLoader.prepareDictionary(Paths.get("TestFile/empty_fileTest.txt"),
                    new PrintWriter(System.out));
            Assertions.fail("Должно было выбросить исключение.");
        } catch (DictionaryIsEmptyException e) {
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

}
