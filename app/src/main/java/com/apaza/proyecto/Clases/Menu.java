package com.apaza.proyecto.Clases;

import java.util.Date;

/**
 * Created by Julio Cesar on 2/12/2017.
 */

public class Menu {
    private Integer id;
    private Integer usuarios_id;
    private String comida;
    private String tipo;
    private String costo;
    private Date fecha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarios_id() {
        return usuarios_id;
    }

    public void setUsuarios_id(Integer usuarios_id) {
        this.usuarios_id = usuarios_id;
    }

    public String getComida() {
        return comida;
    }

    public void setComida(String comida) {
        this.comida = comida;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", usuario_id=" + usuarios_id +
                ", tipo='" + tipo + '\'' +
                ", costo=" + costo +
                ", fecha=" + fecha +
                '}';
    }
}
