package Transactions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class ApproveOrder {
    private final Queue<Map<String, Object>> pendingOrders;

    public ApproveOrder() {
        pendingOrders = new LinkedList<>();
    }

    public void addOrder(Map<String, Object> order) {
        pendingOrders.add(order);
    }

    public void display(JFrame parentFrame, Runnable backToMenu) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Approve Purchase Orders", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));

        // Order Summary Table
        String[] columnNames = {"Order Number", "Items", "Total Cost", "Delivery Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable ordersTable = new JTable(tableModel);

        // Populate table with pending orders
        for (Map<String, Object> order : pendingOrders) {
            String orderNumber = (String) order.get("orderNumber");
            String items = (String) order.get("items");
            String totalCost = String.format("$%.2f", (Double) order.get("totalCost"));
            String deliveryDate = (String) order.get("deliveryDate");

            tableModel.addRow(new Object[]{orderNumber, items, totalCost, deliveryDate});
        }

        JScrollPane tableScrollPane = new JScrollPane(ordersTable);

        // Buttons
        JButton approveButton = new JButton("Approve Selected Order");
        JButton rejectButton = new JButton("Reject Selected Order");
        JButton backButton = new JButton("Back");
        JLabel timerLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 14));

        approveButton.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Please select an order to approve.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String orderNumber = (String) tableModel.getValueAt(selectedRow, 0);
            pendingOrders.removeIf(order -> order.get("orderNumber").equals(orderNumber));
            tableModel.removeRow(selectedRow);

            JOptionPane.showMessageDialog(parentFrame, "Order " + orderNumber + " approved successfully.", "Order Approved", JOptionPane.INFORMATION_MESSAGE);

            // Start countdown timer
            startCountdown(5, backToMenu, timerLabel);
        });

        rejectButton.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Please select an order to reject.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String orderNumber = (String) tableModel.getValueAt(selectedRow, 0);
            pendingOrders.removeIf(order -> order.get("orderNumber").equals(orderNumber));
            tableModel.removeRow(selectedRow);

            JOptionPane.showMessageDialog(parentFrame, "Order " + orderNumber + " rejected successfully.", "Order Rejected", JOptionPane.INFORMATION_MESSAGE);

            // Start countdown timer
            startCountdown(5, backToMenu, timerLabel);
        });

        backButton.addActionListener(e -> backToMenu.run());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(timerLabel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        parentFrame.setContentPane(mainPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void startCountdown(int seconds, Runnable backToMenu, JLabel timerLabel) {
        new Thread(() -> {
            for (int i = seconds; i >= 0; i--) {
                final int timeLeft = i;
                SwingUtilities.invokeLater(() -> {
                    if (timeLeft > 0) {
                        timerLabel.setText("Returning to menu in: " + timeLeft + " seconds...");
                    } else {
                        timerLabel.setText("Returning to main menu...");
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
