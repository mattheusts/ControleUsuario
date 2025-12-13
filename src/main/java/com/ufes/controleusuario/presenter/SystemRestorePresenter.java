package com.ufes.controleusuario.presenter;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.INotificationRepository;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.SystemRestoreView;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.SwingWorker;
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
        if (password1.isEmpty() || password2.isEmpty()) {
            view.showError("Digite sua senha duas vezes para confirmar.");
            return;
        }
        if (!password1.equals(password2)) {
            view.showError("As senhas não conferem. Tente novamente.");
            view.clearFields();
            return;
        }
        if (!password1.equals(currentAdmin.getSenha())) {
            view.showError("Senha incorreta. Operação cancelada.");
            logger.log("RESTORE_FAIL", "Tentativa de restauração com senha incorreta por: " + currentAdmin.getNome());
            view.clearFields();
            return;
        }
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
        executeRestore();
    }
    private void executeRestore() {
        view.setButtonsEnabled(false);
        view.showProgressPanel();
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                logger.log("RESTORE_START", "Iniciando restauração do sistema por: " + currentAdmin.getNome());
                Thread.sleep(1000);
                deleteDatabaseFile();
                Thread.sleep(500);
                return null;
            }
            @Override
            protected void done() {
                try {
                    get();
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
        File dbFile = new File("sistema.db");
        if (dbFile.exists()) {
            boolean deleted = dbFile.delete();
            if (!deleted) {
                throw new RuntimeException("Não foi possível deletar o arquivo do banco de dados.");
            }
        }
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
