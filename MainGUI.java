import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.*;

public class MainGUI extends Application {
    private Map<String, Customer> customers = new HashMap<>();
    private List<Product> productCatalog = new ArrayList<>();
    private Map<String, List<Order>> customerOrders = new HashMap<>();
    private BankService bankService = new BankService();
    private int orderCounter = 1000;

    private Customer currentCustomer = null;
    private ShoppingCart currentCart = new ShoppingCart();

    private Stage primaryStage;

    private TableView<ProductDisplay> productTable;
    private TextArea cartArea, orderArea;
    private Label welcomeLabel;

    // Store button panel so we can update it
    private HBox browseBtnPanel;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("VIP Concert Ticket System");

        initializeProducts();

        showMainMenu();
        primaryStage.show();
    }

    private void initializeProducts() {
        productCatalog.add(new Product("VIP001", "Travis Scott VIP", "Circus Maximus - Front Row + Meet & Greet", 850.00, 749.99));
        productCatalog.add(new Product("VIP002", "Playboi Carti VIP", "Antagonist Tour - VIP Pit", 600.00, 499.99));
        productCatalog.add(new Product("VIP003", "Don Toliver VIP", "Hardstone Psycho - VIP + Merch", 450.00, 379.99));
        productCatalog.add(new Product("VIP004", "Future VIP", "Mixtape Pluto - Backstage Pass", 700.00, 599.99));
        productCatalog.add(new Product("VIP005", "Metro Boomin VIP", "Heroes & Villains - VIP Booth", 550.00, 449.99));
        productCatalog.add(new Product("VIP006", "21 Savage VIP", "American Dream - Floor Seats", 500.00, 419.99));
        productCatalog.add(new Product("VIP007", "Ken Carson VIP", "A Great Chaos - VIP Pit", 350.00, 279.99));
        productCatalog.add(new Product("VIP008", "Yeat VIP", "2 Alive Tour - Front Section", 400.00, 329.99));
    }

    private void showMainMenu() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(50));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("VIP Concert Ticket System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        Button createBtn = new Button("Create Account");
        Button loginBtn = new Button("Log On");
        Button browseBtn = new Button("Browse VIP Tickets (Guest)");
        Button exitBtn = new Button("Exit");

        styleButton(createBtn); styleButton(loginBtn); styleButton(browseBtn); styleButton(exitBtn);

        createBtn.setOnAction(e -> showCreateAccount());
        loginBtn.setOnAction(e -> showLogin());
        browseBtn.setOnAction(e -> showBrowse());
        exitBtn.setOnAction(e -> primaryStage.close());

        layout.getChildren().addAll(title, createBtn, loginBtn, browseBtn, exitBtn);
        primaryStage.setScene(new Scene(layout, 800, 600));
    }

    private void showCreateAccount() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10); grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("Create Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        TextField idField = new TextField();
        PasswordField passField = new PasswordField();
        TextField nameField = new TextField();
        TextField addressField = new TextField();
        TextField cardField = new TextField();
        ComboBox<String> secQCombo = new ComboBox<>();
        secQCombo.getItems().addAll("Mother's maiden name?", "First pet?", "City born in?", "Favorite color?", "First car?");
        secQCombo.setValue("Mother's maiden name?");
        TextField secAnsField = new TextField();

        Button createBtn = new Button("Create Account");
        Button backBtn = new Button("Back");

        grid.add(title, 0, 0, 2, 1);
        grid.add(new Label("Customer ID:"), 0, 1); grid.add(idField, 1, 1);
        grid.add(new Label("Password:"), 0, 2); grid.add(passField, 1, 2);
        grid.add(new Label("Name:"), 0, 3); grid.add(nameField, 1, 3);
        grid.add(new Label("Address:"), 0, 4); grid.add(addressField, 1, 4);
        grid.add(new Label("Card Number:"), 0, 5); grid.add(cardField, 1, 5);
        grid.add(new Label("Security Q:"), 0, 6); grid.add(secQCombo, 1, 6);
        grid.add(new Label("Answer:"), 0, 7); grid.add(secAnsField, 1, 7);

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(createBtn, backBtn);
        grid.add(buttons, 0, 8, 2, 1);

        createBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String pass = passField.getText();
            if (customers.containsKey(id)) { showAlert("Error", "ID exists!"); return; }
            if (!validatePassword(pass)) { showAlert("Error", "Invalid password!"); return; }
            try {
                int card = Integer.parseInt(cardField.getText());
                customers.put(id, new Customer(id, pass, nameField.getText(), addressField.getText(),
                        card, secQCombo.getValue(), secAnsField.getText(), false));
                customerOrders.put(id, new ArrayList<>());
                showAlert("Success", "Account created!");
                clearFields(idField, passField, nameField, addressField, cardField, secAnsField);
                showMainMenu();
            } catch (Exception ex) { showAlert("Error", "Invalid input!"); }
        });

        backBtn.setOnAction(e -> showMainMenu());
        primaryStage.setScene(new Scene(grid, 800, 600));
    }

    private void showLogin() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(100));
        grid.setHgap(10); grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("Log On");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        TextField idField = new TextField();
        PasswordField passField = new PasswordField();
        Button loginBtn = new Button("Log On");
        Button backBtn = new Button("Back");

        grid.add(title, 0, 0, 2, 1);
        grid.add(new Label("Customer ID:"), 0, 1); grid.add(idField, 1, 1);
        grid.add(new Label("Password:"), 0, 2); grid.add(passField, 1, 2);

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(loginBtn, backBtn);
        grid.add(buttons, 0, 3, 2, 1);

        loginBtn.setOnAction(e -> {
            Customer c = customers.get(idField.getText().trim());
            if (c == null || !c.AcceptPass(passField.getText())) { showAlert("Error", "Invalid credentials!"); return; }

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Security Question");
            dialog.setHeaderText(c.getSecQ());
            dialog.setContentText("Your answer:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && c.AcceptAws(result.get().trim())) {
                c.login();
                currentCustomer = c;
                showLoggedInMenu();
            } else { showAlert("Error", "Incorrect answer!"); }
        });

        backBtn.setOnAction(e -> showMainMenu());
        primaryStage.setScene(new Scene(grid, 800, 600));
    }

    private void showLoggedInMenu() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(50));
        layout.setAlignment(Pos.CENTER);

        welcomeLabel = new Label("Welcome, " + currentCustomer.getName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Button browseBtn = new Button("Browse VIP Tickets");
        Button cartBtn = new Button("View Shopping Cart");
        Button checkoutBtn = new Button("Checkout");
        Button ordersBtn = new Button("View My Orders");
        Button logoutBtn = new Button("Log Out");
        Button exitBtn = new Button("Exit");

        styleButton(browseBtn); styleButton(cartBtn); styleButton(checkoutBtn);
        styleButton(ordersBtn); styleButton(logoutBtn); styleButton(exitBtn);

        browseBtn.setOnAction(e -> showBrowse());
        cartBtn.setOnAction(e -> showCart());
        checkoutBtn.setOnAction(e -> {
            if (currentCart.isEmpty()) showAlert("Error", "Cart empty!");
            else showCheckout();
        });
        ordersBtn.setOnAction(e -> showOrders());
        logoutBtn.setOnAction(e -> {
            currentCustomer.logout();
            currentCustomer = null;
            currentCart.clear();
            showAlert("Logged Out", "Goodbye!");
            showMainMenu();
        });
        exitBtn.setOnAction(e -> primaryStage.close());

        layout.getChildren().addAll(welcomeLabel, browseBtn, cartBtn, checkoutBtn, ordersBtn, logoutBtn, exitBtn);
        primaryStage.setScene(new Scene(layout, 800, 600));
    }

    private void showBrowse() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label title = new Label("VIP Concert Tickets");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        productTable = new TableView<>();
        TableColumn<ProductDisplay, String> idCol = new TableColumn<>("ID");
        TableColumn<ProductDisplay, String> artistCol = new TableColumn<>("Artist");
        TableColumn<ProductDisplay, String> packageCol = new TableColumn<>("Package");
        TableColumn<ProductDisplay, String> priceCol = new TableColumn<>("Price");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        packageCol.setCellValueFactory(new PropertyValueFactory<>("packageDesc"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.getColumns().addAll(idCol, artistCol, packageCol, priceCol);
        updateProductTable();

        browseBtnPanel = new HBox(10);
        browseBtnPanel.setAlignment(Pos.CENTER);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            if (currentCustomer != null && currentCustomer.isLoggedIn()) showLoggedInMenu();
            else showMainMenu();
        });

        // Add "Add to Cart" button ONLY if logged in
        if (currentCustomer != null && currentCustomer.isLoggedIn()) {
            Button addBtn = new Button("Add to Cart");
            addBtn.setOnAction(e -> addToCart());
            browseBtnPanel.getChildren().addAll(addBtn, backBtn);
        } else {
            browseBtnPanel.getChildren().add(backBtn);
        }

        layout.getChildren().addAll(title, productTable, browseBtnPanel);
        primaryStage.setScene(new Scene(layout, 800, 600));
    }

    private void addToCart() {
        ProductDisplay selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a ticket first!");
            return;
        }

        Product prod = findProduct(selected.getId());
        if (prod != null) {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Add to Cart");
            dialog.setHeaderText(prod.getName());
            dialog.setContentText("Number of tickets:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    int qty = Integer.parseInt(result.get());
                    if (qty > 0) {
                        currentCart.addItem(prod, qty);
                        showAlert("Success", "Added " + qty + " tickets to cart!");
                    }
                } catch (Exception ex) { showAlert("Error", "Invalid quantity!"); }
            }
        }
    }

    private void showCart() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label title = new Label("Your Ticket Cart");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        cartArea = new TextArea();
        cartArea.setEditable(false);
        cartArea.setFont(Font.font("Courier New", 12));
        updateCart();

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showLoggedInMenu());

        layout.getChildren().addAll(title, cartArea, backBtn);
        primaryStage.setScene(new Scene(layout, 800, 600));
    }

    private void showCheckout() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(100));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label("Checkout");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        ToggleGroup group = new ToggleGroup();
        RadioButton mailBtn = new RadioButton("Physical Mail ($3.00)");
        RadioButton emailBtn = new RadioButton("E-Tickets (Free)");
        mailBtn.setToggleGroup(group); emailBtn.setToggleGroup(group);
        mailBtn.setSelected(true);

        Button placeBtn = new Button("Place Order");
        Button cancelBtn = new Button("Cancel");

        placeBtn.setOnAction(e -> {
            double fee = mailBtn.isSelected() ? 3.0 : 0.0;
            double total = currentCart.getTotal() + fee;
            String orderId = "ORD" + (orderCounter++);
            String authNum = String.format("%04d", new Random().nextInt(10000));

            List<OrderItem> items = new ArrayList<>();
            for (CartItem ci : currentCart.getItems()) {
                items.add(new OrderItem(ci.getProduct().getName(), ci.getQuantity(), ci.getProduct().getPrice()));
            }

            Order order = new Order(orderId, currentCustomer.getId(), new Date().toString(),
                    items, fee, currentCart.getSubtotal(), currentCart.getTax(), total, authNum);
            customerOrders.get(currentCustomer.getId()).add(order);

            showAlert("Order Confirmed!", "Order ID: " + orderId + "\nTotal: $" + String.format("%.2f", total) +
                    "\nDelivery: " + (mailBtn.isSelected() ? "Physical Mail" : "E-Tickets") + "\n\nSee you at the show! 🎤");

            currentCart.clear();
            showLoggedInMenu();
        });

        cancelBtn.setOnAction(e -> showLoggedInMenu());

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(placeBtn, cancelBtn);

        layout.getChildren().addAll(title, new Label("Select Delivery:"), mailBtn, emailBtn, buttons);
        primaryStage.setScene(new Scene(layout, 800, 600));
    }

    private void showOrders() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label title = new Label("My Ticket Orders");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        orderArea = new TextArea();
        orderArea.setEditable(false);
        orderArea.setFont(Font.font("Courier New", 12));
        updateOrders();

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showLoggedInMenu());

        layout.getChildren().addAll(title, orderArea, backBtn);
        primaryStage.setScene(new Scene(layout, 800, 600));
    }

    private void updateProductTable() {
        ObservableList<ProductDisplay> data = FXCollections.observableArrayList();
        for (Product p : productCatalog) {
            data.add(new ProductDisplay(p.getProductID(), p.getName(), p.getDescription(), String.format("$%.2f", p.getSalesPrice())));
        }
        productTable.setItems(data);
    }

    private void updateCart() {
        StringBuilder sb = new StringBuilder("YOUR CART:\n\n");
        if (currentCart.isEmpty()) {
            sb.append("Your cart is empty.\n");
        } else {
            for (CartItem item : currentCart.getItems()) {
                sb.append(String.format("%-25s x%d  $%.2f\n", item.getProduct().getName(), item.getQuantity(), item.getSubtotal()));
            }
            sb.append(String.format("\nSubtotal: $%.2f\n", currentCart.getSubtotal()));
            sb.append(String.format("Service Fee: $%.2f\n", currentCart.getTax()));
            sb.append(String.format("Total: $%.2f\n", currentCart.getTotal()));
        }
        cartArea.setText(sb.toString());
    }

    private void updateOrders() {
        StringBuilder sb = new StringBuilder();
        List<Order> orders = customerOrders.get(currentCustomer.getId());
        if (orders == null || orders.isEmpty()) {
            sb.append("No orders yet.");
        } else {
            for (Order o : orders) {
                sb.append("═══════════════════════════════════════\n");
                sb.append("Order: ").append(o.getOrderID()).append("\n");
                sb.append("Date: ").append(o.getOrderDate()).append("\n");
                for (OrderItem item : o.getItems()) {
                    sb.append(String.format("  %s x%d = $%.2f\n", item.getItemName(), item.getQuantity(), item.getSubtotal()));
                }
                sb.append(String.format("Total: $%.2f\n\n", o.getTotal()));
            }
        }
        orderArea.setText(sb.toString());
    }

    private Product findProduct(String id) {
        for (Product p : productCatalog) if (p.getProductID().equals(id)) return p;
        return null;
    }

    private boolean validatePassword(String p) {
        if (p.length() < 6) return false;
        boolean digit = false, upper = false, special = false;
        for (char c : p.toCharArray()) {
            if (Character.isDigit(c)) digit = true;
            if (Character.isUpperCase(c)) upper = true;
            if ("@#$%&*".indexOf(c) >= 0) special = true;
        }
        return digit && upper && special;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void styleButton(Button btn) {
        btn.setPrefWidth(300);
        btn.setPrefHeight(40);
        btn.setFont(Font.font("Arial", 14));
    }

    private void clearFields(Control... fields) {
        for (Control field : fields) {
            if (field instanceof TextField) ((TextField) field).clear();
            else if (field instanceof PasswordField) ((PasswordField) field).clear();
        }
    }

    public static class ProductDisplay {
        private String id, artist, packageDesc, price;
        public ProductDisplay(String id, String artist, String packageDesc, String price) {
            this.id = id; this.artist = artist; this.packageDesc = packageDesc; this.price = price;
        }
        public String getId() { return id; }
        public String getArtist() { return artist; }
        public String getPackageDesc() { return packageDesc; }
        public String getPrice() { return price; }
    }

    public static void main(String[] args) { launch(args); }
}