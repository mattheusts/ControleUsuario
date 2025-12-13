package com.ufes.controleusuario.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
public class NotificationSendView extends JInternalFrame {
    private JList<String> lstUsers;
    private DefaultListModel<String> listModel;
    private JTextArea txtMessage;
    private JButton btnSend;
    private JButton btnSelectAll;
    private JButton btnClearSelection;
    private JButton btnCancel;
    private JLabel lblSelectedCount;
    private JLabel lblCharCount;
    private List<Integer> userIds;
    public NotificationSendView() {
        super("Enviar Notificação", true, true, true, true);
        setSize(550, 500);
        setMinimumSize(new Dimension(500, 450));
        userIds = new ArrayList<>();
        initComponents();
        setLocation(120, 60);
    }
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(250, 250, 250));
        JPanel usersPanel = new JPanel(new BorderLayout(5, 5));
        usersPanel.setBackground(new Color(250, 250, 250));
        JLabel lblUsersTitle = new JLabel("Selecionar Destinatários:");
        lblUsersTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsersTitle.setForeground(new Color(50, 50, 50));
        usersPanel.add(lblUsersTitle, BorderLayout.NORTH);
        listModel = new DefaultListModel<>();
        lstUsers = new JList<>(listModel);
        lstUsers.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lstUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lstUsers.setVisibleRowCount(8);
        JScrollPane scrollUsers = new JScrollPane(lstUsers);
        scrollUsers.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollUsers.setPreferredSize(new Dimension(0, 150));
        usersPanel.add(scrollUsers, BorderLayout.CENTER);
        JPanel selectionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        selectionButtonsPanel.setBackground(new Color(250, 250, 250));
        btnSelectAll = new JButton("Selecionar Todos");
        btnSelectAll.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnSelectAll.setFocusPainted(false);
        btnClearSelection = new JButton("Limpar Seleção");
        btnClearSelection.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnClearSelection.setFocusPainted(false);
        lblSelectedCount = new JLabel("0 selecionado(s)");
        lblSelectedCount.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSelectedCount.setForeground(new Color(100, 100, 100));
        selectionButtonsPanel.add(btnSelectAll);
        selectionButtonsPanel.add(btnClearSelection);
        selectionButtonsPanel.add(Box.createHorizontalStrut(20));
        selectionButtonsPanel.add(lblSelectedCount);
        usersPanel.add(selectionButtonsPanel, BorderLayout.SOUTH);
        mainPanel.add(usersPanel, BorderLayout.NORTH);
        JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
        messagePanel.setBackground(new Color(250, 250, 250));
        JLabel lblMessageTitle = new JLabel("Mensagem:");
        lblMessageTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMessageTitle.setForeground(new Color(50, 50, 50));
        messagePanel.add(lblMessageTitle, BorderLayout.NORTH);
        txtMessage = new JTextArea();
        txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scrollMessage = new JScrollPane(txtMessage);
        scrollMessage.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        messagePanel.add(scrollMessage, BorderLayout.CENTER);
        lblCharCount = new JLabel("0 caracteres");
        lblCharCount.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblCharCount.setForeground(new Color(100, 100, 100));
        messagePanel.add(lblCharCount, BorderLayout.SOUTH);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonsPanel.setBackground(new Color(250, 250, 250));
        btnCancel = createButton("Cancelar", new Color(100, 100, 100));
        btnSend = createButton("📤 Enviar", new Color(46, 125, 50));
        buttonsPanel.add(btnCancel);
        buttonsPanel.add(btnSend);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
        lstUsers.addListSelectionListener(e -> updateSelectedCount());
        txtMessage.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateCharCount(); }
        });
    }
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
    private void updateSelectedCount() {
        int count = lstUsers.getSelectedIndices().length;
        lblSelectedCount.setText(count + " selecionado(s)");
    }
    private void updateCharCount() {
        int count = txtMessage.getText().length();
        lblCharCount.setText(count + " caracteres");
    }
    public String getMessage() {
        return txtMessage.getText().trim();
    }
    public List<Integer> getSelectedUserIds() {
        List<Integer> selected = new ArrayList<>();
        int[] indices = lstUsers.getSelectedIndices();
        for (int index : indices) {
            if (index >= 0 && index < userIds.size()) {
                selected.add(userIds.get(index));
            }
        }
        return selected;
    }
    public int getSelectedCount() {
        return lstUsers.getSelectedIndices().length;
    }
    public void setUsers(List<Integer> ids, List<String> names) {
        userIds.clear();
        listModel.clear();
        for (int i = 0; i < ids.size(); i++) {
            userIds.add(ids.get(i));
            listModel.addElement(names.get(i));
        }
    }
    public void clearSelection() {
        lstUsers.clearSelection();
    }
    public void selectAll() {
        lstUsers.setSelectionInterval(0, listModel.size() - 1);
    }
    public void clearMessage() {
        txtMessage.setText("");
    }
    public void setSendListener(ActionListener listener) {
        btnSend.addActionListener(listener);
    }
    public void setSelectAllListener(ActionListener listener) {
        btnSelectAll.addActionListener(listener);
    }
    public void setClearSelectionListener(ActionListener listener) {
        btnClearSelection.addActionListener(listener);
    }
    public void setCancelListener(ActionListener listener) {
        btnCancel.addActionListener(listener);
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
