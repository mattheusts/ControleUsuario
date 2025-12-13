package com.ufes.controleusuario.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
public class UserNotificationListView extends JInternalFrame {
    private JTable tblNotifications;
    private DefaultTableModel tableModel;
    private JButton btnOpen;
    private JButton btnMarkAsRead;
    private JButton btnMarkAllAsRead;
    private JButton btnRefresh;
    private JButton btnClose;
    private JLabel lblStatus;
    private JLabel lblTotal;
    public UserNotificationListView() {
        super("Minhas Notificações", true, true, true, true);
        setSize(750, 520);
        setMinimumSize(new Dimension(650, 450));
        initComponents();
        setLocation(100, 50);
    }
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(250, 250, 250));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250));
        JLabel lblTitle = new JLabel("📬 Suas Notificações");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTotal = new JLabel("Total: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTotal.setForeground(new Color(127, 140, 141));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(new Color(250, 250, 250));
        titlePanel.add(lblTitle);
        titlePanel.add(new JSeparator(SwingConstants.VERTICAL));
        titlePanel.add(lblTotal);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        btnRefresh = new JButton("↻ Atualizar");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.setFocusPainted(false);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        String[] columns = {"ID", "Data/Hora", "Mensagem", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblNotifications = new JTable(tableModel);
        tblNotifications.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblNotifications.setRowHeight(45);
        tblNotifications.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblNotifications.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblNotifications.getTableHeader().setBackground(new Color(52, 152, 219));
        tblNotifications.getTableHeader().setForeground(Color.WHITE);
        tblNotifications.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        tblNotifications.getColumnModel().getColumn(0).setMinWidth(0);
        tblNotifications.getColumnModel().getColumn(0).setMaxWidth(0);
        tblNotifications.getColumnModel().getColumn(0).setWidth(0);
        tblNotifications.getColumnModel().getColumn(1).setPreferredWidth(140);
        tblNotifications.getColumnModel().getColumn(2).setPreferredWidth(420);
        tblNotifications.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblNotifications.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
        JScrollPane scrollPane = new JScrollPane(tblNotifications);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(250, 250, 250));
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        lblStatus = new JLabel("Clique duas vezes para abrir uma notificação.");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatus.setForeground(new Color(127, 140, 141));
        footerPanel.add(lblStatus, BorderLayout.WEST);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonsPanel.setBackground(new Color(250, 250, 250));
        btnOpen = createButton("📖 Abrir", new Color(52, 152, 219));
        btnMarkAsRead = createButton("✓ Marcar Lida", new Color(46, 204, 113));
        btnMarkAllAsRead = createButton("✓✓ Marcar Todas", new Color(39, 174, 96));
        btnClose = createButton("Fechar", new Color(149, 165, 166));
        buttonsPanel.add(btnOpen);
        buttonsPanel.add(btnMarkAsRead);
        buttonsPanel.add(btnMarkAllAsRead);
        buttonsPanel.add(btnClose);
        footerPanel.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            String status = (String) value;
            if ("Não lida".equals(status)) {
                if (!isSelected) {
                    c.setBackground(new Color(255, 245, 220));
                    c.setForeground(new Color(230, 126, 34));
                }
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else {
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(new Color(46, 204, 113));
                }
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
            }
            return c;
        }
    }
    private class MultiLineCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null) {
                String text = value.toString();
                if (text.length() > 80) {
                    text = text.substring(0, 77) + "...";
                }
                setText("<html><body style='width: 380px'>" + text + "</body></html>");
            }
            return c;
        }
    }
    public int getSelectedNotificationId() {
        int selectedRow = tblNotifications.getSelectedRow();
        if (selectedRow >= 0) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }
    public String getSelectedNotificationMessage() {
        int selectedRow = tblNotifications.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) tableModel.getValueAt(selectedRow, 2);
        }
        return null;
    }
    public String getSelectedNotificationDate() {
        int selectedRow = tblNotifications.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) tableModel.getValueAt(selectedRow, 1);
        }
        return null;
    }
    public boolean isSelectedNotificationUnread() {
        int selectedRow = tblNotifications.getSelectedRow();
        if (selectedRow >= 0) {
            return "Não lida".equals(tableModel.getValueAt(selectedRow, 3));
        }
        return false;
    }
    public void setTableData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    public void clearTable() {
        tableModel.setRowCount(0);
    }
    public void setTotal(int total, int unread) {
        if (unread > 0) {
            lblTotal.setText("Total: " + total + " (" + unread + " não lidas)");
            lblTotal.setForeground(new Color(230, 126, 34));
        } else {
            lblTotal.setText("Total: " + total);
            lblTotal.setForeground(new Color(127, 140, 141));
        }
    }
    public void setStatus(String message) {
        lblStatus.setText(message);
    }
    public void setOpenListener(ActionListener listener) {
        btnOpen.addActionListener(listener);
    }
    public void setMarkAsReadListener(ActionListener listener) {
        btnMarkAsRead.addActionListener(listener);
    }
    public void setMarkAllAsReadListener(ActionListener listener) {
        btnMarkAllAsRead.addActionListener(listener);
    }
    public void setRefreshListener(ActionListener listener) {
        btnRefresh.addActionListener(listener);
    }
    public void setCloseListener(ActionListener listener) {
        btnClose.addActionListener(listener);
    }
    public void setTableDoubleClickListener(MouseListener listener) {
        tblNotifications.addMouseListener(listener);
    }
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    public void close() {
        dispose();
    }
}
