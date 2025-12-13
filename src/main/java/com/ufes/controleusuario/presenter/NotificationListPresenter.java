package com.ufes.controleusuario.presenter;

import com.ufes.controleusuario.model.Notification;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.INotificationRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.NotificationListView;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * NotificationListPresenter - Lógica para visualização de notificações
 * Padrão MVP - Toda lógica aqui
 */
public class NotificationListPresenter {

    private NotificationListView view;
    private INotificationRepository notificationRepository;
    private ILoggerService logger;
    private User currentUser;
    private Runnable onUpdate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public NotificationListPresenter(NotificationListView view,
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
        view.setMarkAsReadListener(this::onMarkAsRead);
        view.setMarkAllAsReadListener(this::onMarkAllAsRead);
        view.setRefreshListener(this::onRefresh);
        view.setCloseListener(this::onClose);
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
            view.setStatus(unreadCount + " notificação(ões) não lida(s).");
        } else {
            view.setStatus("Todas as notificações foram lidas.");
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

