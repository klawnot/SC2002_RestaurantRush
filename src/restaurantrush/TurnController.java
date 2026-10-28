package restaurantrush;

import java.util.ArrayList;
import java.util.List;

public class TurnController {
    private final RestaurantState state;
    private final RestaurantView view;
    private final InputHandler input;
    private final Menu menu;
    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;
    private int customerCounter = 1;

    // Prevents customer served in current turn from immediately moving to ready-to-pay
    private Customer customerServedThisTurn = null;

    public TurnController(RestaurantState state, RestaurantView view, InputHandler input, Menu menu) {
        this.state = state;
        this.view = view;
        this.input = input;
        this.menu = menu;
        this.waiter = new Waiter();
        this.chef = new Chef();
        this.cashier = new Cashier();
    }

    public void runGame() {
        while (state.getCurrentTurn() <= 18) {
            executeTurn();
            if (state.getCurrentTurn() <= 18) {
                state.advanceTurn();
            } else {
                break;
            }
        }
        evaluateGameResult();
    }

    private void executeTurn() {
        int turn = state.getCurrentTurn();
        customerServedThisTurn = null;

        // Step 1: Handle scheduled arrivals
        handleArrivals(turn);

        // Snapshot dishes that finished cooking in earlier turns
        List<Order> readyBeforeTurn = getReadyOrdersSnapshot();

        view.printStartOfTurn(state, true, true);

        // Steps 2 & 3: Staff actions
        handleWaiterAction(readyBeforeTurn);
        handleChefAction();

        // Step 4: Eating updates and cashier checkout
        handleEatingAndPayment();

        // Step 5: End-of-turn satisfaction loss and departures
        handleWaitingAndAbandonment();

        view.printEndOfTurnSummary(state);
    }

    // Factory method spawning specialized customer subtypes on specific turns
    private Customer createCustomer(String id, int turn) {
        if (turn == 7) {
            return new VIPCustomer(id, turn);
        } else if (turn == 13) {
            return new CriticCustomer(id, turn);
        } else {
            return new Customer(id, turn);
        }
    }

    private void handleArrivals(int turn) {
        if (turn == 1 || turn == 4 || turn == 7 || turn == 10 || turn == 13 || turn == 16) {
            String customerId = "C" + customerCounter++;
            Customer newCustomer = createCustomer(customerId, turn);
            state.getWaitingQueue().add(newCustomer);
            view.printMessage(String.format("Arrival: [%s] %s joined the waiting queue.", 
                    newCustomer.getTypeLabel(), newCustomer.getId()));
        }
    }

    private List<Order> getReadyOrdersSnapshot() {
        List<Order> readyOrders = new ArrayList<>();
        for (Order o : state.getActiveOrders()) {
            if (o.getStatus() == OrderStatus.READY) {
                readyOrders.add(o);
            }
        }
        return readyOrders;
    }

    private void handleWaiterAction(List<Order> readyBeforeTurn) {
        boolean actionTaken = false;
        while (!actionTaken) {
            int choice = input.getWaiterChoice();
            switch (choice) {
                case 1: { // Seat customer
                    if (state.getWaitingQueue().isEmpty()) {
                        view.printError("Waiting queue is empty. Cannot seat anyone.");
                        break;
                    }
                    Table freeTable = state.getFirstFreeTable();
                    if (freeTable == null) {
                        view.printError("No tables available. Both tables are currently occupied.");
                        break;
                    }
                    int tableId = input.getIntInput("Enter table number to seat customer (" + freeTable.getTableId() + "): ", 1, 2);
                    Table chosenTable = state.findTableById(tableId);
                    if (chosenTable == null || chosenTable.isOccupied()) {
                        view.printError("Selected table is either invalid or already occupied.");
                        break;
                    }
                    Customer seatedCustomer = state.getWaitingQueue().peek();
                    waiter.seatCustomer(state, tableId);
                    view.printMessage(String.format("Waiter seated Customer %s at Table %d.", seatedCustomer.getId(), tableId));
                    actionTaken = true;
                    break;
                }
                case 2: { // Take order
                    List<Table> orderableTables = new ArrayList<>();
                    for (Table t : state.getTables()) {
                        if (t.isOccupied() && t.getCurrentCustomer().getOrder() == null) {
                            orderableTables.add(t);
                        }
                    }
                    if (orderableTables.isEmpty()) {
                        view.printError("No seated customers waiting to order.");
                        break;
                    }
                    System.out.println("Select a table to take order from:");
                    for (Table t : orderableTables) {
                        System.out.printf("Table %d (Customer %s)%n", t.getTableId(), t.getCurrentCustomer().getId());
                    }
                    int tableId = input.getIntInput("Enter table number: ", 1, 2);
                    Table selectedTable = state.findTableById(tableId);
                    if (selectedTable == null || !selectedTable.isOccupied() || selectedTable.getCurrentCustomer().getOrder() != null) {
                        view.printError("Table " + tableId + " is not waiting to place an order.");
                        break;
                    }
                    waiter.takeOrder(selectedTable, menu, state);
                    Customer c = selectedTable.getCurrentCustomer();
                    view.printMessage(String.format("Waiter took order for Customer %s: %s.", c.getId(), c.getOrder().getMenuItem().getName()));
                    actionTaken = true;
                    break;
                }
                case 3: { // Serve order
                    if (readyBeforeTurn.isEmpty()) {
                        view.printError("No dishes were READY before this turn started. Cannot serve.");
                        break;
                    }
                    System.out.println("Select a READY order to serve:");
                    for (int i = 0; i < readyBeforeTurn.size(); i++) {
                        Order o = readyBeforeTurn.get(i);
                        System.out.printf("%d: %s for Customer %s%n", i + 1, o.getMenuItem().getName(), o.getCustomerId());
                    }
                    int orderIdx = input.getIntInput("Choose order (1-" + readyBeforeTurn.size() + "): ", 1, readyBeforeTurn.size()) - 1;
                    Order selectedOrder = readyBeforeTurn.get(orderIdx);
                    waiter.serveOrder(selectedOrder, state);

                    for (Table table : state.getTables()) {
                        if (table.isOccupied() && table.getCurrentCustomer().getId().equals(selectedOrder.getCustomerId())) {
                            customerServedThisTurn = table.getCurrentCustomer();
                            break;
                        }
                    }
                    view.printMessage(String.format("Waiter served %s to Customer %s.", selectedOrder.getMenuItem().getName(), selectedOrder.getCustomerId()));
                    actionTaken = true;
                    break;
                }
                case 4: { // Wait
                    view.printMessage("Waiter waits this turn.");
                    actionTaken = true;
                    break;
                }
                default:
                    view.printError("Invalid selection.");
                    break;
            }
        }
    }

