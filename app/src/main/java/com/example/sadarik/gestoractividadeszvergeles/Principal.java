package com.example.sadarik.gestoractividadeszvergeles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

public class Principal extends Activity {

    private final static String URLBASE = "http://ieszv.x10.bz/restful/api/";
    private ArrayList<Actividad> datos = new ArrayList<Actividad>();
    private Adaptador ad;
    private final int ACTIVIDAD_DETALLE = 1;
    private Calendar factual;
    private ListView lv;
    private String fecha,hora;
    private LinearLayout lDetalle;
    private Button btCFecha,btCHorai,btCHoraf,btEFechai,btEFechaf,btEHorai,btEHoraf;
    private EditText etDescripcion,etCLugar,etELugarS,etELugarR,etDepartamento;
    private Spinner spProfesor, spGrupo;
    private DatePickerDialog dpFecha;
    private TimePickerDialog tpHora;
    private String tipo="complementaria";
    private LinearLayout lyComplementaria,lyExtrescolar;
    private ArrayList<Profesor> profesores = new ArrayList<Profesor>();
    private ArrayList<Grupo> grupos = new ArrayList<Grupo>();
    private String idGrupo,idProfesor,idActividad,idActividadGrupo,idActividadProfesor;


    /*****************************************************/
    /*                 METODOS ON                        */
    /*****************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        factual = new GregorianCalendar();
        cargarActividades();
    }

    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (id == R.id.action_borrar) {
            borrar(index);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.agregar) {
            profesores.clear();
            grupos.clear();
            agregar();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*****************************************************/
    /*                 CLASES INTERNAS                   */
    /*****************************************************/

