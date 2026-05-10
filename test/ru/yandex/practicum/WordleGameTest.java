package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

public class WordleGameTest {
    static WordleGame game;

    @BeforeEach
    public void validateShouldBeCorrectTest() throws Exception {
        game = new WordleGame(Paths.get("TestFile/words_ruTest.txt"), 6, new PrintWriter(System.out), "арбуз");
        Assertions.assertTrue(true);
    }

    @Test
    public void checkWordShouldReturnAllPlusIfInputEqualsAnswerTest() throws Exception {
        Assertions.assertEquals(WordleGame.WINNING_RESULT, game.checkWord("Арбуз"));
    }

    @Test
    public void checkWordShouldThrowExceptionIfInputLengNot5Test() {
        try {
            game.checkWord("арбузик");
            Assertions.fail();
        } catch (IncorrectWordException e) {

        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void checkWordShouldThrowExceptionIfLettersNotRUTest() {
        try {
            game.checkWord("arbyz");
            Assertions.fail();
        } catch (IncorrectWordException e) {

        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void checkWordShouldThrowExceptionIfInputNotContainsDictionaryTest() {
        try {
            game.checkWord("таран");
            Assertions.fail();
        } catch (IncorrectWordException e) {

        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void makeMoveShouldReturnCorrectResultTest() {
        try {
            game.makeMove("болиз");
            Assertions.assertEquals("^---+", game.getLastResult());

            Set<String> otherPosition = new HashSet<>();
            otherPosition.add("б");

            Set<String> incorrectLetter = new HashSet<>();
            incorrectLetter.add("и");
            incorrectLetter.add("л");
            incorrectLetter.add("о");

            Map<String, Integer> correctLetter = new HashMap<>();
            correctLetter.put("з", 4);

            Assertions.assertEquals(otherPosition, game.getOtherPositionLetter());
            Assertions.assertEquals(incorrectLetter, game.getIncorrectLetter());
            Assertions.assertEquals(correctLetter, game.getCorrectLetter());

        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void updateHintShouldReturnFilteredWordsTest() throws Exception {
        game.makeMove("болид");
        game.updateHint();
        List<String> hints = game.getHintList();

        Assertions.assertFalse(hints.contains("болид"));
        Assertions.assertTrue(hints.contains("арбуз"));
    }

    @Test
    public void makeMoveShouldThrowPrintHintExceptionIfInputEmptyTest() {
        try {
            game.makeMove("");
            Assertions.fail();
        } catch (PrintHintException e) {
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    public void isWinReturnTrueIfInputIsEqualsAnswerTest() throws Exception {
        game.makeMove("арбуз");
        Assertions.assertTrue(game.isWin());
    }

    @Test
    public void isLoseReturnTrueIfStepsIsOverTest() throws Exception {
        int steps = game.getSteps();
        for (int i = 0; i < steps; i++) {
            game.makeMove("болид");
        }
        Assertions.assertTrue(game.isLose());
    }

}
