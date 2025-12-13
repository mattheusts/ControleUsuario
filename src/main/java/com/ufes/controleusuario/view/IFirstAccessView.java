package com.ufes.controleusuario.view;

import java.awt.event.ActionListener;

public interface IFirstAccessView {
  String getNome();

  String getUsuario();

  String getSenha();

  void setCadastrarListener(ActionListener listener);

  void showMessage(String message);

  void close();

  void setVisible(boolean visible);
}
