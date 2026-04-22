public class Product {
    private String productId;
    private String name;
    private String description;
    private double regularPrice;
    private double salesPrice;

    public Product(String productid, String name, String description,
                   double regularPrice, double salesPrice) {
        this.productId = productid;
        this.name = name;
        this.description = description;
        this.regularPrice = regularPrice;
        this.salesPrice = salesPrice;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getProductID() {
        return productId;
    }

    public double getPrice() {
        return salesPrice;
    }
}