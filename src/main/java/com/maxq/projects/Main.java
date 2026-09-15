package com.maxq.projects;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static People people1 = new People(1, "Danya", 31, "Rostov");
    static People people2 = new People(2, "Diana", 27, "SPB");
    static People people3 = new People(3, "Ivan", 23, "SPB");
    static People people4 = new People(4, "Timoha", 23, "Cherep");
    static People people5 = new People(5, "Nastya", 24, "SPB");
    static People people6 = new People(6, "Ivan", 20, "Rostov");


    static Order order1 = new Order("PAID", 200L);
    static Order order2 = new Order("PENDING", 1500L);
    static Order order3 = new Order("PAID", 300L);
    static Order order4 = new Order("CLOSE", 500L);


    public record People(Integer id, String name, Integer age, String city) {
    }

    public record Order(String status, Long sum) {
    }

    public static void main(String[] args) {

        System.out.println(filterEven());
        System.out.println(sumOfElements());
        System.out.println(listOfNamesFromPeople());
        System.out.println(avgAge());
        System.out.println(groupByCity());
        System.out.println(mapById());
        System.out.println(countOfMore30());
        System.out.println(thereIsAfter18());
        System.out.println(allIsAfter18());
        System.out.println(findFirstIvan());
        System.out.println(flatList());
        System.out.println(toStringJoin());
        System.out.println(top3());
        System.out.println(countChars());
        System.out.println(sumOrderByStatus());
        System.out.println(secondMax());


    }

    private static List<Integer> filterEven() {
        return List.of(1,2,3,4,5,6,7,8,9).stream().filter((x -> x % 2 == 0)).map(y -> y * 2).toList();
    }

    private static Integer sumOfElements() {
        return List.of(1,2,3,4,5,6,7,8,9).stream().reduce(0, (a,b) -> a + b);
    }

    private static List<String> listOfNamesFromPeople() {

        return List.of(people1,people2,people3,people4).stream().map(People::name).toList();
    }

    private static Double avgAge() {
        return List.of(people1,people2,people3,people4).stream().mapToInt(People::age).average().orElseThrow();
    }

    private static Map<String, List<People>> groupByCity() {
        return List.of(people1,people2,people3,people4).stream().collect(Collectors.groupingBy(x -> x.city));

        /*return List.of(people1,people2,people3,people4).stream().groupBy(People::city).collect(
                Collectors.toMap(People::city, Function::identity));*/
    }

    private static Map<Integer, People> mapById() {
        return List.of(people1,people2,people3,people4).stream().collect(Collectors.toMap(x -> x.id, y -> y));
    }

    private static long countOfMore30() {
        return List.of(people1,people2,people3,people4).stream().filter(p -> p.age > 30).count();
    }

    private static boolean thereIsAfter18() {
        return List.of(people1,people2,people3,people4).stream().mapToInt(People::age).filter(x -> x >= 18).findAny().isPresent();
    }

    private static boolean allIsAfter18() {
        return List.of(people1,people2,people3,people4).stream().allMatch(p -> p.age >= 18);
    }

    private static Optional<People> findFirstIvan() {
        return List.of(people1,people2,people3,people4,people5,people6).stream().filter(p -> p.name.equals("Ivan")).limit(1).findFirst();
    }

    private static List<People> flatList() {
        return List.of(List.of(people1, people2), List.of(people3, people4, people5), List.of(people6))
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    private static String toStringJoin() {
        return List.of(people1,people2,people3,people4,people5,people6).stream().map(People::name).collect(Collectors.joining(", ", "[", "]"));
    }

    private static List<People> top3() {
        return List.of(people1,people2,people3,people4,people5,people6).stream().sorted(Comparator.comparing(People::age).reversed()).limit(3).toList();
    }

    private static Map<Character, Long> countChars() {
        return "Папандопулус".chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }

    private static Map<String, Long> sumOrderByStatus() {
        return Collections.emptyMap();
    }

    private static Integer secondMax() {
        List<Integer> list = new Random().ints(14, 0, 234).boxed().sorted().toList();

        System.out.println(list);

        return List.of(people1,people2,people3,people4,people5,people6).stream().sorted(Comparator.comparingInt(People::age).reversed()).skip(1).findFirst().get().age;
    }
}