    class GetActividades extends AsyncTask<String,Void,String> {

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
                    Actividad a = new Actividad(object);
                    ad.notifyDataSetChanged();
                    datos.add(a);
                }
            } catch (Exception e) {
            }
        }
    }

    class GetProfesores extends AsyncTask<String,Void,String> {

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
                    Profesor p = new Profesor(object);
                    profesores.add(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayAdapter<Profesor> dataAdapter = new ArrayAdapter<Profesor>(Principal.this, android.R.layout.simple_spinner_item, profesores);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProfesor.setAdapter(dataAdapter);

            spProfesor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    idProfesor = ((Profesor) parent.getItemAtPosition(pos)).getId();
                    etDepartamento.setText(((Profesor) parent.getItemAtPosition(pos)).getDepartamento());
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    class GetGrupos extends AsyncTask<String,Void,String> {

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
                    Grupo g = new Grupo(object);
                    grupos.add(g);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ArrayAdapter<Grupo> dataAdapter = new ArrayAdapter<Grupo>(Principal.this, android.R.layout.simple_spinner_item, grupos);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spGrupo.setAdapter(dataAdapter);

            spGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    idGrupo = ((Grupo) parent.getItemAtPosition(pos)).getId();
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    class PostActividad extends AsyncTask<ParametrosPost,Void,String> {
        @Override
        protected String doInBackground(ParametrosPost[] params) {
            idActividad=ClienteRestFul.post(params[0].url,params[0].jsonObject);
            return idActividad;
        }
        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            idActividad=idActividad.substring(5,idActividad.length()-1);
            if (idActividad.equals("0")){
                Toast.makeText(Principal.this,getString(R.string.Error),Toast.LENGTH_SHORT).show();
            }else {
                ActividadGrupo ag = new ActividadGrupo(idActividad,idGrupo);
                PostActividadGrupo postag = new PostActividadGrupo();
                ParametrosPost pp = new ParametrosPost();
                pp.url=URLBASE+"actividadgrupo";
                pp.jsonObject=ag.getJSON();
                postag.execute(pp);
            }
        }
    }

    class PostActividadGrupo extends AsyncTask<ParametrosPost,Void,String> {
        @Override
        protected String doInBackground(ParametrosPost[] params) {
            idActividadGrupo=ClienteRestFul.post(params[0].url,params[0].jsonObject);
            return idActividadGrupo;
        }
        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            if (idActividad.equals("0")){
                Toast.makeText(Principal.this,getString(R.string.Error),Toast.LENGTH_SHORT).show();
            }else {
                ActividadProfesor ap = new ActividadProfesor(idActividad,""+idProfesor);
                PostActividadProfesor postap = new PostActividadProfesor();
                ParametrosPost pp = new ParametrosPost();
                pp.url=URLBASE+"actividadprofesor";
                pp.jsonObject=ap.getJSON();
                postap.execute(pp);
            }
        }
    }

    class PostActividadProfesor extends AsyncTask<ParametrosPost,Void,String> {
        @Override
        protected String doInBackground(ParametrosPost[] params) {
            idActividadProfesor=ClienteRestFul.post(params[0].url,params[0].jsonObject);
            return idActividadProfesor;
        }
        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            if (idActividadProfesor.equals("0")){
                Toast.makeText(Principal.this,getString(R.string.Error),Toast.LENGTH_SHORT).show();
            }else {
                cargarActividades();
            }

        }
    }

    /*****************************************************/
    /*                 FECHAS Y HORAS                    */
    /*****************************************************/

    /* Complementarias */

    public class SacarFechaC implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year,int monthOfYear,int dayOfMonth) {

            fecha=year+"-"+(monthOfYear+1)+"-"+dayOfMonth+"";
            btCFecha.setText(fecha);
        }
    }

    public void cFecha(View v){
        dpFecha = new DatePickerDialog(this,new SacarFechaC(), factual.get(Calendar.YEAR), factual.get(Calendar.MONTH), factual.get(Calendar.DAY_OF_MONTH));
        dpFecha.show();
    }

    public class SacarHoraIC implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker tp, int hour, int minute){
            hora=hour+":"+minute;
            btCHorai.setText(hora);
        }
    }

    public void cHoraI(View v){
        tpHora = new TimePickerDialog(this,new SacarHoraIC(), factual.get(Calendar.HOUR_OF_DAY), factual.get(Calendar.MINUTE), true);
        tpHora.show();

    }

    public class SacarhoraFC implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker tp, int hour, int minute){
            hora=hour+":"+minute;
            btCHoraf.setText(hora);
        }
    }

    public void cHoraF(View v){
        tpHora = new TimePickerDialog(this,new SacarhoraFC(), factual.get(Calendar.HOUR_OF_DAY), factual.get(Calendar.MINUTE), true);
        tpHora.show();
    }

    /* Extraexcolares */

    public class SacarFechaIE implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year,int monthOfYear,int dayOfMonth) {

            fecha=year+"-"+(monthOfYear+1)+"-"+dayOfMonth+"";
            btEFechai.setText(fecha);
        }
    }

    public void eFechaI(View v){
        dpFecha = new DatePickerDialog(this,new SacarFechaIE(), factual.get(Calendar.YEAR), factual.get(Calendar.MONTH), factual.get(Calendar.DAY_OF_MONTH));
        dpFecha.show();
    }

    public class SacarFechaFE implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year,int monthOfYear,int dayOfMonth) {

            fecha=year+"-"+(monthOfYear+1)+"-"+dayOfMonth+"";
            btEFechaf.setText(fecha);
        }
    }

    public void eFechaF(View v){
        dpFecha = new DatePickerDialog(this,new SacarFechaFE(), factual.get(Calendar.YEAR), factual.get(Calendar.MONTH), factual.get(Calendar.DAY_OF_MONTH));
        dpFecha.show();
    }

    public class SacarHoraFE implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker tp, int hour, int minute){
            hora=hour+":"+minute;
            btEHoraf.setText(hora);
        }
    }

    public void eHoraF(View v){
        tpHora = new TimePickerDialog(this,new SacarHoraFE(), factual.get(Calendar.HOUR_OF_DAY), factual.get(Calendar.MINUTE), true);
        tpHora.show();
    }

    public class SacarHoraIE implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker tp, int hour, int minute){
            hora=hour+":"+minute;
            btEHorai.setText(hora);
        }
    }

    public void eHoraI(View v){
        tpHora = new TimePickerDialog(this,new SacarHoraIE(), factual.get(Calendar.HOUR_OF_DAY), factual.get(Calendar.MINUTE), true);
        tpHora.show();
    }

    /*****************************************************/
    /*              METODOS AUXILIARES                  */
    /*****************************************************/

    private boolean agregar() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.btAgregar));
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.anadir, null);
        alert.setView(vista);
        initComponets(vista);
        GetProfesores getP= new GetProfesores();
        getP.execute(URLBASE+"profesor");
        GetGrupos getG= new GetGrupos();
        getG.execute(URLBASE+"grupo");
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                Actividad a = new Actividad();
                if(tipo.equals("complementaria")){
                    if(idProfesor.toString().equals("") || btCFecha.getText().toString().equals("") ||
                            btCHorai.getText().toString().equals("") || tipo.toString().equals("") ||
                            btCHoraf.getText().toString().equals("") ||  etDescripcion.getText().toString().equals("") ||
                            etCLugar.getText().toString().equals("")){

                        Toast.makeText(Principal.this, getString(R.string.Vacios), Toast.LENGTH_SHORT).show();

                    }else{
                        String fechaiC = btCFecha.getText()+" "+ btCHorai.getText()+":00";
                        String fechafC = "0000-00-00 "+ btCHoraf.getText()+":00";
                        a = new Actividad(idProfesor,tipo,fechaiC,fechafC,etCLugar.getText().toString(),
                                "",etDescripcion.getText().toString(),"jonathan");
                        PostActividad postAc = new PostActividad();
                        ParametrosPost pp = new ParametrosPost();
                        pp.url=URLBASE+"actividad";
                        pp.jsonObject=a.getJSON();
                        postAc.execute(pp);
                        Toast.makeText(Principal.this,getString(R.string.Guardado),Toast.LENGTH_SHORT).show();
                    }
                }else if (tipo.equals("extraescolar")){
                    if( idProfesor.toString().equals("") || tipo.toString().trim().equals("") ||
                            etDescripcion.toString().equals("") ||
                            btEFechai.getText().toString().equals("") ||
                            btEFechaf.getText().toString().equals("") ||
                            btEHorai.getText().toString().equals("") ||
                            btEHoraf.getText().toString().equals("") ||
                            etELugarS.toString().equals("") ||
                            etELugarR.toString().equals("")
                           ) {
                        Toast.makeText(Principal.this,getString(R.string.Vacios),Toast.LENGTH_SHORT).show();
                    }else{
                        String fechaiE = btEFechai.getText()+" "+btEHorai.getText()+":00";
                        String fechafE = btEFechaf.getText()+" "+btEHoraf.getText()+":00";
                        a = new Actividad(idProfesor,tipo,fechaiE,fechafE,etELugarS.getText().toString(),
                                etELugarR.getText().toString(),etDescripcion.getText().toString(),"jonathan");
                        PostActividad postAc = new PostActividad();
                        ParametrosPost pp = new ParametrosPost();
                        pp.url=URLBASE+"actividad";
                        pp.jsonObject=a.getJSON();
                        postAc.execute(pp);
                        Toast.makeText(Principal.this,getString(R.string.Guardado),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }

    public void cargarActividades(){
        lv = (ListView) findViewById(R.id.lvActividades);
        datos.clear();
        ad = new Adaptador(this, R.layout.detalle, datos);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
        final FragmentoDetalle fDetalle = (FragmentoDetalle)getFragmentManager().findFragmentById(R.id.fragmentDetalle2);
        final boolean horizontal = fDetalle!=null && fDetalle.isInLayout();

        GetActividades get= new GetActividades();
        get.execute(URLBASE+"actividad/jonathan");
        if(horizontal){
            lDetalle = (LinearLayout)findViewById(R.id.layoutdetalle);
            lDetalle.setVisibility(View.INVISIBLE);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Actividad act = (Actividad)lv.getItemAtPosition(position);
                view.setSelected(true);
                if(horizontal){
                    lDetalle.setVisibility(View.VISIBLE);
                    String idActividad=act.getId();
                    fDetalle.inicio(idActividad);
                }else{
                    Intent i = new Intent(Principal.this,Detalle.class);
                    i.putExtra("id",act.getId());
                    startActivityForResult(i, ACTIVIDAD_DETALLE);
                }
            }
        });
        ad.notifyDataSetChanged();
    }

    private boolean borrar(final int pos) {
        final String id;
        id = datos.get(pos).getId();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.ConfirmacionBorrar));
        LayoutInflater inflater = LayoutInflater.from(this);
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                BorrarActividad borrarA = new BorrarActividad();
                borrarA.execute(URLBASE+"actividad/"+id, URLBASE+"actividadprofesor/"+id, URLBASE+"actividadgrupo/"+id );
                cargarActividades();
            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }

    public void initComponets(View vista){
        lyComplementaria = (LinearLayout)vista.findViewById(R.id.layoutComplementaria);
        lyExtrescolar = (LinearLayout)vista.findViewById(R.id.layoutExtraescolar);
        etDescripcion = (EditText)vista.findViewById(R.id.etDescripcion);
        etDepartamento = (EditText)vista.findViewById(R.id.etDepartamento);
        spProfesor=(Spinner)vista.findViewById(R.id.spProfesor);
        spGrupo = (Spinner)vista.findViewById(R.id.spGrupo);
        /*Complementaria*/
        btCFecha=(Button)vista.findViewById(R.id.btCFecha);
        btCHorai=(Button)vista.findViewById(R.id.btCHorai);
        btCHoraf=(Button)vista.findViewById(R.id.btCHoraf);
        etCLugar = (EditText)vista.findViewById(R.id.etLugarC);
        /*Extraescolar*/
        btEFechai=(Button)vista.findViewById(R.id.btEFechai);
        btEFechaf=(Button)vista.findViewById(R.id.btEFechaf);
        btEHorai=(Button)vista.findViewById(R.id.btEHorai);
        btEHoraf=(Button)vista.findViewById(R.id.btEHoraf);
        etELugarS = (EditText)vista.findViewById(R.id.etELugarS);
        etELugarR = (EditText)vista.findViewById(R.id.etELugarR);
    }

    public void onRadioButtonClickedAnadir(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rbComplementaria:
                if (checked)
                btCFecha.setText("");
                btCHorai.setText("");
                btCHoraf.setText("");
                etCLugar.setText("");
                lyExtrescolar.setVisibility(View.INVISIBLE);
                lyComplementaria.setVisibility(View.VISIBLE);
                tipo="complementaria";
                break;
            case R.id.rbExtraescolar:
                if (checked)
                btEFechai.setText("");
                btEFechaf.setText("");
                btEHorai.setText("");
                btEHoraf.setText("");
                etELugarR.setText("");
                etELugarS.setText("");
                lyExtrescolar.setVisibility(View.VISIBLE);
                lyComplementaria.setVisibility(View.INVISIBLE);
                tipo="extraescolar";
                break;
        }
    }
}
