import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Customer> customers = new HashMap<>();
    private static List<Product> productCatalog = new ArrayList<>();
    private static Map<String, List<Order>> customerOrders = new HashMap<>();
    private static BankService bankService = new BankService();
    private static int orderCounter = 1000;

    private static Customer currentCustomer = null;
    private static ShoppingCart currentCart = new ShoppingCart();

    public static void main(String[] args) {
        initializeProductCatalog();

        boolean running = true;
        while (running) {
            if (currentCustomer == null || !currentCustomer.isLoggedIn()) {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        logOn();
                        break;
                    case 3:
                        browseProducts();
                        break;
                    case 4:
                        running = false;
                        System.out.println("\nThank you for using VIP Concert Ticket System! 🎤");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                displayLoggedInMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        browseProducts();
                        break;
                    case 2:
                        viewCart();
                        break;
                    case 3:
                        makeOrder();
                        break;
                    case 4:
                        viewOrders();
                        break;
                    case 5:
                        logOut();
                        break;
                    case 6:
                        running = false;
                        System.out.println("\nThank you for using VIP Concert Ticket System! 🎤");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void initializeProductCatalog() {
        productCatalog.add(new Product("VIP001", "Travis Scott VIP", "Circus Maximus Tour - Front Row + Meet & Greet", 850.00, 749.99));
        productCatalog.add(new Product("VIP002", "Playboi Carti VIP", "Antagonist Tour - VIP Pit + Early Entry", 600.00, 499.99));
        productCatalog.add(new Product("VIP003", "Don Toliver VIP", "Hardstone Psycho Tour - VIP Section + Merch Pack", 450.00, 379.99));
        productCatalog.add(new Product("VIP004", "Future VIP", "Mixtape Pluto Tour - Backstage Pass + Photo Op", 700.00, 599.99));
        productCatalog.add(new Product("VIP005", "Metro Boomin VIP", "Heroes & Villains Tour - VIP Booth + Signed Poster", 550.00, 449.99));
        productCatalog.add(new Product("VIP006", "21 Savage VIP", "American Dream Tour - Floor Seats + VIP Lounge", 500.00, 419.99));
        productCatalog.add(new Product("VIP007", "Ken Carson VIP", "A Great Chaos Tour - VIP Pit + Meet & Greet", 350.00, 279.99));
        productCatalog.add(new Product("VIP008", "Yeat VIP", "2 Alive Tour - Front Section + Exclusive Merch", 400.00, 329.99));
    }

    private static void displayMainMenu() {
        System.out.println("      MAIN MENU      ");
        System.out.println("1. Create Account");
        System.out.println("2. Log On");
        System.out.println("3. Browse VIP Tickets (Guest)");
        System.out.println("4. Exit");
    }

    private static void displayLoggedInMenu() {
        System.out.println("    WELCOME, " + currentCustomer.getName().toUpperCase());
        System.out.println("1. Browse VIP Tickets");
        System.out.println("2. View Shopping Cart");
        System.out.println("3. Checkout");
        System.out.println("4. View My Orders");
        System.out.println("5. Log Out");
        System.out.println("6. Exit");
    }

    // USE CASE: Create Account
    private static void createAccount() {
        System.out.println("        CREATE ACCOUNT                 ");

        // Step 1-2: Enter and check customer ID
        String id;
        while (true) {
            System.out.print("Enter Customer ID: ");
            id = scanner.nextLine().trim();

            if (customers.containsKey(id)) {
                System.out.println("Error: ID already exists. Please choose a different ID.");
            } else {
                break;
            }
        }

        // Step 3-4: Enter and validate password
        String password;
        while (true) {
            System.out.print("Enter Password (min 6 chars, must include digit, special char @#$%&*, uppercase): ");
            password = scanner.nextLine();

            if (validatePassword(password)) {
                break;
            } else {
                System.out.println("Invalid password. Please try again.");
            }
        }

        // Step 5-7: Enter name, address, and credit card
        String name, address;
        int cardNumber;
        while (true) {
            System.out.print("Enter your name: ");
            name = scanner.nextLine().trim();
            System.out.print("Enter your address: ");
            address = scanner.nextLine().trim();
            cardNumber = getIntInput("Enter credit card number (digits only): ");

            if (name.isEmpty() || address.isEmpty() || cardNumber <= 0) {
                System.out.println("Name, address, or credit card cannot be empty. Please try again.");
            } else {
                System.out.println("Account information validated.");
                break;
            }
        }

        // Step 8-10: Select security question and answer
        List<String> questions = getSecurityQuestions();
        System.out.println("\nSelect a security question:");
        for (int i = 0; i < questions.size(); i++) {
            System.out.println((i + 1) + ". " + questions.get(i));
        }

        int qChoice = getIntInput("Enter question number (1-" + questions.size() + "): ");
        while (qChoice < 1 || qChoice > questions.size()) {
            System.out.println("Invalid choice.");
            qChoice = getIntInput("Enter question number (1-" + questions.size() + "): ");
        }

        String securityQ = questions.get(qChoice - 1);
        System.out.print("Enter your answer: ");
        String securityAns = scanner.nextLine().trim();

        // Create the account using YOUR Customer class
        Customer newCustomer = new Customer(id, password, name, address, cardNumber,
                securityQ, securityAns, false);
        customers.put(id, newCustomer);
        customerOrders.put(id, new ArrayList<>());

        System.out.println("\n Account created successfully!");
        System.out.println("You can now log in with ID: " + id);
    }

    // USE CASE: Log On
    private static void logOn() {
        System.out.println("            LOG ON                      ");

        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter Customer ID: ");
            String id = scanner.nextLine().trim();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            Customer customer = customers.get(id);

            if (customer == null) {
                System.out.println("No account found with this ID.");
                return;
            }

            if (!customer.AcceptPass(password)) {
                attempts++;
                System.out.println("Incorrect password. Attempts remaining: " + (MAX_ATTEMPTS - attempts));
                if (attempts >= MAX_ATTEMPTS) {
                    System.out.println("Maximum attempts reached. Login failed.");
                    return;
                }
                continue;
            }

            // Display security question
            System.out.println("\nSecurity Question: " + customer.getSecQ());
            System.out.print("Your Answer: ");
            String answer = scanner.nextLine().trim();

            if (customer.AcceptAws(answer)) {
                customer.login();
                currentCustomer = customer;
                System.out.println("\n✓ Welcome, " + customer.getName() + "!");
                System.out.println("You have successfully logged in.");
                return;
            } else {
                System.out.println("Incorrect security answer. Login failed.");
                return;
            }
        }
    }

    // USE CASE: Log Out
    private static void logOut() {
        if (currentCustomer != null) {
            System.out.println("            LOG OUT                     ");
            System.out.println("Goodbye, " + currentCustomer.getName() + "!");
            currentCustomer.logout();
            currentCustomer = null;
            currentCart.clear();
            System.out.println("✓ You have been logged out successfully.");
        }
    }

    // USE CASE: Select Items (Browse Products)
    private static void browseProducts() {
        System.out.println("                        VIP CONCERT TICKETS:                                 ");

        System.out.println(String.format("%-8s %-22s %-38s %-12s %-12s",
                "ID", "Artist", "Package Details", "Reg. Price", "Sale Price"));
        System.out.println("─".repeat(95));

        for (Product p : productCatalog) {
            System.out.println(String.format("%-8s %-22s %-38s $%-11.2f $%-11.2f",
                    p.getProductID(),
                    p.getName(),
                    p.getDescription(),
                    p.getRegularPrice(),
                    p.getSalesPrice()));
        }

        if (currentCustomer != null && currentCustomer.isLoggedIn()) {
            System.out.println("\nWould you like to add tickets to your cart? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            while (response.equals("y")) {
                System.out.print("Enter Ticket ID (e.g., VIP001): ");
                String productId = scanner.nextLine().trim().toUpperCase();

                Product product = findProduct(productId);
                if (product == null) {
                    System.out.println("Ticket not found.");
                } else {
                    int quantity = getIntInput("Enter number of tickets: ");
                    if (quantity > 0) {
                        currentCart.addItem(product, quantity);
                        System.out.println("Added " + quantity + " x " + product.getName() + " ticket(s) to cart.");
                    } else {
                        System.out.println("Invalid quantity.");
                    }
                }

                System.out.print("\nAdd more tickets? (y/n): ");
                response = scanner.nextLine().trim().toLowerCase();
            }
        } else {
            System.out.println("\n(Please log in to add items to cart)");
        }
    }

    // View Shopping Cart
    private static void viewCart() {
        System.out.println("         YOUR TICKET CART               ");

        if (currentCart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println(String.format("%-22s %-10s %-12s %-12s",
                "Artist", "Qty", "Price", "Subtotal"));
        System.out.println("─".repeat(60));

        for (CartItem item : currentCart.getItems()) {
            System.out.println(String.format("%-22s %-10d $%-11.2f $%-11.2f",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    item.getSubtotal()));
        }

        System.out.println("─".repeat(60));
        System.out.println(String.format("%-44s $%.2f", "Subtotal:", currentCart.getSubtotal()));
        System.out.println(String.format("%-44s $%.2f", "Service Fee (8.25%):", currentCart.getTax()));
        System.out.println(String.format("%-44s $%.2f", "Total:", currentCart.getTotal()));

        System.out.println("\nOptions:");
        System.out.println("1. Remove a ticket");
        System.out.println("2. Continue browsing");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();
        if (choice.equals("1")) {
            System.out.print("Enter Ticket ID to remove: ");
            String productId = scanner.nextLine().trim().toUpperCase();
            currentCart.removeItem(productId);
            System.out.println("Item removed from cart.");
        }
    }

    // USE CASE: Make Order
    private static void makeOrder() {
        System.out.println("          CHECKOUT                      ");

        if (currentCart.isEmpty()) {
            System.out.println("Your cart is empty. Please add tickets first.");
            return;
        }

        // Display current cart
        System.out.println("\nYour Order Summary:");
        System.out.println("─".repeat(60));
        for (CartItem item : currentCart.getItems()) {
            System.out.println(String.format("%-22s x%-3d @ $%-8.2f = $%.2f",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    item.getSubtotal()));
        }
        System.out.println("─".repeat(60));
        System.out.println(String.format("%-44s $%.2f", "Subtotal:", currentCart.getSubtotal()));
        System.out.println(String.format("%-44s $%.2f", "Service Fee:", currentCart.getTax()));

        // Select delivery method
        System.out.println("\nTicket Delivery Options:");
        System.out.println("1. Physical Tickets by Mail ($3.00 fee)");
        System.out.println("2. E-Tickets (Free - sent to email)");
        System.out.println("3. Cancel Order");

        int deliveryChoice = getIntInput("Select delivery method (1-3): ");

        if (deliveryChoice == 3) {
            System.out.println("Order cancelled.");
            return;
        }

        boolean isMailDelivery = (deliveryChoice == 1);
        double deliveryFee = isMailDelivery ? 3.00 : 0.00;
        double subtotal = currentCart.getSubtotal();
        double tax = currentCart.getTax();
        double total = subtotal + tax + deliveryFee;

        // Display total
        System.out.println("\n" + "─".repeat(60));
        System.out.println(String.format("%-44s $%.2f", "Delivery Fee:", deliveryFee));
        System.out.println(String.format("%-44s $%.2f", "TOTAL:", total));
        System.out.println("─".repeat(60));

        // Process payment
        int cardNumber = currentCustomer.getCardNumber();
        boolean paymentSuccess = false;

        while (!paymentSuccess) {
            if (bankService.validateCard(cardNumber)) {
                String authResult = bankService.chargeCard(cardNumber, total);
                System.out.println("\n✓ " + authResult);
                paymentSuccess = true;
            } else {
                System.out.println("Payment declined. Invalid card.");
                System.out.println("\nOptions:");
                System.out.println("1. Enter a new credit card number");
                System.out.println("2. Cancel order");

                int choice = getIntInput("Enter choice: ");
                if (choice == 1) {
                    cardNumber = getIntInput("Enter new credit card number: ");
                    currentCustomer.updateCard(cardNumber);
                } else {
                    System.out.println("Order cancelled.");
                    return;
                }
            }
        }

        // Create order using YOUR Order class
        String orderID = "ORD" + (orderCounter++);
        String authNum = String.format("%04d", new Random().nextInt(10000));
        String orderDate = new Date().toString();

        // Create order items from cart
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : currentCart.getItems()) {
            orderItems.add(new OrderItem(
                    cartItem.getProduct().getName(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice()
            ));
        }

        // Create and store order
        Order order = new Order(orderID, currentCustomer.getId(), orderDate, orderItems,
                deliveryFee, subtotal, tax, total, authNum);
        customerOrders.get(currentCustomer.getId()).add(order);

        System.out.println("      ORDER CONFIRMATION                ");
        System.out.println("Order ID: " + orderID);
        System.out.println("Total Amount: $" + String.format("%.2f", total));
        System.out.println("Delivery: " + (isMailDelivery ? "Physical Tickets by Mail" : "E-Tickets"));
        System.out.println("\n✓ Your tickets have been purchased!");
        System.out.println("🎤 See you at the show!");

        // Clear the cart
        currentCart.clear();
    }

    // USE CASE: View Orders
    private static void viewOrders() {
        System.out.println("       MY TICKET ORDERS                ");

        List<Order> orders = customerOrders.get(currentCustomer.getId());

        if (orders == null || orders.isEmpty()) {
            System.out.println("You haven't purchased any tickets yet.");
            return;
        }

        for (Order order : orders) {
            System.out.println("\n" + "═".repeat(70));
            System.out.println("Order ID: " + order.getOrderID());
            System.out.println("Order Date: " + order.getOrderDate());
            System.out.println("─".repeat(70));
            System.out.println(String.format("%-30s %-10s %-12s", "Artist", "Tickets", "Subtotal"));
            System.out.println("─".repeat(70));

            for (OrderItem item : order.getItems()) {
                System.out.println(String.format("%-30s %-10d $%-11.2f",
                        item.getItemName(),
                        item.getQuantity(),
                        item.getSubtotal()));
            }

            System.out.println("─".repeat(70));
            System.out.println(String.format("%-42s $%.2f", "Total:", order.getTotal()));
            System.out.println("Authorization Number: " + String.format("%04d", (int)order.getauthoNum()));
        }
    }

    // Helper methods
    private static boolean validatePassword(String password) {
        if (password.length() < 6) {
            return false;
        }

        boolean hasDigit = false;
        boolean hasSpecial = false;
        boolean hasUpperCase = false;
        String specialChars = "@#$%&*";

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            if (Character.isUpperCase(c)) hasUpperCase = true;
            if (specialChars.indexOf(c) != -1) hasSpecial = true;
        }

        return hasDigit && hasSpecial && hasUpperCase;
    }

    private static List<String> getSecurityQuestions() {
        List<String> questions = new ArrayList<>();
        questions.add("What is your mother's maiden name?");
        questions.add("What was the name of your first pet?");
        questions.add("What city were you born in?");
        questions.add("What is your favorite color?");
        questions.add("What was your first car?");
        return questions;
    }

    private static Product findProduct(String productId) {
        for (Product p : productCatalog) {
            if (p.getProductID().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}