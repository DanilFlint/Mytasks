package streams;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import streams.StreamPracticeData.*;
import static streams.StreamPracticeData.*;


public class Tasks {

    private static final Map<Long, StreamPracticeData.User> users = StreamPracticeData.USERS.stream()
            .collect(Collectors.toMap(StreamPracticeData.User::id, u -> u));

    private static final Map<Long, List<Order>> userOrders =
            Stream.concat(
                            USERS.stream()
                                    .map(User::id)
                                    .map(id -> Map.entry(id, new ArrayList<Order>())),
                            ORDERS.stream()
                                    .collect(Collectors.groupingBy(Order::userId))
                                    .entrySet()
                                    .stream()
                    )
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (empty, orders) -> {
                                empty.addAll(orders);
                                return empty;
                            }
                    ));

    public static void main(String[] args) {

        System.out.println("1. Топ пользователей по сумме покупок");
        System.out.println(topUsersBySumOrders(5).map(StreamPracticeData.User::toString).collect(Collectors.joining("\n")));
        System.out.println();

        System.out.println("2. Пользователи без завершённых заказов");
        System.out.println(usersWithoutStatusOrder("COMPLETED").map(StreamPracticeData.User::toString).collect(Collectors.joining("\n")));
        System.out.println();

        System.out.println("3. Самый популярный товар");
        mostPopularProduct().ifPresent(System.out::println);
        System.out.println();

        System.out.println("4. Самая прибыльная категория");
        mostProfitCategory().entrySet().forEach(System.out::println);
        System.out.println();

        System.out.println("5. Средний чек пользователей");
        usersWithMoreAvgCostCompletedOrders().entrySet().forEach(System.out::println);
        System.out.println();
    }

    /**
     * 1. Топ пользователей по сумме покупок
     *
     * Найти 5 пользователей, потративших больше всего денег на завершённые заказы.
     *
     * Учитывать количество каждого товара.
     *
     * Результат — список пользователей, отсортированный от максимальной суммы покупок к минимальной.
     *
     * @param limit
     * @return
     */
    public static Stream<StreamPracticeData.User> topUsersBySumOrders(int limit) {
        return StreamPracticeData.ORDERS.stream()
                .filter(o -> "COMPLETED".equals(o.status()))
                .collect(Collectors.groupingBy(
                        StreamPracticeData.Order::userId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                StreamPracticeData.Order::total,
                                BigDecimal::add
                        )
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Long, BigDecimal>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .map(users::get);
    }

    /**
     * 2. Пользователи без завершённых заказов
     *
     * Найти всех пользователей, у которых нет ни одного завершённого заказа.
     *
     * Результат отсортировать по возрасту, а при одинаковом возрасте — по имени.
     * @param status
     * @return
     */
    public static Stream<StreamPracticeData.User> usersWithoutStatusOrder(String status) {

        Stream<Long> allUserId = USERS.stream().mapToLong(User::id).boxed();
        Set<Long> ordersUserId = ORDERS.stream().mapToLong(Order::userId).boxed().collect(Collectors.toSet());

        Stream<Long> usersWithoutOrder = allUserId.filter(userId -> !ordersUserId.contains(userId));
        Stream<Long> usersWithoutCompletedOrder = StreamPracticeData.ORDERS.stream()
                .collect(Collectors.groupingBy(
                                StreamPracticeData.Order::userId,
                                Collectors.reducing(
                                        false, o -> status.equals(o.status()), (a,b) -> a || b)
                        )
                )
                .entrySet()
                .stream()
                .filter(e -> !e.getValue())
                .map(Map.Entry::getKey);


        return Stream.concat(usersWithoutOrder, usersWithoutCompletedOrder)
                .map(users::get)
                .sorted(Comparator
                        .comparing(StreamPracticeData.User::age)
                        .thenComparing(StreamPracticeData.User::name)
                );
    }

    /**
     * 3. Самый популярный товар
     *
     * Определить товар, который был продан в наибольшем количестве экземпляров.
     *
     * Учитывать quantity.
     *
     * Если несколько товаров имеют одинаковое количество продаж, выбрать самый дорогой.
     * @return
     */
    public static Optional<StreamPracticeData.Product> mostPopularProduct() {
        return StreamPracticeData.ORDERS.stream()
                .filter(o -> !"CANCELLED".equals(o.status()))
                .flatMap(order -> order.items().stream())
                .collect(Collectors.groupingBy(
                        StreamPracticeData.OrderItem::product,
                        Collectors.summingInt(StreamPracticeData.OrderItem::quantity)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<StreamPracticeData.Product, Integer>comparingByValue()
                        .thenComparing(e -> e.getKey().price(), BigDecimal::compareTo)
                        .reversed()
                )
                .map(Map.Entry::getKey)
                .findFirst();
    }

    /**
     * 4. Самая прибыльная категория
     *
     * Для каждой категории посчитать общую стоимость всех проданных товаров в завершённых заказах.
     *
     * Определить категорию с максимальной выручкой.
     */
    public static Map<String, BigDecimal> mostProfitCategory() {
        return ORDERS.stream()
                .filter(o -> "COMPLETED".equals(o.status()))
                .flatMap(o -> o.items().stream())
                .collect(Collectors
                        .groupingBy(
                                o -> o.product().category(),
                                Collectors.reducing(BigDecimal.ZERO, OrderItem::total, BigDecimal::add)
                        )
                )
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new)
                );
    }

    /**
     * 5. Средний чек пользователей
     *
     * Для каждого пользователя посчитать среднюю стоимость его завершённых заказов.
     *
     * Получить пользователей, чей средний чек выше среднего чека по всем завершённым заказам.
     */
    public static Map<User, BigDecimal> usersWithMoreAvgCostCompletedOrders() {
        BigDecimal avgOrderSum = ORDERS.stream()
                .filter(o -> "COMPLETED".equals(o.status()))
                .collect(Collectors.teeing(
                        Collectors.reducing(BigDecimal.ZERO, Order::total, BigDecimal::add),
                        Collectors.counting(),
                        (sum, count) -> sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP)));


        return ORDERS.stream()
                .filter(o -> "COMPLETED".equals(o.status()))
                .collect(Collectors.groupingBy(
                        Order::userId,
                        Collectors.teeing(
                            Collectors.reducing(BigDecimal.ZERO, Order::total, BigDecimal::add),
                            Collectors.counting(),
                            (sum, count) -> sum.divide(
                                    BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP
                            ))
                        )
                )
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().compareTo(avgOrderSum) > 0)
                .collect(Collectors.toMap(e -> users.get(e.getKey()), Map.Entry::getValue));
    }

    /**
     * 6. Самый дорогой заказ каждого города
     *
     * Определить самый дорогой завершённый заказ среди пользователей каждого города.
     *
     * Результат должен позволять получить:
     *
     * город → заказ
     *
     * Если в городе нет завершённых заказов, город не должен попадать в результат.
     */
    public static Map<String, Order> mostExpensiveOrderOfCity() {
        return ORDERS.stream()
                .filter(o -> "COMPLETED".equals(o.status()))
                .collect(Collectors.toMap(
                        order -> users.get(order.userId()).city(),
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(Order::total)))
                );
    }

    /**
     * 7. Категории каждого пользователя
     *
     * Для каждого пользователя определить все категории товаров, которые он покупал в завершённых заказах.
     *
     * Результат:
     *
     * Map<Long, Set<String>>
     *
     * Пользователи без заказов также должны присутствовать с пустым набором категорий.
     */
    public static Map<Long, Set<String>> userCategories() {
        Stream<Long> allUserId = USERS.stream().mapToLong(User::id).boxed();
        Set<Long> ordersUserId = ORDERS.stream().mapToLong(Order::userId).boxed().collect(Collectors.toSet());

        Map<Long, Set<String>> usersWithoutOrder = allUserId.filter(userId -> !ordersUserId.contains(userId)).collect(Collectors.toMap(u -> u, o -> Collections.emptySet()));

        Map<Long, Set<String>> usersCategory = ORDERS.stream()
                .filter(o -> "COMPLETED".equals(o.status()))
                .collect(Collectors.groupingBy(
                        Order::userId,
                        Collectors.flatMapping(
                                o -> o.items().stream(),
                                Collectors.mapping(
                                        p -> p.product().category(),
                                        Collectors.toSet()))
                        )
                );

        usersCategory.putAll(usersWithoutOrder);

        return usersCategory;

    }

    /**
     * 8. Самый дорогой товар каждого пользователя
     *
     * Для каждого пользователя определить самый дорогой товар, который он когда-либо покупал в завершённых заказах.
     *
     * Если один и тот же товар покупался несколько раз, он всё равно считается одним товаром.
     */
    public static Map<Long, Product> mostExpensiveProduct() {
        /*return userOrders.entrySet()
                .stream()
                .map(e -> Map.entry(
                        e.getKey(),
                        e.getValue()
                                .stream()
                                .filter(o -> "COMPLETED".equals(o.status()))
                                .flatMap(t -> t.items().stream())
                                .map(OrderItem::product)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().max(Comparator.comparing(Product::price)).get()));*/

        return ORDERS.stream()
                .filter(o -> "COMPLETED".equals(o.status()))
                .flatMap(o -> o.items()
                        .stream()
                        .map(p -> Map.entry(o.userId(), p.product())))
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                BinaryOperator.maxBy(Comparator.comparing(Product::price))
                        )
                );
    }

    /**
     * 9. Количество уникальных товаров
     *
     * Для каждого пользователя определить количество различных товаров, которые он покупал.
     *
     * Получить пользователей, купивших как минимум 3 разных товара.
     */
    public static List<User> uniqProducts() {
        userOrders.entrySet().stream().map(e -> e.getValue().stream()
                .map(o -> o.items()
                        .stream()
                        .map(OrderItem::product)
                        .distinct()
                )
        );
        
        return ORDERS.stream()
                .filter(o -> "COMPLETED".equals(o.status()))
                .flatMap(o -> o.items()
                        .stream()
                        .map(OrderItem::product)
                        .distinct()
                        .map(p -> Map.entry(o.userId(), p))
                )
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 3)
                .map(e -> users.get(e.getKey()))
                .collect(Collectors.toList());
    }
}
