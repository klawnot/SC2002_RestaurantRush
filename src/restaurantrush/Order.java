package restaurantrush;

public class Order {
    private final String orderId;
    private final String customerId;
    private final MenuItem menuItem;
    private final int finalPriceCents;
    private int unitsRemaining;
    private OrderStatus status;

    public Order(String orderId, String customerId, MenuItem menuItem, int finalPriceCents) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.menuItem = menuItem;
        this.finalPriceCents = finalPriceCents;
        this.unitsRemaining = menuItem.getPrepUnitsRequired();
        this.status = OrderStatus.ORDERED;
    }

    public Order(String orderId, String customerId, MenuItem menuItem) {
        this(orderId, customerId, menuItem, menuItem.getPriceCents());
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getFinalPriceCents() {
        return finalPriceCents;
    }

    public int getUnitsRemaining() {
        return unitsRemaining;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void advancePreparation() {
        if (unitsRemaining > 0) {
            unitsRemaining--;
            if (unitsRemaining == 0) {
                this.status = OrderStatus.READY;
            }
        }
    }

    public boolean isReady() {
        return status == OrderStatus.READY;
    }

    public boolean isServed() {
        return status == OrderStatus.SERVED;
    }

    @Override
    public String toString() {
        return String.format("Order %s [%s] for Customer %s - Remaining Units: %d, Status: %s",
                orderId, menuItem.getName(), customerId, unitsRemaining, status);
    }
}