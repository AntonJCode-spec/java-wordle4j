package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {


    public static WordleDictionary prepareDictionary(Path dictionaryPath, PrintWriter log) throws CriticalGameException {
        WordleDictionary dictionary = new WordleDictionary(log);

        try (BufferedReader br = Files.newBufferedReader(dictionaryPath, StandardCharsets.UTF_8)) {
            String str;
            boolean isEmpty = true;

            while ((str = br.readLine()) != null) {
                str = formatString(str);
                if (str.length() != WordleGame.WORD_SIZE) {
                    continue;
                }
                dictionary.add(str);
                isEmpty = false;
            }

            if (isEmpty) {
                throw new DictionaryIsEmptyException("Словарь пуст или не содержит слов подходящей длины");
            }
            return dictionary;
        } catch (IOException e) {
            log.println("Ошибка при получении доступа к словарю:" + dictionaryPath);
            e.printStackTrace(log);
            throw new IncorrectPathException("Ошибка доступа к файлу. Убедитесь в доступности словаря.");
        }

    }

    private static String formatString(String str) {
        return str.trim().toLowerCase().replace('ё', 'е');
    }
}
