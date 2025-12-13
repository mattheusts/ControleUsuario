package com.ufes.controleusuario.presenter;

import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.INotificationRepository;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.SystemRestoreView;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.SwingWorker;

/**
 * SystemRestorePresenter - Lógica para restauração do sistema
 * Padrão MVP - Toda lógica aqui
 * SOMENTE para o primeiro administrador
 */
public class SystemRestorePresenter {

    private SystemRestoreView view;
    private IUserRepository userRepository;
    private INotificationRepository notificationRepository;
    private ILoggerService logger;
    private User currentAdmin;
    private Runnable onSuccess;

    public SystemRestorePresenter(SystemRestoreView view,
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

        // Verify first admin
        User firstAdmin = userRepository.findFirstAdmin();
        if (firstAdmin == null || firstAdmin.getId() != currentAdmin.getId()) {
            view.showError("Acesso negado. Apenas o primeiro administrador pode restaurar o sistema.");
            view.close();
            return;
        }

        initListeners();
    }

    private void initListeners() {
        view.setRestoreListener(this::onRestore);
        view.setCancelListener(this::onCancel);
    }

    private void onRestore(ActionEvent e) {
        String password1 = view.getPassword1();
        String password2 = view.getPassword2();

        // Validate passwords
        if (password1.isEmpty() || password2.isEmpty()) {
            view.showError("Digite sua senha duas vezes para confirmar.");
            return;
        }

        if (!password1.equals(password2)) {
            view.showError("As senhas não conferem. Tente novamente.");
            view.clearFields();
            return;
        }

        // Verify password matches current admin's password
        if (!password1.equals(currentAdmin.getSenha())) {
            view.showError("Senha incorreta. Operação cancelada.");
            logger.log("RESTORE_FAIL", "Tentativa de restauração com senha incorreta por: " + currentAdmin.getNome());
            view.clearFields();
            return;
        }

        // Final confirmation dialog
        boolean confirmed = view.showConfirm(
            "ÚLTIMA CONFIRMAÇÃO\n\n" +
            "Você está prestes a restaurar o sistema.\n" +
            "TODOS os dados serão apagados permanentemente:\n" +
            "• Todos os usuários\n" +
            "• Todas as notificações\n" +
            "• Todas as configurações\n\n" +
            "A aplicação será reiniciada.\n\n" +
            "Deseja realmente continuar?"
        );

        if (!confirmed) {
            logger.log("RESTORE_CANCELLED", "Restauração cancelada pelo usuário: " + currentAdmin.getNome());
            return;
        }

        // Proceed with restore
        executeRestore();
    }

    private void executeRestore() {
        view.setButtonsEnabled(false);
        view.showProgressPanel();

        // Execute in background thread
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Log before deletion
                logger.log("RESTORE_START", "Iniciando restauração do sistema por: " + currentAdmin.getNome());

                // Small delay to show progress
                Thread.sleep(1000);

                // Delete database file
                deleteDatabaseFile();

                Thread.sleep(500);

                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions

                    logger.log("RESTORE_SUCCESS", "Sistema restaurado com sucesso por: " + currentAdmin.getNome());

                    if (onSuccess != null) {
                        onSuccess.run();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    view.showError("Erro ao restaurar o sistema: " + ex.getMessage());
                    logger.log("RESTORE_FAIL", "Erro na restauração: " + ex.getMessage());
                    view.setButtonsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void deleteDatabaseFile() {
        // Delete SQLite database file
        File dbFile = new File("sistema.db");
        if (dbFile.exists()) {
            boolean deleted = dbFile.delete();
            if (!deleted) {
                throw new RuntimeException("Não foi possível deletar o arquivo do banco de dados.");
            }
        }

        // Also delete any log files if needed
        File logCsv = new File("log.csv");
        if (logCsv.exists()) {
            logCsv.delete();
        }

        File logJsonl = new File("log.jsonl");
        if (logJsonl.exists()) {
            logJsonl.delete();
        }
    }

    private void onCancel(ActionEvent e) {
        view.close();
    }
}

