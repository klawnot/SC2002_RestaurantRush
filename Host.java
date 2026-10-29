package restaurantrush;

import java.util.ArrayList;
import java.util.List;

public class Host {

    public List<Integer> getAvailableTableIds(RestaurantState state) {
        List<Integer> freeTableIds = new ArrayList<>();
        if (state.getWaitingQueue().isEmpty()) {
            return freeTableIds;
        }
        for (Table t : state.getTables()) {
            if (!t.isOccupied()) {
                freeTableIds.add(t.getTableId());
            }
        }
        return freeTableIds;
    }
    
    // Seats at most one waiting customer at an unoccupied table
    public Customer seatCustomer(RestaurantState state, int tableId) {
        if (state.getWaitingQueue().isEmpty()) {
            return null;
        }
        Table table = state.findTableById(tableId);
        if (table == null || table.isOccupied()) {
            return null;
        }
        Customer customer = state.getWaitingQueue().poll();
        table.seatCustomer(customer);
        return customer;
    }
}