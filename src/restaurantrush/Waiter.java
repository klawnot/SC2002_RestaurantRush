package restaurantrush;

public class Waiter {
    public boolean seatCustomer(RestaurantState state, int tableId) {
        if (state.getWaitingQueue().isEmpty()) {
            return false;
        }
        Table table = state.findTableById(tableId);
        if (table == null || table.isOccupied()) {
            return false;
        }
        Customer customer = state.getWaitingQueue().poll();
        table.seatCustomer(customer);
        return true;
    }

    public boolean takeOrder(Table table, Menu menu, RestaurantState state) {
        if (table == null || !table.isOccupied()) {
            return false;
        }
        Customer customer = table.getCurrentCustomer();
        if (customer.getOrder() != null) {
            return false;
        }

        MenuItem chosenItem = customer.chooseFood(menu);
        int finalPriceCents = chosenItem.getPriceCents();

        // Apply VIP 10% discount first
        if (customer instanceof VIPCustomer) {
            finalPriceCents = (int) Math.round(finalPriceCents * 0.90);
        }

        // Apply Happy Hour 20% promotional discount if active
        if (state.isHappyHourActive()) {
            finalPriceCents = (int) Math.round(finalPriceCents * 0.80);
        }

        String orderId = "ORD-" + customer.getId();
        Order order = new Order(orderId, customer.getId(), chosenItem, finalPriceCents);
        customer.setOrder(order);
        state.addOrder(order);
        return true;
    }

    public boolean serveOrder(Order order, RestaurantState state) {
        if (order == null || order.getStatus() != OrderStatus.READY) {
            return false;
        }
        order.setStatus(OrderStatus.SERVED);

        for (Table table : state.getTables()) {
            if (table.isOccupied() && table.getCurrentCustomer().getId().equals(order.getCustomerId())) {
                table.getCurrentCustomer().setState(CustomerState.SERVED_EATING);
                break;
            }
        }
        return true;
    }
}