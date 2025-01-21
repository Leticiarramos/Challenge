package br.com.literalura.service;

public interface iConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
