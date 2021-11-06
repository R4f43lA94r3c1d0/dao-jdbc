package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao
{

    private Connection conn;

    public SellerDaoJDBC (Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj)
    {
        PreparedStatement st = null;

        try
        {
            st = conn.prepareStatement(
                    "INSERT INTO seller "
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                    + "VALUES "
                     /** As interrogações são os Placeholders,
                      * onde iremos setar manualmente os valores **/
                    + "(?, ?, ?, ?, ?)",

                    /** Esse comando retorna o id do novo vendedor inserido **/
                    Statement.RETURN_GENERATED_KEYS
            );

            /** Setar a primeira interrogação, referente a nome  **/
            st.setString(1, obj.getName());

            /** Setar a segunda interrogação, referente a email  **/
            st.setString(2, obj.getEmail());

            /** Setar a terceira interrogação, referente a data de aniversário  **/
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));

            /** Setar a quarta interrogação, referente a salário  **/
            st.setDouble(4, obj.getBaseSalary());

            /** Setar a quinta interrogação, referente a departamento  **/
            st.setInt(5, obj.getDepartment().getId());

            /** Comando para executar o código SQL **/
            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0)
            {
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next())
                {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                /**
                 * Um detalhe importante: Como os recursos de Statement e ResultSet são externos,
                 * ou seja, não são controlados pela JVM do Java, é interessante realizar o fechamento desses recursos
                 * manualmente, afim de evitar que nosso programa tenha algum tipo de vazamento de memória.
                 */
                DB.closeResultSet(rs);
            } else
            {
                throw new DbException("Unexpected error! No rows affected!");
            }

        }catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }
        finally
        {
            /**
             * Um detalhe importante: Como os recursos de Statement e ResultSet são externos,
             * ou seja, não são controlados pela JVM do Java, é interessante realizar o fechamento desses recursos
             * manualmente, afim de evitar que nosso programa tenha algum tipo de vazamento de memória.
             */
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj)
    {
        PreparedStatement st = null;

        try
        {
            st = conn.prepareStatement(
                    "UPDATE seller "
                    + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                    + "WHERE Id = ? ");

            /** Setar a primeira interrogação, referente a nome  **/
            st.setString(1, obj.getName());

            /** Setar a segunda interrogação, referente a email  **/
            st.setString(2, obj.getEmail());

            /** Setar a terceira interrogação, referente a data de aniversário  **/
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));

            /** Setar a quarta interrogação, referente a salário  **/
            st.setDouble(4, obj.getBaseSalary());

            /** Setar a quinta interrogação, referente a departamento  **/
            st.setInt(5, obj.getDepartment().getId());

            /** Setar a sexta interrogação, referente ao id do vendedor  **/
            st.setInt(6, obj.getId());

            /** Comando para executar o código SQL **/
            st.executeUpdate();

        }catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }
        finally
        {
            /**
             * Um detalhe importante: Como os recursos de Statement e ResultSet são externos,
             * ou seja, não são controlados pela JVM do Java, é interessante realizar o fechamento desses recursos
             * manualmente, afim de evitar que nosso programa tenha algum tipo de vazamento de memória.
             */
            DB.closeStatement(st);
        }

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id)
    {
        PreparedStatement st = null;
        ResultSet rs = null;

        try
        {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery(); //Executar comendo SQL

            if(rs.next())
            {
                Department dep = instantiateDepartment(rs);

                Seller obj = instantiateSeller(rs, dep);

                return obj;
            }
            return null;

        } catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }
        finally
        {
            /**
             * Um detalhe importante: Como os recursos de Statement e ResultSet são externos,
             * ou seja, não são controlados pela JVM do Java, é interessante realizar o fechamento desses recursos
             * manualmente, afim de evitar que nosso programa tenha algum tipo de vazamento de memória.
             */

            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException
    {
        /** Aqui, o método reclama do get e set, pois pode
         * ocasionar uma excessão. Porém, nós já estamos tratando
         * essa excessão na chamada desse método, então iremos
         * propagar essa excessão com throws SQLException
         * na assinatura do método
         * **/

        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(dep);

        return obj;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException
    {
        Department dep = new Department();

        /** Aqui, o método reclama do get e set, pois pode
         * ocasionar uma excessão. Porém, nós já estamos tratando
         * essa excessão na chamada desse método, então iremos
         * propagar essa excessão com throws SQLException
         * na assinatura do método
         * **/
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));

        return dep;
    }

    @Override
    public List<Seller> findAll()
    {
        PreparedStatement st = null;
        ResultSet rs = null;

        try
        {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name");

            rs = st.executeQuery(); //Executar comendo SQL

            List<Seller> list = new ArrayList<>();

            /** Criando estrutura Map VAZIA para controlar a não repetição do departamento dentro do while.
             * Dentro desse Map, será guardado qualquer departamento que será instanciado.
             * Então, dentro do bloco while, ele irá testar se o departamento já existe **/
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next())
            {
                /** Aqui ele irá verificar se já existe o departamento. Ele faz isso
                 * tentando buscar através do método get (map.get) um departamento
                 * que já tenha esse id (consultando a coluna do banco de dados cujo nome
                 * é "DepartmentId"). Caso não exista, o método get retornará nulo,
                 * e se for nulo, aí sim irá instanciar o departamento **/

                Department dep = map.get(rs.getInt("DepartmentId"));

                if(dep == null)
                {
                    /** Como dito acima: Caso seja nulo, instancia um novo departamento **/
                    dep = instantiateDepartment(rs);

                    /** Agora, iremos salvar esse departamento dentro do Map para que
                     * na próxima vez, verificar ou não se já existe **/
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);

                list.add(obj);
            }
            return list;

        } catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }
        finally
        {
            /**
             * Um detalhe importante: Como os recursos de Statement e ResultSet são externos,
             * ou seja, não são controlados pela JVM do Java, é interessante realizar o fechamento desses recursos
             * manualmente, afim de evitar que nosso programa tenha algum tipo de vazamento de memória.
             */

            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try
        {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = ? "
                    + "ORDER BY Name");

            st.setInt(1, department.getId());
            rs = st.executeQuery(); //Executar comendo SQL

            List<Seller> list = new ArrayList<>();

            /** Criando estrutura Map VAZIA para controlar a não repetição do departamento dentro do while.
             * Dentro desse Map, será guardado qualquer departamento que será instanciado.
             * Então, dentro do bloco while, ele irá testar se o departamento já existe **/
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next())
            {
                /** Aqui ele irá verificar se já existe o departamento. Ele faz isso
                 * tentando buscar através do método get (map.get) um departamento
                 * que já tenha esse id (consultando a coluna do banco de dados cujo nome
                 * é "DepartmentId"). Caso não exista, o método get retornará nulo,
                 * e se for nulo, aí sim irá instanciar o departamento **/

                Department dep = map.get(rs.getInt("DepartmentId"));

                if(dep == null)
                {
                    /** Como dito acima: Caso seja nulo, instancia um novo departamento **/
                    dep = instantiateDepartment(rs);

                    /** Agora, iremos salvar esse departamento dentro do Map para que
                     * na próxima vez, verificar ou não se já existe **/
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);

                list.add(obj);
            }
            return list;

        } catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }
        finally
        {
            /**
             * Um detalhe importante: Como os recursos de Statement e ResultSet são externos,
             * ou seja, não são controlados pela JVM do Java, é interessante realizar o fechamento desses recursos
             * manualmente, afim de evitar que nosso programa tenha algum tipo de vazamento de memória.
             */

            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
