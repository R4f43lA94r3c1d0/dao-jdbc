package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args)
	{

		/** Dessa forma, realizamos uma injeção de dependência sem expor a implementação **/
		SellerDao sellerDao = DaoFactory.createSellerDao();

		Seller seller = sellerDao.findById(3);

		System.out.println(seller);

	}
}
