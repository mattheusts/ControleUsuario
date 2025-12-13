package com.ufes.controleusuario.infra;

import com.ufes.controleusuario.service.IPasswordValidator;

public class MockPasswordValidator implements IPasswordValidator {
  // Example: Min 5 chars.
  @Override
  public boolean validate(String password) {
    return password != null && password.length() >= 5;
  }
}
