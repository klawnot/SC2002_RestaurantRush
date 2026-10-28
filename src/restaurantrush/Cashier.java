package restaurantrush;

import java.util.ArrayList;
import java.util.List;

public class Cashier {
    public Customer processPayment(RestaurantState state) {
        List<Customer> readyCustomers = new ArrayList<>();
        for (Table table : state.getTables()) {
            if (table.isOccupied()) {
                Customer c = table.getCurrentCustomer();
                if (c.getState() == CustomerState.READY_TO_PAY) {
                    readyCustomers.add(c);
                }
            }
        }

        if (readyCustomers.isEmpty()) {
            return null;
        }

        // Cashier serves the customer who arrived earliest
        Customer oldestCustomer = readyCustomers.get(0);
        for (int i = 1; i < readyCustomers.size(); i++) {
            Customer current = readyCustomers.get(i);
            if (current.getArrivalTurn() < oldestCustomer.getArrivalTurn()) {
                oldestCustomer = current;
            }
        }

        // Add payment to restaurant revenue
        int billCents = oldestCustomer.calculateBill();
        state.addRevenue(billCents);

        // Update records and release table
        oldestCustomer.setState(CustomerState.PAID);
        if (oldestCustomer.getOrder() != null) {
            oldestCustomer.getOrder().setStatus(OrderStatus.PAID);
            state.removeActiveOrder(oldestCustomer.getOrder());
        }
        state.recordPaidCustomer(oldestCustomer);

        Table table = oldestCustomer.getTable();
        if (table != null) {
            table.vacate();
        }

        return oldestCustomer;
    }
}