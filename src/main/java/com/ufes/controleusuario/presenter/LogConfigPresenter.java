package com.ufes.controleusuario.presenter;
import com.ufes.controleusuario.infra.ExternalLoggerAdapter;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.LogConfigView;
import java.awt.event.ActionEvent;
public class LogConfigPresenter {
    private LogConfigView view;
    private ILoggerService logger;
    private User currentAdmin;
    public LogConfigPresenter(LogConfigView view,
                              ILoggerService logger,
                              User currentAdmin) {
        this.view = view;
        this.logger = logger;
        this.currentAdmin = currentAdmin;
        initListeners();
        loadCurrentConfig();
    }
    private void initListeners() {
        view.setSaveListener(this::onSave);
        view.setCancelListener(this::onCancel);
    }
    private void loadCurrentConfig() {
        if (logger instanceof ExternalLoggerAdapter) {
            String currentFormat = ((ExternalLoggerAdapter) logger).getFormat();
            view.setCurrentFormat(currentFormat);
        } else {
            view.setCurrentFormat("CSV");
        }
    }
    private void onSave(ActionEvent e) {
        String selectedFormat = view.getSelectedFormat();
        if (selectedFormat == null) {
            view.showError("Selecione um formato de log.");
            return;
        }
        try {
            if (logger instanceof ExternalLoggerAdapter) {
                ((ExternalLoggerAdapter) logger).setFormat(selectedFormat);
            }
            logger.log("LOG_CONFIG_CHANGE", "Formato de log alterado para: " + selectedFormat +
                    " por: " + currentAdmin.getNome());
            view.showSuccess("Configuração salva com sucesso!\n\nNovos logs serão gravados no formato " + selectedFormat + ".");
            view.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao salvar configuração: " + ex.getMessage());
            logger.log("LOG_CONFIG_FAIL", "Erro ao alterar formato de log: " + ex.getMessage());
        }
    }
    private void onCancel(ActionEvent e) {
        view.close();
    }
}
