package homework6_java3_logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

//Используемые методы находятся в пакете homework6_java3_logger

class MainAppTest {
    private static MainApp mainApp;

    @BeforeEach
    public void init() {
        mainApp = new MainApp();
    }

    @Test
    void checkArray() {
        Integer[] arr = {2, 4, 4, 2, 3, 4, 1, 7};
        ArrayList<Integer> checkArray2 = mainApp.createNewArrayAfterNumFour(arr);
        ArrayList<Integer> actual = new ArrayList<>(Arrays.asList(1, 7));
        Assertions.assertEquals(checkArray2, actual);
    }

    @ParameterizedTest
    @MethodSource("createNewArrayAfterNumberFour")
    public void testArraySumOperation(Integer[] array, Integer[] result) {
        ArrayList<Integer> actual = mainApp.createNewArrayAfterNumFour(array);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(result));
        Assertions.assertEquals(actual, expected);
    }

    public static Stream<Arguments> createNewArrayAfterNumberFour() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new Integer[]{2, 4, 4, 2, 3, 4, 1, 7}, new Integer[]{1, 7}));
        out.add(Arguments.arguments(new Integer[]{1, 2, 3, 4, 7, 8, 2, 7}, new Integer[]{7, 8, 2, 7}));
        return out.stream();

    }

    @Test
    public void checkToRuntimeException() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            Integer[] arr = {2, 2, 3, 1, 7};
            ArrayList<Integer> checkArray2 = mainApp.createNewArrayAfterNumFour(arr);
            ArrayList<Integer> actual = new ArrayList<>(Arrays.asList(1, 7));
            Assertions.assertEquals(checkArray2, actual);
        });
    }


    @ParameterizedTest
    @MethodSource("checkIfArrayHaveSpecificNumber1")
    public void testCheckArray2(Integer[] array, boolean result) {
        Assertions.assertEquals(mainApp.checkArrayForSpecificNum(array), result);
    }

    public static Stream<Arguments> checkIfArrayHaveSpecificNumber1() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new Integer[]{1, 1, 1, 4, 4, 1, 4, 4},true));
        out.add(Arguments.arguments(new Integer[]{1, 1, 1, 1, 1, 1},false));
        out.add(Arguments.arguments(new Integer[]{4, 4, 4, 4}, false));
        out.add(Arguments.arguments(new Integer[]{1, 1, 1, 4, 4, 1, 4, 4}, true));
        return out.stream();
    }

    @ParameterizedTest
    @MethodSource("checkIfArrayHaveSpecificNumber1")
    public void testCheckArray(Integer[] array, boolean result) {
        Assertions.assertEquals(mainApp.checkArrayForSpecificNumber1(array), result);
    }

    public static Stream<Arguments> anotherCheckForSpecNum() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new Integer[]{1, 1, 1, 4, 4, 1, 4, 4},true));
        out.add(Arguments.arguments(new Integer[]{1, 1, 1, 1, 1, 1},false));
        out.add(Arguments.arguments(new Integer[]{4, 4, 4, 4}, false));
        out.add(Arguments.arguments(new Integer[]{1, 1, 1, 4, 4, 1, 4, 4}, true));
        return out.stream();
    }


}


