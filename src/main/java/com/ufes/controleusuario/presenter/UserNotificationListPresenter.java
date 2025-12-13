package com.ufes.controleusuario.presenter;
import com.ufes.controleusuario.model.Notification;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.INotificationRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.NotificationReadView;
import com.ufes.controleusuario.view.UserNotificationListView;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class UserNotificationListPresenter {
    private UserNotificationListView view;
    private INotificationRepository notificationRepository;
    private ILoggerService logger;
    private User currentUser;
    private Runnable onUpdate;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public UserNotificationListPresenter(UserNotificationListView view,
                                         INotificationRepository notificationRepository,
                                         ILoggerService logger,
                                         User currentUser,
                                         Runnable onUpdate) {
        this.view = view;
        this.notificationRepository = notificationRepository;
        this.logger = logger;
        this.currentUser = currentUser;
        this.onUpdate = onUpdate;
        initListeners();
        loadNotifications();
    }
    private void initListeners() {
        view.setOpenListener(this::onOpen);
        view.setMarkAsReadListener(this::onMarkAsRead);
        view.setMarkAllAsReadListener(this::onMarkAllAsRead);
        view.setRefreshListener(this::onRefresh);
        view.setCloseListener(this::onClose);
        view.setTableDoubleClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onOpen(null);
                }
            }
        });
    }
    private void loadNotifications() {
        List<Notification> notifications = notificationRepository.findByUserId(currentUser.getId());
        if (notifications.isEmpty()) {
            view.clearTable();
            view.setTotal(0, 0);
            view.setStatus("Você não tem notificações.");
            return;
        }
        int unreadCount = 0;
        Object[][] data = new Object[notifications.size()][4];
        for (int i = 0; i < notifications.size(); i++) {
            Notification notif = notifications.get(i);
            String dateStr = notif.getCreatedAt() != null
                    ? notif.getCreatedAt().format(DATE_FORMATTER)
                    : "-";
            String status = notif.isRead() ? "Lida" : "Não lida";
            if (!notif.isRead()) {
                unreadCount++;
            }
            data[i][0] = notif.getId();
            data[i][1] = dateStr;
            data[i][2] = notif.getMessage();
            data[i][3] = status;
        }
        view.setTableData(data);
        view.setTotal(notifications.size(), unreadCount);
        if (unreadCount > 0) {
            view.setStatus(unreadCount + " notificação(ões) não lida(s). Clique duas vezes para abrir.");
        } else {
            view.setStatus("Todas as notificações foram lidas.");
        }
    }
    private void onOpen(ActionEvent e) {
        int notificationId = view.getSelectedNotificationId();
        String message = view.getSelectedNotificationMessage();
        String date = view.getSelectedNotificationDate();
        boolean isUnread = view.isSelectedNotificationUnread();
        if (notificationId < 0) {
            view.showError("Selecione uma notificação para abrir.");
            return;
        }
        NotificationReadView readView = new NotificationReadView();
        new NotificationReadPresenter(
            readView,
            notificationRepository,
            logger,
            currentUser,
            notificationId,
            message,
            date,
            isUnread,
            () -> {
                loadNotifications();
                if (onUpdate != null) {
                    onUpdate.run();
                }
            }
        );
        java.awt.Container parent = view.getDesktopPane();
        if (parent instanceof javax.swing.JDesktopPane) {
            ((javax.swing.JDesktopPane) parent).add(readView);
            readView.setVisible(true);
            try {
                readView.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void onMarkAsRead(ActionEvent e) {
        int notificationId = view.getSelectedNotificationId();
        if (notificationId < 0) {
            view.showError("Selecione uma notificação para marcar como lida.");
            return;
        }
        try {
            notificationRepository.markAsRead(notificationId);
            logger.log("NOTIFICATION_READ", "Notificação marcada como lida por: " + currentUser.getNome());
            loadNotifications();
            if (onUpdate != null) {
                onUpdate.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao marcar notificação: " + ex.getMessage());
        }
    }
    private void onMarkAllAsRead(ActionEvent e) {
        try {
            notificationRepository.markAllAsReadByUserId(currentUser.getId());
            logger.log("NOTIFICATIONS_ALL_READ", "Todas notificações marcadas como lidas por: " + currentUser.getNome());
            view.showSuccess("Todas as notificações foram marcadas como lidas.");
            loadNotifications();
            if (onUpdate != null) {
                onUpdate.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao marcar notificações: " + ex.getMessage());
        }
    }
    private void onRefresh(ActionEvent e) {
        loadNotifications();
        view.setStatus("Lista atualizada.");
    }
    private void onClose(ActionEvent e) {
        view.close();
    }
}
