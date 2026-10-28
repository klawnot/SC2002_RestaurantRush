package restaurantrush;

public class ComboMeal extends MenuItem {
    private final MenuItem firstItem;
    private final MenuItem secondItem;

    public ComboMeal(MenuItem firstItem, MenuItem secondItem) {
        super(
            firstItem.getName() + " & " + secondItem.getName() + " Combo",
            (int) Math.round((firstItem.getPriceCents() + secondItem.getPriceCents()) * 0.90),
            firstItem.getPrepUnitsRequired() + secondItem.getPrepUnitsRequired()
        );
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    public MenuItem getFirstItem() {
        return firstItem;
    }

    public MenuItem getSecondItem() {
        return secondItem;
    }
}