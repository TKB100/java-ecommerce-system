public class OrderItem {
    private String itemName;
    private int quant;
    private double price;

    public OrderItem(String productName, int quant, double price) {
        this.itemName = productName;
        this.quant = quant;
        this.price = price;
    }

    public double getSubtotal() {
        return quant * price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quant;
    }

    public double getPrice() {
        return price;
    }
}