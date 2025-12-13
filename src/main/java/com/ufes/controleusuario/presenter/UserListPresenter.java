package com.ufes.controleusuario.presenter;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.INotificationRepository;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.UserListView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import java.util.List;
public class UserListPresenter {
    private UserListView view;
    private IUserRepository userRepository;
    private INotificationRepository notificationRepository;
    private ILoggerService logger;
    private User currentAdmin;
    private boolean isFirstAdmin;
    private Runnable onUpdate;
    private User firstAdmin;
    public UserListPresenter(UserListView view,
                             IUserRepository userRepository,
                             INotificationRepository notificationRepository,
                             ILoggerService logger,
                             User currentAdmin,
                             boolean isFirstAdmin,
                             Runnable onUpdate) {
        this.view = view;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.logger = logger;
        this.currentAdmin = currentAdmin;
        this.isFirstAdmin = isFirstAdmin;
        this.onUpdate = onUpdate;
        this.firstAdmin = userRepository.findFirstAdmin();
        initListeners();
        configurePermissions();
        loadUsers();
    }
    private void configurePermissions() {
        if (!isFirstAdmin) {
            view.setPromoteVisible(false);
            view.setDemoteVisible(false);
        }
    }
    private void initListeners() {
        view.setPromoteListener(this::onPromote);
        view.setDemoteListener(this::onDemote);
        view.setDeleteListener(this::onDelete);
        view.setRefreshListener(this::onRefresh);
        view.setTableSelectionListener(this::onTableSelection);
    }
    private void loadUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            view.clearTable();
            view.setTotalUsers(0);
            view.setStatus("Nenhum usuário cadastrado.");
            return;
        }
        Object[][] data = new Object[users.size()][8];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            int totalNotifications = notificationRepository.countByUserId(user.getId());
            int unreadNotifications = notificationRepository.countUnreadByUserId(user.getId());
            int readNotifications = totalNotifications - unreadNotifications;
            data[i][0] = user.getId();
            data[i][1] = user.getNome();
            data[i][2] = user.getUsuario();
            data[i][3] = user.getTipo();
            data[i][4] = user.getStatus();
            data[i][5] = user.getDataCadastro() != null ? user.getDataCadastro().toString() : "-";
            data[i][6] = totalNotifications;
            data[i][7] = readNotifications;
        }
        view.setTableData(data);
        view.setTotalUsers(users.size());
        view.setStatus("Selecione um usuário para gerenciar.");
        view.setStatusColor(new Color(100, 100, 100));
    }
    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int userId = view.getSelectedUserId();
        String profile = view.getSelectedUserProfile();
        if (userId < 0) return;
        boolean isSelectedFirstAdmin = (firstAdmin != null && firstAdmin.getId() == userId);
        boolean isSelectedSelf = (userId == currentAdmin.getId());
        if (isFirstAdmin) {
            view.setPromoteEnabled("PADRAO".equals(profile));
            view.setDemoteEnabled("ADMIN".equals(profile) && !isSelectedFirstAdmin && !isSelectedSelf);
        }
        boolean canDelete = !isSelectedSelf;
        if (!isFirstAdmin && "ADMIN".equals(profile)) {
            canDelete = false;
        }
        if (isSelectedFirstAdmin) {
            canDelete = false;
        }
        view.setDeleteEnabled(canDelete);
        if (isSelectedSelf) {
            view.setStatus("Você está selecionado. Não é possível alterar seu próprio perfil.");
            view.setStatusColor(new Color(200, 150, 50));
        } else if (isSelectedFirstAdmin) {
            view.setStatus("Primeiro administrador selecionado. Não pode ser alterado ou excluído.");
            view.setStatusColor(new Color(200, 50, 50));
        } else {
            view.setStatus("Usuário selecionado: " + view.getSelectedUserName());
            view.setStatusColor(new Color(100, 100, 100));
        }
    }
    private void onPromote(ActionEvent e) {
        int userId = view.getSelectedUserId();
        String userName = view.getSelectedUserName();
        if (userId < 0) {
            view.showError("Selecione um usuário para promover.");
            return;
        }
        if (!isFirstAdmin) {
            view.showError("Apenas o primeiro administrador pode promover usuários.");
            logger.log("PROMOTE_FAIL", "Tentativa de promoção negada para: " + currentAdmin.getNome());
            return;
        }
        if (!view.showConfirm("Deseja promover \"" + userName + "\" a Administrador?")) {
            return;
        }
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                view.showError("Usuário não encontrado.");
                return;
            }
            if ("ADMIN".equals(user.getTipo())) {
                view.showError("Este usuário já é um administrador.");
                return;
            }
            user.setTipo("ADMIN");
            userRepository.update(user);
            logger.log("PROMOTE_SUCCESS", "Usuário promovido a ADMIN: " + user.getNome() +
                    " por: " + currentAdmin.getNome());
            view.showSuccess("Usuário \"" + userName + "\" promovido a Administrador!");
            loadUsers();
            if (onUpdate != null) {
                onUpdate.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao promover usuário: " + ex.getMessage());
            logger.log("PROMOTE_FAIL", "Erro ao promover: " + ex.getMessage());
        }
    }
    private void onDemote(ActionEvent e) {
        int userId = view.getSelectedUserId();
        String userName = view.getSelectedUserName();
        if (userId < 0) {
            view.showError("Selecione um usuário para rebaixar.");
            return;
        }
        if (!isFirstAdmin) {
            view.showError("Apenas o primeiro administrador pode rebaixar usuários.");
            logger.log("DEMOTE_FAIL", "Tentativa de rebaixamento negada para: " + currentAdmin.getNome());
            return;
        }
        if (firstAdmin != null && firstAdmin.getId() == userId) {
            view.showError("O primeiro administrador não pode ser rebaixado.");
            logger.log("DEMOTE_FAIL", "Tentativa de rebaixar primeiro admin");
            return;
        }
        if (userId == currentAdmin.getId()) {
            view.showError("Você não pode rebaixar a si mesmo.");
            logger.log("DEMOTE_FAIL", "Tentativa de auto-rebaixamento");
            return;
        }
        if (!view.showConfirm("Deseja rebaixar \"" + userName + "\" para usuário comum?")) {
            return;
        }
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                view.showError("Usuário não encontrado.");
                return;
            }
            if ("PADRAO".equals(user.getTipo())) {
                view.showError("Este usuário já é um usuário comum.");
                return;
            }
            user.setTipo("PADRAO");
            userRepository.update(user);
            logger.log("DEMOTE_SUCCESS", "Usuário rebaixado a PADRAO: " + user.getNome() +
                    " por: " + currentAdmin.getNome());
            view.showSuccess("Usuário \"" + userName + "\" rebaixado para usuário comum.");
            loadUsers();
            if (onUpdate != null) {
                onUpdate.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao rebaixar usuário: " + ex.getMessage());
            logger.log("DEMOTE_FAIL", "Erro ao rebaixar: " + ex.getMessage());
        }
    }
    private void onDelete(ActionEvent e) {
        int userId = view.getSelectedUserId();
        String userName = view.getSelectedUserName();
        String profile = view.getSelectedUserProfile();
        if (userId < 0) {
            view.showError("Selecione um usuário para excluir.");
            return;
        }
        if (userId == currentAdmin.getId()) {
            view.showError("Você não pode excluir a si mesmo.");
            logger.log("DELETE_FAIL", "Tentativa de auto-exclusão por: " + currentAdmin.getNome());
            return;
        }
        if (firstAdmin != null && firstAdmin.getId() == userId) {
            view.showError("O primeiro administrador não pode ser excluído.");
            logger.log("DELETE_FAIL", "Tentativa de excluir primeiro admin");
            return;
        }
        if (!isFirstAdmin && "ADMIN".equals(profile)) {
            view.showError("Você não tem permissão para excluir outros administradores.");
            logger.log("DELETE_FAIL", "Admin comum tentou excluir outro admin");
            return;
        }
        if (!view.showConfirm("Deseja EXCLUIR permanentemente o usuário \"" + userName + "\"?\n\nEsta ação não pode ser desfeita.")) {
            return;
        }
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                view.showError("Usuário não encontrado.");
                return;
            }
            userRepository.delete(user);
            logger.log("DELETE_SUCCESS", "Usuário excluído: " + user.getNome() +
                    " por: " + currentAdmin.getNome());
            view.showSuccess("Usuário \"" + userName + "\" excluído com sucesso.");
            loadUsers();
            if (onUpdate != null) {
                onUpdate.run();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao excluir usuário: " + ex.getMessage());
            logger.log("DELETE_FAIL", "Erro ao excluir: " + ex.getMessage());
        }
    }
    private void onRefresh(ActionEvent e) {
        loadUsers();
        view.setStatus("Lista atualizada.");
    }
}
