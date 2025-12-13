package com.ufes.controleusuario.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserListView extends JInternalFrame {

    private JTable tblUsers;
    private DefaultTableModel tableModel;
    private JButton btnPromote;
    private JButton btnDemote;
    private JButton btnDelete;
    private JButton btnRefresh;
    private JLabel lblStatus;
    private JLabel lblTotalUsers;

    public UserListView() {
        super("Lista de Usuários - Gerenciamento", true, true, true, true);
        setSize(900, 550);
        setMinimumSize(new Dimension(800, 450));

        initComponents();
        setLocation(100, 50);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(250, 250, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250));

        JLabel lblTitle = new JLabel("Gerenciamento de Usuários");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(50, 50, 50));

        lblTotalUsers = new JLabel("Total: 0 usuários");
        lblTotalUsers.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTotalUsers.setForeground(new Color(100, 100, 100));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(new Color(250, 250, 250));
        titlePanel.add(lblTitle);
        titlePanel.add(new JSeparator(SwingConstants.VERTICAL));
        titlePanel.add(lblTotalUsers);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        btnRefresh = new JButton("↻ Atualizar");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.setFocusPainted(false);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Nome", "Usuário", "Perfil", "Status", "Data Cadastro", "Total Notif.", "Lidas"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblUsers = new JTable(tableModel);
        tblUsers.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblUsers.setRowHeight(28);
        tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblUsers.getTableHeader().setBackground(new Color(70, 130, 180));
        tblUsers.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblUsers.getColumnCount(); i++) {
            tblUsers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tblUsers.getColumnModel().getColumn(0).setMinWidth(0);
        tblUsers.getColumnModel().getColumn(0).setMaxWidth(0);
        tblUsers.getColumnModel().getColumn(0).setWidth(0);

        tblUsers.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblUsers.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblUsers.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblUsers.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblUsers.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblUsers.getColumnModel().getColumn(6).setPreferredWidth(80);
        tblUsers.getColumnModel().getColumn(7).setPreferredWidth(60);

        JScrollPane scrollPane = new JScrollPane(tblUsers);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(250, 250, 250));
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblStatus = new JLabel("Selecione um usuário para gerenciar.");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatus.setForeground(new Color(100, 100, 100));
        footerPanel.add(lblStatus, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(250, 250, 250));

        btnPromote = createButton("⬆ Promover", new Color(46, 125, 50));
        btnDemote = createButton("⬇ Rebaixar", new Color(200, 150, 50));
        btnDelete = createButton("✕ Excluir", new Color(180, 60, 60));

        buttonsPanel.add(btnPromote);
        buttonsPanel.add(btnDemote);
        buttonsPanel.add(btnDelete);
        footerPanel.add(buttonsPanel, BorderLayout.EAST);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public int getSelectedUserId() {
        int selectedRow = tblUsers.getSelectedRow();
        if (selectedRow >= 0) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public String getSelectedUserName() {
        int selectedRow = tblUsers.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) tableModel.getValueAt(selectedRow, 1);
        }
        return null;
    }

    public String getSelectedUserProfile() {
        int selectedRow = tblUsers.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) tableModel.getValueAt(selectedRow, 3);
        }
        return null;
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

    public void setTotalUsers(int total) {
        lblTotalUsers.setText("Total: " + total + " usuário(s)");
    }

    public void setStatus(String message) {
        lblStatus.setText(message);
    }

    public void setStatusColor(Color color) {
        lblStatus.setForeground(color);
    }

    public void setPromoteEnabled(boolean enabled) {
        btnPromote.setEnabled(enabled);
    }

    public void setDemoteEnabled(boolean enabled) {
        btnDemote.setEnabled(enabled);
    }

    public void setDeleteEnabled(boolean enabled) {
        btnDelete.setEnabled(enabled);
    }

    public void setPromoteVisible(boolean visible) {
        btnPromote.setVisible(visible);
    }

    public void setDemoteVisible(boolean visible) {
        btnDemote.setVisible(visible);
    }

    public void setPromoteListener(ActionListener listener) {
        btnPromote.addActionListener(listener);
    }

    public void setDemoteListener(ActionListener listener) {
        btnDemote.addActionListener(listener);
    }

    public void setDeleteListener(ActionListener listener) {
        btnDelete.addActionListener(listener);
    }

    public void setRefreshListener(ActionListener listener) {
        btnRefresh.addActionListener(listener);
    }

    public void setTableSelectionListener(javax.swing.event.ListSelectionListener listener) {
        tblUsers.getSelectionModel().addListSelectionListener(listener);
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

    public boolean showConfirm(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirmação",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    public void close() {
        dispose();
    }
}
