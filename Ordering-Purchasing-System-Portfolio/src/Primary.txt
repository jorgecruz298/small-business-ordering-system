import javax.swing.*;
import java.awt.*;
import Transactions.Inventory;
import Transactions.CreatePurchaseOrder;
import Transactions.ApproveOrder;
import java.util.Map;

public class Primary extends JFrame {
    private static final long serialVersionUID = 1L;
	private JPanel currentPanel;
    private final Map<String, String> credentials;
    private final Inventory inventory;
    private final CreatePurchaseOrder createPurchaseOrder;
    private final ApproveOrder approveOrder;

    public Primary() {
        setTitle("ProcureWorx - Small Business Transaction Manager");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        credentials = Map.of(
            "Purchasing Agent", "agent123",
            "Vendor Representative", "vendor123"
        );

        inventory = new Inventory();
        approveOrder = new ApproveOrder();
        createPurchaseOrder = new CreatePurchaseOrder(inventory.getInventory(), inventory.getItemPrices(), approveOrder);

        // Start with Role Selection Panel
        showRoleSelectionPanel();
        System.out.println("Application started. Showing role selection panel.");
    }

    private void showRoleSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JLabel label = new JLabel("Select Your Role:", SwingConstants.CENTER);
        JButton purchasingAgentButton = new JButton("Purchasing Agent");
        JButton vendorRepButton = new JButton("Vendor Representative");

        purchasingAgentButton.addActionListener(e -> {
            System.out.println("Role selected: Purchasing Agent");
            showLoginPanel("Purchasing Agent");
        });
        vendorRepButton.addActionListener(e -> {
            System.out.println("Role selected: Vendor Representative");
            showLoginPanel("Vendor Representative");
        });

        panel.add(label);
        panel.add(purchasingAgentButton);
        panel.add(vendorRepButton);

        updatePanel(panel);
    }

    private void showLoginPanel(String role) {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JLabel label = new JLabel("Login as " + role + ":", SwingConstants.CENTER);
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            System.out.println("Username entered: " + username);
            System.out.println("Password entered: " + password);

            if (validateLogin(role, username, password)) {
                System.out.println("Login successful for role: " + role);
                showMenuPanel(role);
            } else {
                System.out.println("Login failed for role: " + role);
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(label);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(loginButton);

        updatePanel(panel);
        System.out.println("Showing login panel for role: " + role);
    }

    private boolean validateLogin(String role, String username, String password) {
        return credentials.getOrDefault(role, "").equals(password);
    }

    private void showMenuPanel(String role) {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        JLabel label = new JLabel("Welcome, " + role + ". Select a Transaction:", SwingConstants.CENTER);
        panel.add(label);

        JButton createOrderButton = new JButton("Create Purchase Order");
        JButton updateInventoryButton = new JButton("Update Inventory Levels");
        JButton approveOrderButton = new JButton("Approve Purchase Order");
        JButton changeRoleButton = new JButton("Change Role");
        JButton exitButton = new JButton("Exit");

        if ("Purchasing Agent".equals(role)) {
            createOrderButton.addActionListener(e -> {
                System.out.println("Purchasing Agent selected: Create Purchase Order");
                createPurchaseOrder.display(this, () -> showMenuPanel(role));
            });
            updateInventoryButton.addActionListener(e -> {
                System.out.println("Purchasing Agent selected: Update Inventory Levels");
                updateInventory(role);
            });

            panel.add(createOrderButton);
            panel.add(updateInventoryButton);
        }

        if ("Vendor Representative".equals(role)) {
            approveOrderButton.addActionListener(e -> {
                System.out.println("Vendor Representative selected: Approve Purchase Order");
                approveOrder.display(this, () -> showMenuPanel(role));
            });
            panel.add(approveOrderButton);
        }

        changeRoleButton.addActionListener(e -> {
            System.out.println("User selected: Change Role");
            showRoleSelectionPanel();
        });
        exitButton.addActionListener(e -> {
            System.out.println("Application exited.");
            System.exit(0);
        });

        panel.add(changeRoleButton);
        panel.add(exitButton);

        updatePanel(panel);
        System.out.println("Menu panel displayed for role: " + role);
    }

    private void updateInventory(String role) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Update Inventory Levels", SwingConstants.CENTER);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        inventory.getInventory().forEach((item, quantity) -> {
            JPanel itemPanel = new JPanel(new GridLayout(1, 4));
            JLabel itemLabel = new JLabel(item);
            JLabel priceLabel = new JLabel(String.format("Price: $%.2f", inventory.getItemPrices().getOrDefault(item, 0.0)));
            JTextField quantityField = new JTextField();
            JButton updateButton = new JButton("Update");

            updateButton.addActionListener(e -> {
                try {
                    int newQuantity = Integer.parseInt(quantityField.getText());
                    inventory.updateInventory(item, newQuantity - quantity);
                    JOptionPane.showMessageDialog(this, item + " updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateInventory(role);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            itemPanel.add(itemLabel);
            itemPanel.add(priceLabel);
            itemPanel.add(new JLabel("Current Quantity: " + quantity));
            itemPanel.add(quantityField);
            itemPanel.add(updateButton);
            listPanel.add(itemPanel);
        });

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMenuPanel(role));

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        updatePanel(panel);
    }

    private void updatePanel(JPanel newPanel) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = newPanel;
        setContentPane(currentPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Starting application...");
            Primary frame = new Primary();
            frame.setVisible(true);
        });
    }
}
