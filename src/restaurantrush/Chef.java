package restaurantrush;

public class Chef {
    // Progress cooking by 1 prep unit on the assigned order
    public boolean cookOrder(Order order) {
        if (order == null || order.getStatus() != OrderStatus.ORDERED) {
            return false;
        }
        order.advancePreparation();
        return true;
    }
}