import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<CartItem> items;
    private double TaxRate;

    public ShoppingCart() {
        this.items = new ArrayList<>();
        this.TaxRate = 0.0825;
    }

    public void addItem(Product product, int quantity) {
        items.add(new CartItem(product, quantity));
    }

    public void removeItem(String productID) {
        items.removeIf(item -> item.getProduct().getProductID().equals(productID));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : items) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    public double getTax() {
        return getSubtotal() * TaxRate;
    }

    public double getTotal() {
        return getSubtotal() + getTax();
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Shopping Cart:\n");
        for (CartItem item : items) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("Subtotal: $").append(String.format("%.2f", getSubtotal())).append("\n");
        sb.append("Tax: $").append(String.format("%.2f", getTax())).append("\n");
        sb.append("Total: $").append(String.format("%.2f", getTotal()));
        return sb.toString();
    }
}