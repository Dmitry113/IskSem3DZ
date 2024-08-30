package IskSem3DZ;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

// Исключение для неверного формата данных
class InvalidDataFormatException extends Exception {
    public InvalidDataFormatException(String message) {
        super(message);
    }
}

// Исключение для неверного количества данных
class InvalidNumberOfArgumentsException extends Exception {
    public InvalidNumberOfArgumentsException(String message) {
        super(message);
    }
}

public class UserDataApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные (Фамилия Имя Отчество дата рождения номер телефона пол):");
        String input = scanner.nextLine().trim(); // Удаление лишних пробелов по краям

        try {
            // Парсинг и валидация данных
            String[] userData = parseUserData(input);
            
            // Запись данных в файл
            writeDataToFile(userData);
            
            System.out.println("Данные успешно записаны.");
        } catch (InvalidDataFormatException | InvalidNumberOfArgumentsException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода: ");
            e.printStackTrace();
        } finally {
            scanner.close(); // Закрытие сканера после использования
        }
    }

    private static String[] parseUserData(String input) throws InvalidDataFormatException, InvalidNumberOfArgumentsException {
        // Отладочная информация: вывод сырого ввода
        System.out.println("Введенные данные: '" + input + "'");

        // Разделение строки на части по пробелам
        String[] data = input.split("\\s+", 6); // Разделение по пробелам, но не более 6 частей

        // Отладочная информация
        System.out.println("Распарсенные данные:");
        for (int i = 0; i < data.length; i++) {
            System.out.println("Часть " + i + ": '" + data[i] + "'");
        }

        if (data.length != 6) {
            throw new InvalidNumberOfArgumentsException("Введено неверное количество данных. Ожидалось 6 элементов.");
        }
        
        String lastName = data[0].trim();
        String firstName = data[1].trim();
        String middleName = data[2].trim();
        String birthDate = data[3].trim();
        String phoneNumber = data[4].trim();
        String gender = data[5].trim();

        if (lastName.isEmpty()) {
            throw new InvalidDataFormatException("Фамилия не может быть пустой.");
        }

        // Проверка формата даты: dd.MM.yyyy
        if (!birthDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            throw new InvalidDataFormatException("Неверный формат даты. Ожидалось: dd.MM.yyyy");
        }

        // Проверка номера телефона
        if (!phoneNumber.matches("\\d+")) {
            throw new InvalidDataFormatException("Номер телефона должен содержать только цифры.");
        }

        // Проверка пола
        if (!gender.matches("[fm]")) {
            throw new InvalidDataFormatException("Пол должен быть указан как 'f' или 'm'.");
        }
        
        return data;
    }

    private static void writeDataToFile(String[] data) throws IOException {
        // Создание имени файла на основе фамилии
        String lastName = data[0].replaceAll("[\\/:*?\"<>|]", "");  // Удаление недопустимых символов
        String fileName = lastName + ".txt";

        // Проверка, что имя файла не пустое
        if (fileName.trim().isEmpty() || fileName.equals(".txt")) {
            throw new IOException("Имя файла не может быть пустым или состоять только из расширения.");
        }

        // Вывод отладочной информации о рабочем каталоге и имени файла
        System.out.println("Текущий рабочий каталог: " + System.getProperty("user.dir"));
        System.out.println("Имя файла для записи: " + fileName);

        // Запись данных в файл с указанием кодировки UTF-8
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"))) {
            writer.write(String.join(" ", data)); // Записываем данные в одну строку
            writer.newLine(); // Переход на новую строку для следующего ввода
        } catch (IOException e) {
            // Вывод полной информации об ошибке
            System.out.println("Ошибка ввода-вывода: ");
            e.printStackTrace();
            throw e; // Повторно выбрасываем исключение для обработки в вызывающем коде
        }
    }
}










