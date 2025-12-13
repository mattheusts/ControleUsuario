package com.ufes.controleusuario.presenter;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.UserAuthorizationView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;
public class UserAuthorizationPresenter {
    private UserAuthorizationView view;
    private IUserRepository userRepository;
    private ILoggerService logger;
    private User currentAdmin;
    private Runnable onUpdate;
    public UserAuthorizationPresenter(UserAuthorizationView view,
                                      IUserRepository userRepository,
                                      ILoggerService logger,
                                      User currentAdmin,
                                      Runnable onUpdate) {
        this.view = view;
        this.userRepository = userRepository;
        this.logger = logger;
        this.currentAdmin = currentAdmin;
        this.onUpdate = onUpdate;
        initListeners();
        loadPendingUsers();
    }
    private void initListeners() {
        view.setAuthorizeListener(this::onAuthorize);
        view.setRejectListener(this::onReject);
        view.setRefreshListener(this::onRefresh);
    }
    private void loadPendingUsers() {
        List<User> pendingUsers = userRepository.findByStatus("PENDENTE");
        if (pendingUsers.isEmpty()) {
            view.clearTable();
            view.setStatus("Não há usuários pendentes de autorização.");
            view.setStatusColor(new Color(100, 100, 100));
            return;
        }
        Object[][] data = new Object[pendingUsers.size()][5];
        for (int i = 0; i < pendingUsers.size(); i++) {
            User user = pendingUsers.get(i);
            data[i][0] = user.getId();
            data[i][1] = user.getNome();
            data[i][2] = user.getUsuario();
            data[i][3] = user.getDataCadastro() != null ? user.getDataCadastro().toString() : "-";
            data[i][4] = "Pendente";
        }
        view.setTableData(data);
        view.setStatus(pendingUsers.size() + " usuário(s) aguardando autorização.");
        view.setStatusColor(new Color(200, 150, 50));
    }
    private void onAuthorize(ActionEvent e) {
        int userId = view.getSelectedUserId();
        String userName = view.getSelectedUserName();
        if (userId < 0) {
            view.showError("Selecione um usuário para autorizar.");
            return;
        }
        if (!view.showConfirm("Deseja autorizar o usuário \"" + userName + "\"?")) {
            return;
        }
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                view.showError("Usuário não encontrado.");
                logger.log("AUTHORIZATION_FAIL", "Usuário não encontrado: ID " + userId);
                return;
            }
            user.setStatus("AUTORIZADO");
            userRepository.update(user);
            logger.log("AUTHORIZATION_SUCCESS", "Usuário autorizado: " + user.getNome() +
                    " por: " + currentAdmin.getNome());
            view.showSuccess("Usuário \"" + userName + "\" autorizado com sucesso!");
            loadPendingUsers();
            if (onUpdate != null) {
                onUpdate.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao autorizar usuário: " + ex.getMessage());
            logger.log("AUTHORIZATION_FAIL", "Erro ao autorizar: " + ex.getMessage());
        }
    }
    private void onReject(ActionEvent e) {
        int userId = view.getSelectedUserId();
        String userName = view.getSelectedUserName();
        if (userId < 0) {
            view.showError("Selecione um usuário para rejeitar.");
            return;
        }
        if (!view.showConfirm("Deseja REJEITAR e EXCLUIR o usuário \"" + userName + "\"?\n\nEsta ação não pode ser desfeita.")) {
            return;
        }
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                view.showError("Usuário não encontrado.");
                logger.log("REJECTION_FAIL", "Usuário não encontrado: ID " + userId);
                return;
            }
            userRepository.delete(user);
            logger.log("REJECTION_SUCCESS", "Usuário rejeitado e removido: " + user.getNome() +
                    " por: " + currentAdmin.getNome());
            view.showSuccess("Usuário \"" + userName + "\" rejeitado e removido do sistema.");
            loadPendingUsers();
            if (onUpdate != null) {
                onUpdate.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao rejeitar usuário: " + ex.getMessage());
            logger.log("REJECTION_FAIL", "Erro ao rejeitar: " + ex.getMessage());
        }
    }
    private void onRefresh(ActionEvent e) {
        loadPendingUsers();
        view.setStatus("Lista atualizada.");
    }
}
