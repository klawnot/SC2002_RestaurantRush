package restaurantrush;

public enum CustomerState {
    WAITING,       // In the arrival queue
    SEATED,        // Seated at a table, order may or may not be taken
    SERVED_EATING, // Food served, spending the current turn eating
    READY_TO_PAY,  // Done eating, waiting for Cashier
    PAID,          // Payment collected, departed happily
    ABANDONED      // Satisfaction reached 0, departed without paying
}