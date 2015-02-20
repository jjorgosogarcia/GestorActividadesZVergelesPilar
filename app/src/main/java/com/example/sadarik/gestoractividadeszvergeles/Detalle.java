package com.example.sadarik.gestoractividadeszvergeles;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;


public class Detalle extends Activity {

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


    /*****************************************************/
    /*                 METODOS ON                        */
    /*****************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_ampliado);
        initComponents();
        consultarActividad();
    }

    /*****************************************************/
    /*              METODOS AUXILIARES                  */
    /*****************************************************/


    public void initComponents(){
        tvProfesor = (TextView)findViewById(R.id.tvProfesor);
        tvDepartamento = (TextView)findViewById(R.id.tvDepartamento);
        tvGrupo= (TextView)findViewById(R.id.tvGrupo);
        tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);
        lyComplementaria = (LinearLayout)findViewById(R.id.layoutComplementaria);
        lyExtraescolar = (LinearLayout)findViewById(R.id.layoutExtraescolar);
        tvEFechainicio = (TextView)findViewById(R.id.tvEFechai);
        tvEFechafin = (TextView)findViewById(R.id.tvEFechaf);
        tvELugarinicio = (TextView)findViewById(R.id.tvELugari);
        tvELugarfin = (TextView)findViewById(R.id.tvELugarf);
        tvEHorainicio = (TextView)findViewById(R.id.tvEHorai);
        tvEHorafin = (TextView)findViewById(R.id.tvEHoraf);
        tvCFecha = (TextView)findViewById(R.id.tvCFecha);
        tvCLugar = (TextView)findViewById(R.id.tvCLugar);
        tvCHorainicio = (TextView)findViewById(R.id.tvCHorai);
        tvCHorafin = (TextView)findViewById(R.id.tvCHoraf);
    }

    public void consultarActividad(){
        idActividad = getIntent().getExtras().getString("id");
        GetActividadID get= new GetActividadID();
        get.execute(URLBASE+"actividad/"+idActividad);

    }

    public void pasarInfo(){
        tvProfesor.setText(profesor.getNombre());
        tvDepartamento.setText(profesor.getDepartamento());
        tvGrupo.setText(grupo.getGrupo());
        tvDescripcion.setText(actividad.getDescripcion());
        if(actividad.getTipo().compareToIgnoreCase("extraescolar")==0){
            tvEFechainicio.setText(actividad.getFechaInicio().substring(0,10));
            tvEFechafin.setText(actividad.getFechaFinal().substring(0,10));
            tvELugarinicio.setText(actividad.getLugarSalida());
            tvELugarfin.setText(actividad.getLugarRegreso());
            tvEHorainicio.setText(actividad.getFechaInicio().substring(11,19));
            tvEHorafin.setText(actividad.getFechaFinal().substring(11,19));
        }else if(actividad.getTipo().compareToIgnoreCase("complementaria")==0){
            tvCFecha.setText(actividad.getFechaInicio().substring(0,10));
            tvCLugar.setText(actividad.getLugarSalida());
            tvCHorainicio.setText(actividad.getFechaInicio().substring(11,19));
            tvCHorafin.setText(actividad.getFechaFinal().substring(11,19));
        }
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
            if(actividad.getTipo().equals("extraescolar")){
                lyExtraescolar.setVisibility(View.VISIBLE);
                lyComplementaria.setVisibility(View.INVISIBLE);
            }else if(actividad.getTipo().equals("complementaria")){
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
