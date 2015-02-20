package com.example.sadarik.gestoractividadeszvergeles;

import android.os.AsyncTask;

public class BorrarActividad  extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String[] params) {
        String r = ClienteRestFul.delete(params[0]);
        return r;
    }
    @Override
    protected void onPostExecute(String r) {
        super.onPostExecute(r);
    }
}
