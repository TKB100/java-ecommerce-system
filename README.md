# Java E-Commerce System

A fully object-oriented Java e-commerce application featuring customer authentication, product browsing, shopping cart management, order processing, and payment integration with a graphical user interface (GUI).

---

## Project Structure

| File | Description |
|------|-------------|
| `Main.java` | Entry point — launches the application |
| `MainGUI.java` | Graphical user interface — handles all user interaction and display |
| `Customer.java` | Customer class — manages authentication, profile, and session state |
| `Product.java` | Product class — stores product details, pricing, and descriptions |
| `ShoppingCart.java` | Shopping cart — manages items, calculates subtotals, tax, and totals |
| `CartItem.java` | Cart item — links a product to a quantity with subtotal calculation |
| `Order.java` | Order class — stores completed order details including items and totals |
| `OrderItem.java` | Order item — represents a purchased product within an order |
| `BankService.java` | Payment service — handles card validation and transaction processing |

---

## Features

- **Customer Authentication** — login, logout, password validation, and security question recovery
- **Product Catalog** — browse products with regular and sale pricing
- **Shopping Cart** — add/remove items, automatic tax calculation (8.25%), real-time totals
- **Order Processing** — complete checkout flow with order ID generation and order history
- **Payment Integration** — card validation and transaction authorization via BankService
- **GUI Interface** — full graphical interface built with Java Swing

---

## OOP Design

This project demonstrates core object-oriented programming principles:

- **Encapsulation** — all class fields are private with public getters/setters
- **Abstraction** — BankService abstracts payment logic from the rest of the application
- **Separation of Concerns** — each class has a single responsibility (Customer, Product, Cart, Order)
- **Composition** — ShoppingCart contains CartItems, Order contains OrderItems

---

## How to Run

### Prerequisites
- Java JDK 11 or higher

### Compile

```bash
javac *.java
```

### Run

```bash
java Main
```

---

## Skills Demonstrated

- **Java OOP** — classes, encapsulation, composition, and method design
- **Data Structures** — ArrayList for cart and order item management
- **GUI Development** — Java Swing for graphical user interface
- **Business Logic** — tax calculation, order processing, payment validation
- **Software Design** — clean separation of concerns across multiple classes

---

## Author

**Triston Barrientos**
CS Senior — Texas Tech University (May 2026)
[LinkedIn](https://linkedin.com/in/triston00barrientos) | [GitHub](https://github.com/TKB100)
