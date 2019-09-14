package ba.unsa.etf.rma.ajla.projekatrma;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KategorijeAkt extends AppCompatActivity {


    public static Boolean siriL=false;
    //public static  ArrayList<Autor> Autori = new ArrayList<Autor>();       //ovdje cuvamo sve autore
    //public static ArrayList<Knjiga> Knjige=new ArrayList<Knjiga>();
    public static FragmentManager fm;
    public static DodavanjeKnjigeFragment dodavanjeKnjigeFragment= new DodavanjeKnjigeFragment();
    public static ListeFragment listeFragment ; //= new ListeFragment();
    public static KnjigeFragment knjigeFragment = new KnjigeFragment();
    public static FragmentOnline fragmentOnline = new FragmentOnline();
    public static FragmentPreporuci fragmentPreporuci = new FragmentPreporuci();
    //public static ArrayList<String> nizStringova;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Stetho.initializeWithDefaults(this);

        setContentView(R.layout.activity_kategorije_akt);
        siriL=false;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        fm=getFragmentManager();

        FrameLayout knjigeFrag=(FrameLayout)findViewById(R.id.dLayout2);
        if(knjigeFrag!=null) {
            siriL=true;

        }
        listeFragment = (ListeFragment) fm.findFragmentById(R.id.dLayout);

        if(listeFragment==null) {
            listeFragment=new ListeFragment();
            fm.beginTransaction().replace(R.id.dLayout,listeFragment).commit();
        }
        else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        //ReplaceFragment();
    }


   /* public void ReplaceFragment() {
        fm=getFragmentManager();

        Bundle argumenti=new Bundle();
        argumenti.putParcelableArrayList("ListaK", KategorijeAkt.Knjige); //niz knjiga prosljedjujemo u fragment
        listeFragment.setArguments(argumenti);

        fm.beginTransaction().replace(R.id.dLayout, listeFragment).commit();
    }*/

}
