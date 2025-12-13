package com.ufes.controleusuario.presenter;

import com.ufes.controleusuario.model.Notification;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.INotificationRepository;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.NotificationSendView;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationSendPresenter - Lógica para envio de notificações
 * Padrão MVP - Toda lógica aqui
 */
public class NotificationSendPresenter {

    private NotificationSendView view;
    private IUserRepository userRepository;
    private INotificationRepository notificationRepository;
    private ILoggerService logger;
    private User currentAdmin;
    private Runnable onSuccess;

    public NotificationSendPresenter(NotificationSendView view,
                                     IUserRepository userRepository,
                                     INotificationRepository notificationRepository,
                                     ILoggerService logger,
                                     User currentAdmin,
                                     Runnable onSuccess) {
        this.view = view;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.logger = logger;
        this.currentAdmin = currentAdmin;
        this.onSuccess = onSuccess;

        initListeners();
        loadAuthorizedUsers();
    }

    private void initListeners() {
        view.setSendListener(this::onSend);
        view.setSelectAllListener(this::onSelectAll);
        view.setClearSelectionListener(this::onClearSelection);
        view.setCancelListener(this::onCancel);
    }

    private void loadAuthorizedUsers() {
        List<User> users = userRepository.findAuthorizedUsers();

        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();

        for (User user : users) {
            ids.add(user.getId());
            names.add(user.getNome() + " (" + user.getUsuario() + ") - " + user.getTipo());
        }

        view.setUsers(ids, names);
    }

    private void onSend(ActionEvent e) {
        List<Integer> selectedUserIds = view.getSelectedUserIds();
        String message = view.getMessage();

        // Validations
        if (selectedUserIds.isEmpty()) {
            view.showError("Selecione pelo menos um destinatário.");
            return;
        }

        if (message.isEmpty()) {
            view.showError("Digite uma mensagem para enviar.");
            return;
        }

        if (message.length() > 1000) {
            view.showError("A mensagem não pode ter mais de 1000 caracteres.");
            return;
        }

        // Confirm
        if (!view.showConfirm("Enviar notificação para " + selectedUserIds.size() + " usuário(s)?")) {
            return;
        }

        try {
            int successCount = 0;
            LocalDateTime now = LocalDateTime.now();

            for (Integer userId : selectedUserIds) {
                Notification notification = new Notification(
                    0,
                    userId,
                    message,
                    false, // Not read
                    now
                );
                notificationRepository.save(notification);
                successCount++;
            }

            logger.log("NOTIFICATION_SEND_SUCCESS",
                    "Notificação enviada para " + successCount + " usuário(s) por: " + currentAdmin.getNome());

            view.showSuccess("Notificação enviada com sucesso para " + successCount + " usuário(s)!");
            view.clearMessage();
            view.clearSelection();

            if (onSuccess != null) {
                onSuccess.run();
            }

            view.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao enviar notificação: " + ex.getMessage());
            logger.log("NOTIFICATION_SEND_FAIL", "Erro ao enviar notificação: " + ex.getMessage());
        }
    }

    private void onSelectAll(ActionEvent e) {
        view.selectAll();
    }

    private void onClearSelection(ActionEvent e) {
        view.clearSelection();
    }

    private void onCancel(ActionEvent e) {
        view.close();
    }
}

