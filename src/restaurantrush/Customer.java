package restaurantrush;

public class Customer {
    private final String id;
    private final int arrivalTurn;
    private int satisfaction;
    private CustomerState state;
    private Table table;
    private Order order;

    public Customer(String id, int arrivalTurn) {
        this.id = id;
        this.arrivalTurn = arrivalTurn;
        this.satisfaction = 100;
        this.state = CustomerState.WAITING;
        this.table = null;
        this.order = null;
    }

    public String getId() {
        return id;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = Math.max(0, Math.min(100, satisfaction));
    }

    public CustomerState getState() {
        return state;
    }

    public void setState(CustomerState state) {
        this.state = state;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // Regular customers lose 8 satisfaction per turn while waiting or seated without food
    public int getSatisfactionLossPerTurn() {
        return 8;
    }

    // Regular customers pay full price
    public int calculateBill() {
        if (order == null || order.getMenuItem() == null) {
            return 0;
        }
        return order.getMenuItem().getPriceCents();
    }

    // Customer randomly picks an item from the menu
    public MenuItem chooseFood(Menu menu) {
        return menu.getRandomItem();
    }

    // Reduce satisfaction at end of turn if still waiting for food
    public void applyTurnDecay() {
        if (state == CustomerState.WAITING || state == CustomerState.SEATED) {
            this.satisfaction = Math.max(0, this.satisfaction - getSatisfactionLossPerTurn());
            if (this.satisfaction == 0) {
                this.state = CustomerState.ABANDONED;
            }
        }
    }

    public String getTypeLabel() {
        return "Standard";
    }

    @Override
    public String toString() {
        return String.format("Customer %s [Sat: %d, State: %s]", id, satisfaction, state);
    }
}