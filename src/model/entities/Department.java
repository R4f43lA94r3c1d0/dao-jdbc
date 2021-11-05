package model.entities;

import java.io.Serializable;
import java.util.Objects;

/** Gerar um Serializable para que os objetos possam ser transformados em sequências de bytes
 * (Importante caso o objeto seja gravado em arquivo ou trafegado em rede) **/

public class Department implements Serializable
{
    /** Número da versão **/
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;

    /** Standard constructor **/
    public Department(){}

    /** Custom constructor **/
    public Department(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Gerar um hashcode e equals para que os objetos sejam comparados pelo conteúdo, e não referência de ponteiro **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /** Gerar um toString para mais facilidade no momento de impressão dos objetos **/
    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
