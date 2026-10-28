package restaurantrush;

public class Table {
    private final int tableId;
    private Customer currentCustomer;

    public Table(int tableId) {
        this.tableId = tableId;
        this.currentCustomer = null;
    }

    public int getTableId() {
        return tableId;
    }

    public boolean isOccupied() {
        return currentCustomer != null;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void seatCustomer(Customer customer) {
        this.currentCustomer = customer;
        customer.setTable(this);
        customer.setState(CustomerState.SEATED);
    }

    public void vacate() {
        if (this.currentCustomer != null) {
            this.currentCustomer.setTable(null);
            this.currentCustomer = null;
        }
    }

    @Override
    public String toString() {
        if (!isOccupied()) {
            return String.format("Table %d [Empty]", tableId);
        }
        return String.format("Table %d [Customer %s]", tableId, currentCustomer.getId());
    }
}