    private void handleChefAction() {
        List<Order> cookableOrders = new ArrayList<>();
        for (Order o : state.getActiveOrders()) {
            if (o.getStatus() == OrderStatus.ORDERED) {
                cookableOrders.add(o);
            }
        }
        Order chosenOrder = input.getChefChoice(cookableOrders);
        if (chosenOrder != null) {
            chef.cookOrder(chosenOrder);
            view.printMessage(String.format("Chef worked on %s (Customer %s). Remaining units: %d.",
                    chosenOrder.getMenuItem().getName(), chosenOrder.getCustomerId(), chosenOrder.getUnitsRemaining()));
            if (chosenOrder.isReady()) {
                view.printMessage(String.format("Order for Customer %s is now READY to be served on a later turn.", chosenOrder.getCustomerId()));
            }
        } else {
            view.printMessage("Chef waits this turn.");
        }
    }

    private void handleEatingAndPayment() {
        for (Table table : state.getTables()) {
            if (table.isOccupied()) {
                Customer c = table.getCurrentCustomer();
                if (c.getState() == CustomerState.SERVED_EATING && c != customerServedThisTurn) {
                    c.setState(CustomerState.READY_TO_PAY);
                    view.printMessage(String.format("Customer %s finished eating and is now READY_TO_PAY.", c.getId()));
                }
            }
        }

        Customer paidCustomer = cashier.processPayment(state);
        if (paidCustomer != null) {
            view.printMessage(String.format("Cashier collected payment from Customer %s. Table is now vacant.",
                    paidCustomer.getId()));
        }
    }

    private void handleWaitingAndAbandonment() {
        List<Customer> abandonedInQueue = new ArrayList<>();
        for (Customer c : state.getWaitingQueue()) {
            c.applyTurnDecay();
            if (c.getState() == CustomerState.ABANDONED) {
                abandonedInQueue.add(c);
            }
        }
        for (Customer c : abandonedInQueue) {
            state.getWaitingQueue().remove(c);
            state.recordAbandonedCustomer(c);
            view.printMessage(String.format("Customer %s reached 0 satisfaction and left the queue.", c.getId()));
        }

        for (Table table : state.getTables()) {
            if (table.isOccupied()) {
                Customer c = table.getCurrentCustomer();
                c.applyTurnDecay();
                if (c.getState() == CustomerState.ABANDONED) {
                    if (c.getOrder() != null) {
                        c.getOrder().setStatus(OrderStatus.CANCELLED);
                        state.removeActiveOrder(c.getOrder());
                    }
                    table.vacate();
                    state.recordAbandonedCustomer(c);
                    view.printMessage(String.format("Customer %s reached 0 satisfaction and left Table %d without paying.", c.getId(), table.getTableId()));
                }
            }
        }
    }

    private void evaluateGameResult() {
        boolean paidTargetMet = state.getPaidCustomers().size() >= 4;
        boolean revenueTargetMet = state.getRevenueCents() >= 4500;
        boolean satisfactionTargetMet = state.getAveragePaidSatisfaction() >= 60.0;
        boolean isVictory = paidTargetMet && revenueTargetMet && satisfactionTargetMet;
        view.printFinalResult(state, isVictory);
    }
}