package restaurantrush;

import java.util.List;
import java.util.Queue;

public class RestaurantView {

    public void printStartOfTurn(RestaurantState state, boolean waiterAvailable, boolean chefAvailable) {
        System.out.println("==================================================");
        System.out.printf("--- START OF TURN %d / 18 ---%n", state.getCurrentTurn());
        System.out.println("==================================================");

        // Revenue and Satisfaction
        double avgSat = state.getAveragePaidSatisfaction();
        String avgSatDisplay = (avgSat < 0) ? "N/A" : String.format("%.2f", avgSat);
        System.out.printf("Revenue: $%.2f | Paid Avg Satisfaction: %s%n", 
                state.getRevenueCents() / 100.0, avgSatDisplay);

        // Waiting Queue
        Queue<Customer> queue = state.getWaitingQueue();
        System.out.printf("Waiting Queue (%d): ", queue.size());
        if (queue.isEmpty()) {
            System.out.print("[Empty]");
        } else {
            for (Customer c : queue) {
                System.out.printf("[%s (Sat: %d)] ", c.getId(), c.getSatisfaction());
            }
        }
        System.out.println();

        // Tables
        System.out.println("Tables:");
        for (Table table : state.getTables()) {
            if (!table.isOccupied()) {
                System.out.printf("  Table %d: [Empty]%n", table.getTableId());
            } else {
                Customer c = table.getCurrentCustomer();
                String orderInfo = (c.getOrder() == null) ? "No Order Placed" : c.getOrder().toString();
                System.out.printf("  Table %d: Customer %s (Sat: %d, State: %s) | %s%n",
                        table.getTableId(), c.getId(), c.getSatisfaction(), c.getState(), orderInfo);
            }
        }

        // Active Orders in Kitchen
        List<Order> orders = state.getActiveOrders();
        System.out.printf("Kitchen Orders (%d):%n", orders.size());
        if (orders.isEmpty()) {
            System.out.println("  [No active orders in kitchen]");
        } else {
            for (Order o : orders) {
                System.out.printf("  - %s (Customer %s) | Needs %d unit(s) | Status: %s%n",
                        o.getMenuItem().getName(), o.getCustomerId(), o.getUnitsRemaining(), o.getStatus());
            }
        }

        // Staff Tasks Available
        System.out.println("Staff Availability:");
        System.out.printf("  Waiter: %s%n", waiterAvailable ? "Available (Seat / Take Order / Serve)" : "Task Used");
        System.out.printf("  Chef:   %s%n", chefAvailable ? "Available (Cook 1 Unit)" : "Task Used");
        System.out.println("  Cashier: Automatic (Oldest READY_TO_PAY)");
        System.out.println("--------------------------------------------------");
    }

    public void printEndOfTurnSummary(RestaurantState state) {
        System.out.println("--------------------------------------------------");
        System.out.printf("--- END OF TURN %d SUMMARY ---%n", state.getCurrentTurn());
        System.out.printf("Customers Paid: %d%n", state.getPaidCustomers().size());
        System.out.printf("Unhappy Departures: %d%n", state.getAbandonedCustomers().size());
        System.out.printf("Current Revenue: $%.2f%n", state.getRevenueCents() / 100.0);
        
        double avgSat = state.getAveragePaidSatisfaction();
        String avgSatDisplay = (avgSat < 0) ? "N/A" : String.format("%.2f", avgSat);
        System.out.printf("Average Paid Customer Satisfaction: %s%n", avgSatDisplay);
        System.out.println("--------------------------------------------------\n");
    }

    public void printFinalResult(RestaurantState state, boolean isVictory) {
        System.out.println("==================================================");
        System.out.println("                  GAME OVER                       ");
        System.out.println("==================================================");
        System.out.println(isVictory ? "RESULT: VICTORY!" : "RESULT: DEFEAT");

        int paidCount = state.getPaidCustomers().size();
        int revenueCents = state.getRevenueCents();
        double avgSat = state.getAveragePaidSatisfaction();

        System.out.printf("Total Paid Customers: %d / 4 (Target: >= 4) -> %s%n",
                paidCount, paidCount >= 4 ? "PASSED" : "FAILED");
        System.out.printf("Total Revenue: $%.2f / $45.00 (Target: >= $45.00) -> %s%n",
                revenueCents / 100.0, revenueCents >= 4500 ? "PASSED" : "FAILED");
        System.out.printf("Average Satisfaction: %.2f / 60.0 (Target: >= 60.0) -> %s%n",
                avgSat < 0 ? 0.0 : avgSat, avgSat >= 60.0 ? "PASSED" : "FAILED");
        System.out.println("==================================================");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printError(String error) {
        System.out.println("[Error] " + error);
    }
}