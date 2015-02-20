package com.example.sadarik.gestoractividadeszvergeles;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.ArrayList;

public class FragmentoDetalle extends Fragment {

    private View v;

    private final static String URLBASE = "http://ieszv.x10.bz/restful/api/";
    private Actividad actividad = new Actividad();
    private Profesor profesor = new Profesor();
    private ActividadGrupo acti_grupo = new ActividadGrupo();
    private Grupo grupo = new Grupo();
    private LinearLayout lyComplementaria,lyExtraescolar;
    private ArrayList<ActividadGrupo> grupos = new ArrayList<ActividadGrupo>();
    private String idActividad;
    private TextView tvProfesor, tvDepartamento,tvGrupo,tvDescripcion,tvEFechainicio,tvEHorainicio,
            tvEFechafin,tvELugarinicio,tvELugarfin,tvCHorainicio,tvEHorafin,tvCFecha,tvCLugar,tvCHorafin;


    public FragmentoDetalle(){

    }

    /*****************************************************/
    /*                 METODOS ON                        */
    /*****************************************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fragmento_detalle, container, false);
        return v;
    }

    /*****************************************************/
    /*              METODOS AUXILIARES                  */
    /*****************************************************/

    public void pasarInfo(){
        tvProfesor.setText(profesor.getNombre());
        tvDepartamento.setText(profesor.getDepartamento());
        tvGrupo.setText(grupo.getGrupo());
        tvDescripcion.setText(actividad.getDescripcion());
        if(actividad.getTipo().equals("extraescolar")){
            tvEFechainicio.setText(actividad.getFechaInicio().substring(0,10));
            tvEFechafin.setText(actividad.getFechaFinal().substring(0,10));
            tvELugarinicio.setText(actividad.getLugarSalida());
            tvELugarfin.setText(actividad.getLugarRegreso());
            tvEHorainicio.setText(actividad.getFechaInicio().substring(11,19));
            tvEHorafin.setText(actividad.getFechaFinal().substring(11,19));
        }else if(actividad.getTipo().equals("complementaria")){
            tvCFecha.setText(actividad.getFechaInicio().substring(0,10));
            tvCLugar.setText(actividad.getLugarSalida());
            tvCHorainicio.setText(actividad.getFechaInicio().substring(11,19));
            tvCHorafin.setText(actividad.getFechaFinal().substring(11,19));
        }
    }

    public void inicio(String s){
        initComponents();
        idActividad=s;
        GetActividadID get= new GetActividadID();
        get.execute(URLBASE+"actividad/"+idActividad);
    }

    public void initComponents(){
        tvProfesor = (TextView)v.findViewById(R.id.tvProfesor);
        tvDepartamento = (TextView)v.findViewById(R.id.tvDepartamento);
        tvGrupo = (TextView)v.findViewById(R.id.tvGrupo);
        tvDescripcion = (TextView)v.findViewById(R.id.tvDescripcion);
        lyComplementaria = (LinearLayout)v.findViewById(R.id.layoutComplementaria);
        lyExtraescolar = (LinearLayout)v.findViewById(R.id.layoutExtraescolar);
        tvEFechainicio = (TextView)v.findViewById(R.id.tvEFechai);
        tvEFechafin = (TextView)v.findViewById(R.id.tvEFechaf);
        tvELugarinicio = (TextView)v.findViewById(R.id.tvELugari);
        tvELugarfin = (TextView)v.findViewById(R.id.tvELugarf);
        tvEHorainicio = (TextView)v.findViewById(R.id.tvEHorai);
        tvEHorafin = (TextView)v.findViewById(R.id.tvEHoraf);
        tvCFecha = (TextView)v.findViewById(R.id.tvCFecha);
        tvCLugar = (TextView)v.findViewById(R.id.tvCLugar);
        tvCHorainicio = (TextView)v.findViewById(R.id.tvCHorai);
        tvCHorafin = (TextView)v.findViewById(R.id.tvCHoraf);
    }



    /*****************************************************/
    /*                 CLASES INTERNAS                   */
    /*****************************************************/

    class GetActividadID extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String[] params) {
            String r = ClienteRestFul.get(params[0]);
            return r;
        }
        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            JSONTokener token = new JSONTokener(r);

            try {
                JSONArray array = new JSONArray(token);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    actividad = new Actividad(object);
                }

            } catch (Exception e) {

            }
            if(actividad.getTipo().compareToIgnoreCase("extraescolar")==0){
                lyExtraescolar.setVisibility(View.VISIBLE);
                lyComplementaria.setVisibility(View.INVISIBLE);
            }else if(actividad.getTipo().compareToIgnoreCase("complementaria")==0){
                lyExtraescolar.setVisibility(View.INVISIBLE);
                lyComplementaria.setVisibility(View.VISIBLE);
            }
            GetProfesorID get= new GetProfesorID();
            get.execute(URLBASE+"profesor/"+actividad.getIdProfesor());
        }
    }


    class GetProfesorID extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String[] params) {
            String r = ClienteRestFul.get(params[0]);
            return r;
        }
        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            JSONTokener token = new JSONTokener(r);
            try {
                JSONObject object = new JSONObject(token);
                profesor = new Profesor(object);

            } catch (Exception e) {
                e.printStackTrace();
            }

            GetActividadGrupo get= new GetActividadGrupo();
            get.execute(URLBASE+"actividadgrupo/"+actividad.getId());
        }
    }

    class GetActividadGrupo extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String[] params) {
            String r = ClienteRestFul.get(params[0]);
            return r;
        }
        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            JSONTokener token = new JSONTokener(r);

            try {
                JSONArray array = new JSONArray(token);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    acti_grupo = new ActividadGrupo(object);
                    grupos.add(acti_grupo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            GetGrupoID get= new GetGrupoID();
            get.execute(URLBASE+"grupo/"+grupos.get(0).getIdGrupo());

        }
    }
    class GetGrupoID extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String[] params) {
            String r = ClienteRestFul.get(params[0]);
            return r;
        }
        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            JSONTokener token = new JSONTokener(r);

            try {
                JSONObject object = new JSONObject(token);
                grupo = new Grupo(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pasarInfo();
        }
    }
}
