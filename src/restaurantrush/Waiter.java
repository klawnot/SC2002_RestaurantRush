package restaurantrush;

public class Waiter {
    // Seat the next customer from queue at a free table
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

    // Take an order from a seated customer without an active order
    public boolean takeOrder(Table table, Menu menu, RestaurantState state) {
        if (table == null || !table.isOccupied()) {
            return false;
        }
        Customer customer = table.getCurrentCustomer();
        if (customer.getOrder() != null) {
            return false;
        }
        MenuItem chosenItem = customer.chooseFood(menu);
        String orderId = "ORD-" + customer.getId();
        Order order = new Order(orderId, customer.getId(), chosenItem);
        customer.setOrder(order);
        state.addOrder(order);
        return true;
    }

    // Serve a dish that was already READY before this turn's action phase
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