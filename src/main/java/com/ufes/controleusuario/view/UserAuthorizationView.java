package com.ufes.controleusuario.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class UserAuthorizationView extends JInternalFrame {

    private JTable tblPendingUsers;
    private DefaultTableModel tableModel;
    private JButton btnAuthorize;
    private JButton btnReject;
    private JButton btnRefresh;
    private JLabel lblStatus;

    public UserAuthorizationView() {
        super("Autorização de Usuários Pendentes", true, true, true, true);
        setSize(650, 450);
        setMinimumSize(new Dimension(550, 400));

        initComponents();
        setLocation(80, 30);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(250, 250, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250));

        JLabel lblTitle = new JLabel("Usuários Aguardando Autorização");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(50, 50, 50));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        btnRefresh = new JButton("↻ Atualizar");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.setFocusPainted(false);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Nome", "Usuário", "Data Cadastro", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPendingUsers = new JTable(tableModel);
        tblPendingUsers.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblPendingUsers.setRowHeight(28);
        tblPendingUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPendingUsers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblPendingUsers.getTableHeader().setBackground(new Color(70, 130, 180));
        tblPendingUsers.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblPendingUsers.getColumnCount(); i++) {
            tblPendingUsers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tblPendingUsers.getColumnModel().getColumn(0).setMinWidth(0);
        tblPendingUsers.getColumnModel().getColumn(0).setMaxWidth(0);
        tblPendingUsers.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(tblPendingUsers);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(250, 250, 250));
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblStatus = new JLabel("Selecione um usuário para autorizar ou rejeitar.");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatus.setForeground(new Color(100, 100, 100));
        footerPanel.add(lblStatus, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(250, 250, 250));

        btnReject = createButton("✕ Rejeitar", new Color(180, 60, 60));
        btnAuthorize = createButton("✓ Autorizar", new Color(46, 125, 50));

        buttonsPanel.add(btnReject);
        buttonsPanel.add(btnAuthorize);
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
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public int getSelectedUserId() {
        int selectedRow = tblPendingUsers.getSelectedRow();
        if (selectedRow >= 0) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    public String getSelectedUserName() {
        int selectedRow = tblPendingUsers.getSelectedRow();
        if (selectedRow >= 0) {
            return (String) tableModel.getValueAt(selectedRow, 1);
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

    public void setStatus(String message) {
        lblStatus.setText(message);
    }

    public void setStatusColor(Color color) {
        lblStatus.setForeground(color);
    }

    public void setAuthorizeListener(ActionListener listener) {
        btnAuthorize.addActionListener(listener);
    }

    public void setRejectListener(ActionListener listener) {
        btnReject.addActionListener(listener);
    }

    public void setRefreshListener(ActionListener listener) {
        btnRefresh.addActionListener(listener);
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
