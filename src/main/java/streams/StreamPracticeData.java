package streams;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class StreamPracticeData {

    public record User(
            long id,
            String name,
            int age,
            String city,
            boolean active,
            LocalDate registrationDate
    ) {}

    public record Product(
            long id,
            String name,
            String category,
            BigDecimal price
    ) {}

    public record OrderItem(
            Product product,
            int quantity
    ) {
        public BigDecimal total() {
            return product.price().multiply(BigDecimal.valueOf(quantity));
        }
    }

    public record Order(
            long id,
            long userId,
            LocalDate date,
            String status,
            String paymentMethod,
            List<OrderItem> items
    ) {
        public BigDecimal total() {
            return items.stream()
                    .map(OrderItem::total)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    public static final List<User> USERS = List.of(
            new User(1, "Алексей", 25, "Москва", true,
                    LocalDate.of(2023, 1, 15)),
            new User(2, "Мария", 31, "Санкт-Петербург", true,
                    LocalDate.of(2022, 7, 10)),
            new User(3, "Иван", 19, "Москва", false,
                    LocalDate.of(2024, 2, 20)),
            new User(4, "Ольга", 42, "Казань", true,
                    LocalDate.of(2021, 11, 5)),
            new User(5, "Дмитрий", 28, "Москва", true,
                    LocalDate.of(2023, 5, 17)),
            new User(6, "Елена", 35, "Новосибирск", true,
                    LocalDate.of(2022, 3, 12)),
            new User(7, "Сергей", 23, "Казань", false,
                    LocalDate.of(2024, 1, 8)),
            new User(8, "Анна", 29, "Москва", true,
                    LocalDate.of(2023, 9, 1)),
            new User(9, "Максим", 38, "Санкт-Петербург", true,
                    LocalDate.of(2021, 6, 25)),
            new User(10, "Наталья", 26, "Новосибирск", true,
                    LocalDate.of(2024, 3, 3)),
            new User(11, "Павел", 45, "Москва", false,
                    LocalDate.of(2020, 12, 19)),
            new User(12, "Виктория", 33, "Казань", true,
                    LocalDate.of(2022, 10, 30))
    );

    public static final List<Product> PRODUCTS = List.of(
            new Product(1, "MacBook Pro", "Ноутбуки",
                    new BigDecimal("220000")),
            new Product(2, "ThinkPad X1", "Ноутбуки",
                    new BigDecimal("180000")),
            new Product(3, "iPhone 17", "Смартфоны",
                    new BigDecimal("120000")),
            new Product(4, "Samsung Galaxy S26", "Смартфоны",
                    new BigDecimal("95000")),
            new Product(5, "iPad Pro", "Планшеты",
                    new BigDecimal("110000")),
            new Product(6, "Galaxy Tab", "Планшеты",
                    new BigDecimal("75000")),
            new Product(7, "AirPods Pro", "Аксессуары",
                    new BigDecimal("30000")),
            new Product(8, "Sony WH-1000XM6", "Аксессуары",
                    new BigDecimal("45000")),
            new Product(9, "Logitech MX Master", "Аксессуары",
                    new BigDecimal("12000")),
            new Product(10, "Keychron K2", "Аксессуары",
                    new BigDecimal("15000")),
            new Product(11, "Dell UltraSharp", "Мониторы",
                    new BigDecimal("80000")),
            new Product(12, "LG UltraGear", "Мониторы",
                    new BigDecimal("65000"))
    );

    public static final List<Order> ORDERS = List.of(
            new Order(
                    1001, 1,
                    LocalDate.of(2025, 1, 10),
                    "COMPLETED",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(0), 1),
                            new OrderItem(PRODUCTS.get(6), 2)
                    )
            ),

            new Order(
                    1002, 2,
                    LocalDate.of(2025, 1, 12),
                    "COMPLETED",
                    "SBP",
                    List.of(
                            new OrderItem(PRODUCTS.get(2), 1),
                            new OrderItem(PRODUCTS.get(7), 1)
                    )
            ),

            new Order(
                    1003, 3,
                    LocalDate.of(2025, 1, 15),
                    "CANCELLED",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(3), 1)
                    )
            ),

            new Order(
                    1004, 4,
                    LocalDate.of(2025, 1, 18),
                    "COMPLETED",
                    "CASH",
                    List.of(
                            new OrderItem(PRODUCTS.get(4), 1),
                            new OrderItem(PRODUCTS.get(9), 1)
                    )
            ),

            new Order(
                    1005, 5,
                    LocalDate.of(2025, 1, 20),
                    "PROCESSING",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(1), 1),
                            new OrderItem(PRODUCTS.get(8), 2)
                    )
            ),

            new Order(
                    1006, 1,
                    LocalDate.of(2025, 2, 2),
                    "COMPLETED",
                    "SBP",
                    List.of(
                            new OrderItem(PRODUCTS.get(10), 2),
                            new OrderItem(PRODUCTS.get(9), 1)
                    )
            ),

            new Order(
                    1007, 6,
                    LocalDate.of(2025, 2, 5),
                    "COMPLETED",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(5), 1),
                            new OrderItem(PRODUCTS.get(6), 1)
                    )
            ),

            new Order(
                    1008, 7,
                    LocalDate.of(2025, 2, 8),
                    "CANCELLED",
                    "CASH",
                    List.of(
                            new OrderItem(PRODUCTS.get(11), 1)
                    )
            ),

            new Order(
                    1009, 8,
                    LocalDate.of(2025, 2, 10),
                    "COMPLETED",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(2), 2),
                            new OrderItem(PRODUCTS.get(6), 1)
                    )
            ),

            new Order(
                    1010, 9,
                    LocalDate.of(2025, 2, 14),
                    "COMPLETED",
                    "SBP",
                    List.of(
                            new OrderItem(PRODUCTS.get(0), 1),
                            new OrderItem(PRODUCTS.get(10), 1)
                    )
            ),

            new Order(
                    1011, 10,
                    LocalDate.of(2025, 2, 17),
                    "PROCESSING",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(3), 1),
                            new OrderItem(PRODUCTS.get(7), 1)
                    )
            ),

            new Order(
                    1012, 11,
                    LocalDate.of(2025, 2, 20),
                    "COMPLETED",
                    "CASH",
                    List.of(
                            new OrderItem(PRODUCTS.get(4), 2)
                    )
            ),

            new Order(
                    1013, 12,
                    LocalDate.of(2025, 2, 23),
                    "COMPLETED",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(1), 1),
                            new OrderItem(PRODUCTS.get(11), 1)
                    )
            ),

            new Order(
                    1014, 2,
                    LocalDate.of(2025, 3, 1),
                    "COMPLETED",
                    "SBP",
                    List.of(
                            new OrderItem(PRODUCTS.get(5), 1),
                            new OrderItem(PRODUCTS.get(8), 1)
                    )
            ),

            new Order(
                    1015, 5,
                    LocalDate.of(2025, 3, 3),
                    "COMPLETED",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(2), 1),
                            new OrderItem(PRODUCTS.get(9), 2)
                    )
            ),

            new Order(
                    1016, 6,
                    LocalDate.of(2025, 3, 5),
                    "CANCELLED",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(0), 1)
                    )
            ),

            new Order(
                    1017, 8,
                    LocalDate.of(2025, 3, 7),
                    "COMPLETED",
                    "CASH",
                    List.of(
                            new OrderItem(PRODUCTS.get(7), 2),
                            new OrderItem(PRODUCTS.get(8), 1)
                    )
            ),

            new Order(
                    1018, 9,
                    LocalDate.of(2025, 3, 10),
                    "PROCESSING",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(10), 1)
                    )
            ),

            new Order(
                    1019, 10,
                    LocalDate.of(2025, 3, 12),
                    "COMPLETED",
                    "SBP",
                    List.of(
                            new OrderItem(PRODUCTS.get(4), 1),
                            new OrderItem(PRODUCTS.get(6), 2)
                    )
            ),

            new Order(
                    1020, 12,
                    LocalDate.of(2025, 3, 15),
                    "COMPLETED",
                    "CARD",
                    List.of(
                            new OrderItem(PRODUCTS.get(3), 1),
                            new OrderItem(PRODUCTS.get(11), 1)
                    )
            )
    );
}
