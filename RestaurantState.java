package restaurantrush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RestaurantState {
    private int currentTurn;
    private int revenueCents;
    private final Queue<Customer> waitingQueue;
    private final List<Table> tables;
    private final List<Order> activeOrders;
    private final List<Customer> paidCustomers;
    private final List<Customer> abandonedCustomers;
    private boolean happyHourUsed = false;
    private int happyHourTurnsRemaining = 0;

    public RestaurantState() {
        this.currentTurn = 1;
        this.revenueCents = 0;
        this.waitingQueue = new LinkedList<>();
        this.tables = new ArrayList<>();
        this.activeOrders = new ArrayList<>();
        this.paidCustomers = new ArrayList<>();
        this.abandonedCustomers = new ArrayList<>();

        tables.add(new Table(1));
        tables.add(new Table(2));
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void advanceTurn() {
        this.currentTurn++;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public void addRevenue(int cents) {
        this.revenueCents += cents;
    }

    public Queue<Customer> getWaitingQueue() {
        return waitingQueue;
    }

    public List<Table> getTables() {
        return Collections.unmodifiableList(tables);
    }

    public List<Order> getActiveOrders() {
        return Collections.unmodifiableList(activeOrders);
    }

    public List<Customer> getPaidCustomers() {
        return Collections.unmodifiableList(paidCustomers);
    }

    public List<Customer> getAbandonedCustomers() {
        return Collections.unmodifiableList(abandonedCustomers);
    }

    public void addOrder(Order order) {
        this.activeOrders.add(order);
    }

    public void recordPaidCustomer(Customer customer) {
        this.paidCustomers.add(customer);
    }

    public void recordAbandonedCustomer(Customer customer) {
        this.abandonedCustomers.add(customer);
    }

    public Table findTableById(int tableId) {
        for (Table table : tables) {
            if (table.getTableId() == tableId) {
                return table;
            }
        }
        return null;
    }

    public Table getFirstFreeTable() {
        for (Table table : tables) {
            if (!table.isOccupied()) {
                return table;
            }
        }
        return null;
    }

    public double getAveragePaidSatisfaction() {
        if (paidCustomers.isEmpty()) {
            return -1.0;
        }
        int totalSatisfaction = 0;
        for (Customer c : paidCustomers) {
            totalSatisfaction += c.getSatisfaction();
        }
        return (double) totalSatisfaction / paidCustomers.size();
    }

    public boolean canActivateHappyHour() {
        return !happyHourUsed;
    }

    public void activateHappyHour() {
        if (!happyHourUsed) {
            this.happyHourUsed = true;
            this.happyHourTurnsRemaining = 2;
        }
    }

    public boolean isHappyHourActive() {
        return happyHourTurnsRemaining > 0;
    }

    public int getHappyHourTurnsRemaining() {
        return happyHourTurnsRemaining;
    }

    public void decrementHappyHour() {
        if (happyHourTurnsRemaining > 0) {
            happyHourTurnsRemaining--;
        }
    }

    public List<Order> getReadyOrders() {
        List<Order> ready = new ArrayList<>();
        for (Order o : activeOrders) {
            if (o.isReady()) {
                ready.add(o);
            }
        }
        return ready;
    }

    public List<Order> getCookableOrders() {
        List<Order> cookable = new ArrayList<>();
        for (Order o : activeOrders) {
            if (o.getStatus() == OrderStatus.ORDERED) {
                cookable.add(o);
            }
        }
        return cookable;
    }

    public void cancelCustomerOrder(Customer customer) {
        if (customer == null) {
            return;
        }
        Order order = customer.getOrder();
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            this.activeOrders.remove(order);
        }
    }
}