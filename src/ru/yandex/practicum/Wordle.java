package ru.yandex.practicum;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter("log.txt", StandardCharsets.UTF_8)) {
            WordleGame game = new WordleGame(Paths.get("words_ru.txt"), 6, log);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Игра началась!");
            String input;
            while (true) {
                try {
                    System.out.println("Введите слово");
                    input = scanner.nextLine();
                    game.makeMove(input);
                    System.out.println(game.getLastResult());
                    if (game.isWin()) {
                        System.out.println("Вы угадали слово\n" + game.getAnswer());
                        break;
                    } else if (game.isLose()) {
                        System.out.println("Вы проиграли, попытки закончены.");
                        break;
                    }
                } catch (PrintHintException e) {
                    System.out.println("Выводим подсказки");
                    for (String str : game.getHintList()) {
                        System.out.println(str);
                    }

                } catch (WordleException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace(log);
                    log.flush();
                }
            }
            System.out.println("Было загадано слово " + game.getAnswer());
        } catch (CriticalGameException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
