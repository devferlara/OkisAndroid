package com.i24web.okisandroid.Modelos;

/**
 * Created by diegofernandolaramontealegre on 2/01/18.
 */

public class Modelo_categorias_inicio {

    private int Imagen;
    private String Categoria;
    private Boolean isSelected;

    public Modelo_categorias_inicio(int Imagen, String Categoria) {
        this.Imagen = Imagen;
        this.Categoria = Categoria;
        this.isSelected = false;
    }

    public int getImagen() { return Imagen; }

    public String getCategoria() {
        return Categoria;
    }

    public Boolean getSelected() { return isSelected; }
    public void setIsSelected(Boolean newStatus) {
        isSelected = newStatus;
    }


}
