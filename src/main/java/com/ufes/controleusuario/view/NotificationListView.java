package com.ufes.controleusuario.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

public class NotificationListView extends JInternalFrame {
    private JTable tblNotifications;
    private DefaultTableModel tableModel;
    private JButton btnMarkAsRead;
    private JButton btnMarkAllAsRead;
    private JButton btnRefresh;
    private JButton btnClose;
    private JLabel lblStatus;
    private JLabel lblTotal;

    public NotificationListView() {
        super("Minhas Notificações", true, true, true, true);
        setSize(700, 500);
        setMinimumSize(new Dimension(600, 400));
        initComponents();
        setLocation(140, 70);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(250, 250, 250));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250));
        JLabel lblTitle = new JLabel("📬 Suas Notificações");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(50, 50, 50));
        lblTotal = new JLabel("Total: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTotal.setForeground(new Color(100, 100, 100));
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
        String[] columns = { "ID", "Data/Hora", "Mensagem", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblNotifications = new JTable(tableModel);
        tblNotifications.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblNotifications.setRowHeight(40);
        tblNotifications.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblNotifications.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblNotifications.getTableHeader().setBackground(new Color(70, 130, 180));
        tblNotifications.getTableHeader().setForeground(Color.WHITE);
        tblNotifications.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        tblNotifications.getColumnModel().getColumn(0).setMinWidth(0);
        tblNotifications.getColumnModel().getColumn(0).setMaxWidth(0);
        tblNotifications.getColumnModel().getColumn(0).setWidth(0);
        tblNotifications.getColumnModel().getColumn(1).setPreferredWidth(130);
        tblNotifications.getColumnModel().getColumn(2).setPreferredWidth(400);
        tblNotifications.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblNotifications.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
        JScrollPane scrollPane = new JScrollPane(tblNotifications);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(250, 250, 250));
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        lblStatus = new JLabel("Selecione uma notificação para marcar como lida.");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatus.setForeground(new Color(100, 100, 100));
        footerPanel.add(lblStatus, BorderLayout.WEST);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(250, 250, 250));
        btnMarkAsRead = createButton("✓ Marcar como Lida");
        btnMarkAllAsRead = createButton("✓✓ Marcar Todas");
        btnClose = createButton("Fechar");
        buttonsPanel.add(btnMarkAsRead);
        buttonsPanel.add(btnMarkAllAsRead);
        buttonsPanel.add(btnClose);
        footerPanel.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
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
                    c.setForeground(new Color(200, 130, 50));
                }
                setFont(new Font("Segoe UI", Font.BOLD, 12));
            } else {
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(new Color(100, 150, 100));
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
                setText("<html><body style='width: 350px'>" + text + "</body></html>");
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
            lblTotal.setForeground(new Color(200, 130, 50));
        } else {
            lblTotal.setText("Total: " + total);
            lblTotal.setForeground(new Color(100, 100, 100));
        }
    }

    public void setStatus(String message) {
        lblStatus.setText(message);
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
