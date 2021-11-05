package model.dao;

import model.entities.Seller;

import java.util.List;

public interface SellerDao
{
    /** Operação responsável por inserir no banco de dados o objeto */
    void insert(Seller obj);

    /** Operação responsável por atualizar o objeto no banco de dados */
    void update(Seller obj);

    /** Operação responsável por deletar um objeto no banco de dados através do Id */
    void deleteById(Integer id);

    /** Operação responsável por consultar um objeto no banco de dados através do Id */
    Seller findById(Integer id); //Caso não exista, irá retornar nulo.

    /** Operação responsável por retornar uma lista com todos os departamentos do banco de dados */
    List<Seller> findAll();
}
