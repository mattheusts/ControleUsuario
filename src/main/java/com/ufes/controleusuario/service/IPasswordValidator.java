package com.ufes.controleusuario.service;

import java.util.List;

public interface IPasswordValidator {
  List<String> validate(String password);
}
