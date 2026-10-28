package restaurantrush;

public class CriticCustomer extends Customer {
    public CriticCustomer(String id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    // Food critics are impatient and lose 12 satisfaction points per turn
    @Override
    public int getSatisfactionLossPerTurn() {
        return 12;
    }

    // Critics specifically order the first item listed on the menu
    @Override
    public MenuItem chooseFood(Menu menu) {
        if (menu != null && !menu.getItems().isEmpty()) {
            return menu.getItems().get(0);
        }
        return super.chooseFood(menu);
    }

    @Override
    public String getTypeLabel() {
        return "Critic";
    }
}