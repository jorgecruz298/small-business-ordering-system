package Transactions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreatePurchaseOrder {
    private final Map<String, Integer> inventory;
    private final Map<String, Double> itemPrices;
    private final ApproveOrder approveOrder;

    public CreatePurchaseOrder(Map<String, Integer> inventory, Map<String, Double> itemPrices, ApproveOrder approveOrder) {
        this.inventory = inventory;
        this.itemPrices = itemPrices;
        this.approveOrder = approveOrder;

        // Load saved orders from the file
        loadOrdersFromFile();
    }

    public void display(JFrame parentFrame, Runnable backToMenu) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Create Purchase Order", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));

        // Delivery Date Section
        JPanel deliveryDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel deliveryDateLabel = new JLabel("Delivery Date (YYYY-MM-DD):");
        JTextField deliveryDateField = new JTextField(10);
        deliveryDatePanel.add(deliveryDateLabel);
        deliveryDatePanel.add(deliveryDateField);

        // Order Summary Table
        String[] columnNames = {"Item", "Quantity", "Unit Price", "Total"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable orderTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(orderTable);

        // Buttons
        JButton addItemButton = new JButton("Add Item");
        JButton submitButton = new JButton("Submit Order");
        JButton backButton = new JButton("Back");
        JLabel timerLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 18));

        // Add Item Logic
        addItemButton.addActionListener(e -> {
            String selectedItem = (String) JOptionPane.showInputDialog(
                    parentFrame,
                    "Select an item to add:",
                    "Item Selection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    inventory.keySet().toArray(),
                    null);

            if (selectedItem != null) {
                String quantityInput = JOptionPane.showInputDialog(parentFrame, "Enter quantity for " + selectedItem + ":");
                try {
                    int quantity = Integer.parseInt(quantityInput);

                    // Check if the requested quantity is available in inventory
                    int availableQuantity = inventory.getOrDefault(selectedItem, 0);
                    if (quantity > availableQuantity) {
                        JOptionPane.showMessageDialog(parentFrame,
                                "Not enough stock available! Only " + availableQuantity + " units of " + selectedItem + " are available.",
                                "Insufficient Stock", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double price = itemPrices.getOrDefault(selectedItem, 0.0);
                    double totalCost = price * quantity;

                    // Deduct the quantity from inventory
                    inventory.put(selectedItem, availableQuantity - quantity);

                    // Add item to the table
                    tableModel.addRow(new Object[]{selectedItem, quantity, String.format("$%.2f", price), String.format("$%.2f", totalCost)});

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Invalid quantity. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Submit Button Logic
        submitButton.addActionListener(e -> {
            String deliveryDateText = deliveryDateField.getText();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                Date deliveryDate = sdf.parse(deliveryDateText);

                if (deliveryDate.before(new Date())) {
                    JOptionPane.showMessageDialog(parentFrame, "Delivery date must be in the future.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Generate a random order number
                int orderNumber = new Random().nextInt(900000) + 100000; // Generate 6-digit number
                StringBuilder summary = new StringBuilder("Order Summary:\n");
                summary.append("Order Number: ").append(orderNumber).append("\n\n");

                double totalOrderCost = 0;
                StringBuilder itemsSummary = new StringBuilder();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String itemName = (String) tableModel.getValueAt(i, 0);
                    int quantity = (int) tableModel.getValueAt(i, 1);
                    double totalCost = Double.parseDouble(((String) tableModel.getValueAt(i, 3)).replace("$", ""));
                    totalOrderCost += totalCost;

                    itemsSummary.append(itemName).append(" (x").append(quantity).append("), ");
                }

                if (itemsSummary.length() > 0) {
                    itemsSummary.setLength(itemsSummary.length() - 2); // Remove trailing comma and space
                }

                Map<String, Object> order = new HashMap<>();
                order.put("orderNumber", String.valueOf(orderNumber));
                order.put("items", itemsSummary.toString());
                order.put("totalCost", totalOrderCost);
                order.put("deliveryDate", deliveryDateText);

                approveOrder.addOrder(order);

                // Save the order to the file
                saveOrderToFile(order);

                // Display order summary
                summary.append("Items: ").append(itemsSummary).append("\n");
                summary.append("Total Cost: $").append(String.format("%.2f", totalOrderCost)).append("\n");
                summary.append("Delivery Date: ").append(deliveryDateText);

                JOptionPane.showMessageDialog(parentFrame, summary.toString(), "Order Created", JOptionPane.INFORMATION_MESSAGE);

                // Clear table and reset UI
                tableModel.setRowCount(0);
                deliveryDateField.setText("");

                // Start countdown timer
                startCountdown(5, backToMenu, timerLabel);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid delivery date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back Button Logic
        backButton.addActionListener(e -> SwingUtilities.invokeLater(backToMenu));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addItemButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(deliveryDatePanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(timerLabel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        parentFrame.setContentPane(mainPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void saveOrderToFile(Map<String, Object> order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            writer.write("Order Number: " + order.get("orderNumber"));
            writer.newLine();
            writer.write("Items: " + order.get("items"));
            writer.newLine();
            writer.write("Total Cost: $" + order.get("totalCost"));
            writer.newLine();
            writer.write("Delivery Date: " + order.get("deliveryDate"));
            writer.newLine();
            writer.write("---");
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving order to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadOrdersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            while (reader.readLine() != null) {
                // Placeholder for future processing
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved orders found. Starting fresh.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading orders from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startCountdown(int seconds, Runnable backToMenu, JLabel timerLabel) {
        new Thread(() -> {
            for (int i = seconds; i >= 0; i--) {
                final int timeLeft = i;
                SwingUtilities.invokeLater(() -> {
                    if (timeLeft > 0) {
                        timerLabel.setText("Time Remaining: " + timeLeft + " seconds");
                    } else {
                        timerLabel.setText("Time's up! Returning to the main menu...");
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            SwingUtilities.invokeLater(backToMenu);
        }).start();
    }
}
