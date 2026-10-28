package restaurantrush;

public enum OrderStatus {
    ORDERED,    // Order placed, waiting for Chef
    READY,      // All prep units finished, ready for Waiter to serve
    SERVED,     // Delivered to table, customer is eating
    PAID,       // Customer finished and paid
    CANCELLED   // Customer abandoned before being served
}