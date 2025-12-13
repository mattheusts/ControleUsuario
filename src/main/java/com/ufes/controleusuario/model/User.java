package com.ufes.controleusuario.model;
import java.time.LocalDate;
public class User {
  private int id;
  private String nome;
  private String usuario;
  private String senha;
  private String tipo;  
  private String status;  
  private LocalDate dataCadastro;
  public User(int id, String nome, String usuario, String senha, String tipo, String status, LocalDate dataCadastro) {
    this.id = id;
    this.nome = nome;
    this.usuario = usuario;
    this.senha = senha;
    this.tipo = tipo;
    this.status = status;
    this.dataCadastro = dataCadastro;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }
  public String getUsuario() {
    return usuario;
  }
  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }
  public String getSenha() {
    return senha;
  }
  public void setSenha(String senha) {
    this.senha = senha;
  }
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public LocalDate getDataCadastro() {
    return dataCadastro;
  }
  public void setDataCadastro(LocalDate dataCadastro) {
    this.dataCadastro = dataCadastro;
  }
  public boolean isAdmin() {
    return "ADMIN".equalsIgnoreCase(tipo);
  }
  public boolean isAuthorized() {
    return "AUTORIZADO".equalsIgnoreCase(status);
  }
}
