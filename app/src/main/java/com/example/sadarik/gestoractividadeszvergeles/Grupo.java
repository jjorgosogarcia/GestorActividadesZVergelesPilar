package com.example.sadarik.gestoractividadeszvergeles;

import org.json.JSONException;
import org.json.JSONObject;

public class Grupo {

    private String id, grupo;

    public Grupo() {
    }

    public Grupo(String id, String grupo) {
        this.id = id;
        this.grupo = grupo;
    }

    public Grupo(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.grupo = object.getString("grupo");
        } catch (JSONException e) {

        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String toString() {
        return this.getGrupo()+"";
    }

}
