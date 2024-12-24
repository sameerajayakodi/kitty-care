# Kitty Care Petshop POS System

Welcome to the Kitty Care Petshop POS System! This application is designed to efficiently manage pet products, handle customer orders, and track inventory using a robust Model-View-Controller (MVC) architecture and Singleton design pattern.

---

## Features and Functionality

### Product Management
- **Add Products:** Add new products to the inventory with details such as name, category, quantity, price, and availability.
- **Update Products:** Modify product information, including price, quantity, and availability.
- **Track Stock:** Real-time stock level updates whenever a product is sold.

### Order Management
- **Create Orders:** Log customer purchases and calculate total order amounts.
- **Order Details:** Maintain records of individual products in each order, including quantities and prices.
- **View Orders:** View comprehensive order details, such as product names, quantities, and total amounts.

### Inventory Management
- **Automatic Updates:** Update stock levels automatically after each sale, ensuring inventory is always up-to-date.

---

## Architecture

The system follows the **MVC (Model-View-Controller)** pattern:
- **Model:** Handles system data and business logic, interacting with the database for operations like adding products, updating inventory, and creating orders.
- **View:** Provides user-friendly interfaces to display data, such as product listings, order forms, and summaries.
- **Controller:** Processes user input, interacts with the model, and updates the view as needed.

### Singleton Design Pattern
The Singleton design pattern is used for database connection management to:
- Ensure a single, efficient connection to the MySQL database.
- Reduce overhead and optimize resource usage.

---

## Database Structure

### Product Table (`product`)
| Field         | Description                                 |
|---------------|---------------------------------------------|
| `id`          | Unique identifier for each product         |
| `name`        | Product name (e.g., "Cat Food")            |
| `category`    | Product category (e.g., "Food")            |
| `qty`         | Quantity in stock                          |
| `price`       | Price per unit                             |
| `create_date` | Timestamp of product addition              |
| `update_date` | Timestamp of last update                   |
| `availability`| Availability status (1 for available, 0 for unavailable) |

### Orders Table (`orders`)
| Field         | Description                                 |
|---------------|---------------------------------------------|
| `id`          | Unique identifier for each order           |
| `order_date`  | Timestamp of order placement               |
| `amount`      | Total order amount                         |

### Order Details Table (`order_detail`)
| Field         | Description                                 |
|---------------|---------------------------------------------|
| `odid`        | Unique identifier for each order detail    |
| `oid`         | Order ID referencing the `orders` table    |
| `iid`         | Product ID referencing the `product` table |
| `qty`         | Quantity purchased                         |
| `price`       | Price at the time of purchase              |

---

## Technologies Used

### Backend
- Java (MVC pattern) for business logic and database interactions.

### Database
- MySQL to store and manage data.

### Frontend
- Fxml.


---

## Additional Features

### Reporting
- Generate detailed reports for sales, stock levels, and order history.

### Authentication
- Secure admin access for product and order management.

### User-Friendly Interface
- Ensure ease of use for managing products and orders.

---

## System Images
### Login Page
![Screenshot 2024-12-24 155918](https://github.com/user-attachments/assets/0ef182d8-7d04-45b5-a7d1-401e0dbb6749)

### Product Management Page
![Screenshot 2024-12-24 180659](https://github.com/user-attachments/assets/4ff471a2-c159-4b8e-b896-c842e6bc8ed7)

![Screenshot 2024-12-24 180731](https://github.com/user-attachments/assets/acebe651-3ca0-4cc3-ac5e-c702c6a501d1)

### Order Summary Page
![Screenshot 2024-12-24 180520](https://github.com/user-attachments/assets/c638e9f0-bd11-4015-8cb0-524ecfef8858)

### Inventory Dashboard
![Screenshot 2024-12-24 180617](https://github.com/user-attachments/assets/b671ed55-c3a4-4418-a929-8c02a28ab2b3)

---

## Getting Started

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/username/kittycare-pos-system.git
   ```

2. **Set Up Database:**
   - Import the provided MySQL scripts to create the database and tables.

3. **Configure Backend:**
   - Update database connection settings in the backend configuration file.

4. **Run the Application:**
   - Start the backend server.
   - Open the frontend in your browser.

---

## Contributions
Contributions are welcome! Please open an issue or submit a pull request.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

