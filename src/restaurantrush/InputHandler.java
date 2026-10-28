package restaurantrush;

import java.util.List;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public int getIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("[Error] Please enter an integer between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("[Error] Invalid input. Please enter a valid number.");
            }
        }
    }

    public int getWaiterChoice() {
        System.out.println("\n--- Waiter Action ---");
        System.out.println("1: Seat a waiting customer");
        System.out.println("2: Take an order from a seated customer");
        System.out.println("3: Serve a READY order");
        System.out.println("4: Wait (Do nothing)");
        return getIntInput("Choose Waiter task (1-4): ", 1, 4);
    }

    public Order getChefChoice(List<Order> activeOrders) {
        System.out.println("\n--- Chef Action ---");
        System.out.println("0: Wait (Do nothing)");
        if (activeOrders.isEmpty()) {
            System.out.println("[No orders available in kitchen to cook]");
            getIntInput("Enter 0 to continue: ", 0, 0);
            return null;
        }
        for (int i = 0; i < activeOrders.size(); i++) {
            Order o = activeOrders.get(i);
            System.out.printf("%d: Cook %s (Customer %s, %d unit(s) remaining, Status: %s)%n",
                    i + 1, o.getMenuItem().getName(), o.getCustomerId(), o.getUnitsRemaining(), o.getStatus());
        }
        int choice = getIntInput("Choose order for Chef to cook (0-" + activeOrders.size() + "): ", 0, activeOrders.size());
        if (choice == 0) {
            return null;
        }
        return activeOrders.get(choice - 1);
    }

    public boolean getHappyHourChoice() {
        System.out.println("\n--- Happy Hour Promotion ---");
        System.out.println("Happy Hour is available! (Lasts 2 turns: +15 sat boost, 20% discount on new orders)");
        int choice = getIntInput("Activate Happy Hour this turn? (1: Yes, 2: No): ", 1, 2);
        return choice == 1;
    }

    public int getHostChoice(List<Integer> availableTableIds) {
        System.out.println("\n--- Host Action ---");
        System.out.println("0: Skip Host seating");
        for (int tableId : availableTableIds) {
            System.out.printf("Table %d: Available%n", tableId);
        }
        return getIntInput("Choose table for Host to seat next customer (or 0 to skip): ", 0, 2);
    }
}