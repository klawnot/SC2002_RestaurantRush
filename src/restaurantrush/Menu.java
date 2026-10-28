package restaurantrush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Menu {
    private final List<MenuItem> items;
    private final Random rng;

    public Menu() {
        this(new Random());
    }

    public Menu(Random rng) {
        this.rng = rng;
        this.items = new ArrayList<>();
        initializeDefaultMenu();
    }

    private void initializeDefaultMenu() {
        items.add(new MenuItem("Salad", 900, 1));
        items.add(new MenuItem("Burger", 1500, 1));
        items.add(new MenuItem("Pasta", 1800, 2));
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public MenuItem getRandomItem() {
        return items.get(rng.nextInt(items.size()));
    }

    public MenuItem getItemByName(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}