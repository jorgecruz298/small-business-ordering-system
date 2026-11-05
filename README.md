# ProcureWorx – Small Business Transaction Manager

## Overview

ProcureWorx is a Java desktop application developed as an academic project to simulate purchasing and inventory management operations for a small business.

The application provides separate interfaces for Purchasing Agents and Vendor Representatives, allowing users to manage inventory, create purchase orders, and review pending orders through a graphical user interface.

## Features

- **Role-Based Access:** Separate login interfaces for Purchasing Agents and Vendor Representatives.
- **Inventory Management:** Tracks 20 predefined products, including quantities and unit prices.
- **Purchase Order Creation:** Allows users to select products, enter quantities, and generate purchase orders.
- **Order Calculations:** Automatically calculates individual item costs and total order amounts.
- **Delivery Scheduling:** Validates delivery dates before order submission.
- **Order Number Generation:** Generates unique-format six-digit order numbers.
- **File Storage:** Saves purchase order summaries to a local text file (`orders.txt`).
- **Order Processing:** Allows Vendor Representatives to approve or reject pending purchase orders.

## Technologies Used

- Java 11
- Java Swing
- Object-Oriented Programming (OOP)
- Java Collections (`HashMap`, `Map`, `Queue`)
- File I/O
- Eclipse IDE

## Project Structure

```text
Ordering-Purchasing-System-Portfolio/
│
├── src/
│   ├── Primary.java
│   └── Transactions/
│       ├── Inventory.java
│       ├── CreatePurchaseOrder.java
│       └── ApproveOrder.java
│
├── bin/
├── doc/
└── orders.txt
```

## Running the Application

### Requirements
- Java JDK 11 or later
- Eclipse IDE (recommended)

### Instructions

1. Clone or download this repository.
2. Open Eclipse IDE.
3. Select **File → Import → General → Existing Projects into Workspace**.
4. Browse to the `Ordering-Purchasing-System-Portfolio` folder.
5. Select the project and click **Finish**.
6. Expand the `src` folder in the Package Explorer.
7. Open `Primary.java`.
8. Right-click the file and select **Run As → Java Application**.

The application will open in a Java Swing window, beginning with the role selection screen.

### Demo Login Credentials

| Role | Password |
|---|---|
| Purchasing Agent | `agent123` |
| Vendor Representative | `vendor123` |

*The application currently uses demonstration credentials.*

## Application Screenshots

The screenshots below demonstrate the main features and workflows of ProcureWorx, from user login and inventory management to purchase order creation and approval.

### 1. Login & Navigation

**Start Menu** – Initial application screen.

![Start Menu](Start%20Menu.png)

**Agent Login** – Purchasing agent login screen.

![Agent Login](Agent%20Login.png)

**Agent Menu** – Main menu for purchasing agents.

![Agent Menu](Agent%20Menu.png)

**Vendor Window** – Vendor representative interface.

![Vendor Window](Vendor%20Window.png)

### 2. Inventory Management

**Update Inventory Window** – Interface for managing product inventory.

![Update Inventory Window](Update%20Inv%20Levels%20Window.png)

**Item Update** – Updating product quantities.

![Item Update](Item%20Update.png)

**Updated Inventory Levels** – Displays inventory after updates.

![Updated Inventory Levels](Updated%20Inv%20Levels.png)

### 3. Purchase Order Creation

**Order Window** – Purchase order creation interface.

![Order Window](Order%20Window.png)

**Item Selection** – Selecting products for an order.

![Item Selection](Item%20Selection.png)

**Order Submission** – Purchase order submission confirmation.

![Order Submission](Order%20Submission.png)

**Delivery Date Validation** – Example of delivery date error handling.

![Delivery Date Error](Delivery%20Date%20Error.png)

### 4. Order Approval

**Approve Purchase Window** – Reviewing pending purchase orders.

![Approve Purchase Window](Approve%20Purchase%20Window.png)

**Approved Order** – Example of a successfully approved order.

![Approved Order](Approved%20Order.png)

**Rejected Order** – Example of an order rejection.

![Rejected Order](Order%20Rejected.png)


## Project Context

Developed as part of my Information Technology studies at Northern Virginia Community College (NOVA).
