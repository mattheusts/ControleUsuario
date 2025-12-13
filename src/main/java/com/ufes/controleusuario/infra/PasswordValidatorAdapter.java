package com.ufes.controleusuario.infra;
import com.ufes.controleusuario.service.IPasswordValidator;
public class PasswordValidatorAdapter implements IPasswordValidator {
  @Override
  public boolean validate(String password) {
    return password != null && password.length() >= 5;  
  }
}
