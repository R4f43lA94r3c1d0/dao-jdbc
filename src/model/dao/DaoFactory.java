package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory
{
    /** Essa classe terá operações estáticas para instanciar os DAO's **/

    public static SellerDao createSellerDao()
    {
        /** Desse modo, não é exposto a implementação, somente o tipo da interface.
         * Esse é um macete para não expor a implementação, somente a interface **/
        return new SellerDaoJDBC();
    }
}
