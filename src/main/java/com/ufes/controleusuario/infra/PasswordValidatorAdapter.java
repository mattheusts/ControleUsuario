package com.ufes.controleusuario.infra;

import com.ufes.controleusuario.service.IPasswordValidator;
import com.pss.senha.validacao.ValidadorSenha;
import java.util.List;

import java.util.Collections;

public class PasswordValidatorAdapter implements IPasswordValidator {
  @Override
  public List<String> validate(String password) {
    if (password == null) {
      return Collections.singletonList("Senha não pode ser nula");
    }
    ValidadorSenha validador = new ValidadorSenha();
    try {
      return validador.validar(password);
    } catch (Exception e) {
      return Collections.singletonList("Erro na validação: " + e.getMessage());
    }
  }
}
