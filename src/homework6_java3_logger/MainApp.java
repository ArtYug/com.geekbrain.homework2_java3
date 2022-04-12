package homework6_java3_logger;

import java.util.*;
import java.util.stream.Collectors;

/*                             HomeWork6 Java3
        1. Добавить на серверную сторону чата логирование, с выводом информации
        о действиях на сервере
        (запущен, произошла ошибка, клиент подключился, клиент прислал сообщение/команду).
        class Server. логи находятся в папке logs
        2. Написать метод, которому в качестве аргумента передается не пустой одномерный
        целочисленный массив.
        Метод должен вернуть новый массив, который получен путем вытаскивания из исходного
        массива элементов, идущих после последней четверки.
        Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо
        выбросить RuntimeException. Написать набор тестов для этого метода
        (по 3-4 варианта входных данных). Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
        3. Написать метод, который проверяет состав массива из чисел 1 и 4.
        Если в нем нет хоть одной четверки или единицы,
        то метод вернет false; Написать набор тестов для этого метода
        (по 3-4 варианта входных данных).
        4. *Добавить на серверную сторону сетевого чата логирование событий.*/
public class MainApp {
    public static void main(String[] args) {

    }

    public static ArrayList<Integer> checkArray(Integer[] arr) {
        List<Integer> list = Arrays.asList(arr);
        ArrayList<Integer> list1 = new ArrayList<>();
        if (list.contains(4)) {
            System.out.println("origin array" + " " + list);
            int result = (list.size() - 1) - list.lastIndexOf(4);
            for (int i = list.size() - 1; i > (list.size() - 1) - result; i--) {
                list1.add(list.get(i));
            }
            Collections.reverse(list1);
        } else {
            throw new RuntimeException("array must contain at least one 4");
        }
        return list1;
    }

    public ArrayList<Integer> createNewArrayAfterNumFour(Integer[] arr) {
        List<Integer> list = Arrays.asList(arr);
        ArrayList<Integer> list1;
        if (list.contains(4)) {
            list1 = new ArrayList<>(list.subList(list.lastIndexOf(4) + 1, list.size()));
        } else {
            throw new RuntimeException("array must contain at least one 4");
        }
        return list1;
    }


    public  boolean checkArrayForSpecificNumber1(Integer[] array) {
        List<Integer> specificNumbers = Arrays.stream(array).distinct().collect(Collectors.toList());
        return specificNumbers.size() == 2;
    }

    public boolean checkArrayForSpecificNum(Integer[] array) {
        boolean res = Arrays.stream(array).noneMatch(integer -> integer == 4);
        boolean res1 = Arrays.stream(array).noneMatch(integer -> integer == 1);
        return !res && !res1;
    }
}
