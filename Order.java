import java.util.List;

public class Order {
    private String orderID;
    private String ID;
    private String orderDate;
    private List<OrderItem> items;
    private double deliveryFee;
    private double subtotal;
    private double tax;
    private double total;
    private String authoNum;

    public Order(String orderID, String ID, String orderDate, List<OrderItem> items,
                 double deliveryFee, double subtotal, double tax, double total, String authoNum) {
        this.orderID = orderID;
        this.ID = ID;
        this.orderDate = orderDate;
        this.items = items;
        this.deliveryFee = deliveryFee;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.authoNum = authoNum;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getCustomerID() {
        return ID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public double getauthoNum() {
        return Double.parseDouble(authoNum);
    }
}