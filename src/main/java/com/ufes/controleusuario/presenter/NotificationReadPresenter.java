package com.ufes.controleusuario.presenter;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.INotificationRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.NotificationReadView;
import java.awt.event.ActionEvent;
public class NotificationReadPresenter {
    private NotificationReadView view;
    private INotificationRepository notificationRepository;
    private ILoggerService logger;
    private User currentUser;
    private int notificationId;
    private boolean isUnread;
    private Runnable onUpdate;
    public NotificationReadPresenter(NotificationReadView view,
                                     INotificationRepository notificationRepository,
                                     ILoggerService logger,
                                     User currentUser,
                                     int notificationId,
                                     String message,
                                     String date,
                                     boolean isUnread,
                                     Runnable onUpdate) {
        this.view = view;
        this.notificationRepository = notificationRepository;
        this.logger = logger;
        this.currentUser = currentUser;
        this.notificationId = notificationId;
        this.isUnread = isUnread;
        this.onUpdate = onUpdate;
        initView(message, date);
        initListeners();
    }
    private void initView(String message, String date) {
        view.setMessage(message);
        view.setDate(date);
        view.setReadStatus(!isUnread);
    }
    private void initListeners() {
        view.setMarkAsReadListener(this::onMarkAsRead);
        view.setCloseListener(this::onClose);
    }
    private void onMarkAsRead(ActionEvent e) {
        if (!isUnread) {
            view.showMessage("Esta notificação já foi lida.");
            return;
        }
        try {
            notificationRepository.markAsRead(notificationId);
            isUnread = false;
            view.setReadStatus(true);
            logger.log("NOTIFICATION_READ", "Notificação lida pelo usuário: " + currentUser.getNome());
            view.showSuccess("Notificação marcada como lida.");
            if (onUpdate != null) {
                onUpdate.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao marcar notificação como lida: " + ex.getMessage());
        }
    }
    private void onClose(ActionEvent e) {
        view.close();
    }
}
