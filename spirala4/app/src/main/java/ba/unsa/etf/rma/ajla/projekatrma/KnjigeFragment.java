package ba.unsa.etf.rma.ajla.projekatrma;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 7.4.2018.
 */

public class KnjigeFragment extends Fragment {

    public ArrayList<Knjiga> listaKnjigaOdabranogZanra = new ArrayList<>(); //Lista u koju smiještamo sve knjige zanra koji smo odabrali

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.knjige_fragment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        if (getArguments().containsKey("ListaK")) {
            listaKnjigaOdabranogZanra = getArguments().getParcelableArrayList("ListaK");
            //Dohvaćamo referencu na ListView u fragmentu
            ListView lista = (ListView) getView().findViewById(R.id.listaKategorija);
        }
        super.onActivityCreated(savedInstanceState);

        final ListView lista = (ListView) getView().findViewById(R.id.listaKnjiga);
        Button povratak = (Button) getView().findViewById(R.id.dPovratak);
        ImageView slike = (ImageView) getView().findViewById(R.id.naslovnaStr);

        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //KategorijeAkt.fm = getFragmentManager();
                if(KategorijeAkt.siriL) {
                    KategorijeAkt.fm.beginTransaction().remove(KategorijeAkt.knjigeFragment).commit();
                }
                else
                KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.listeFragment, KategorijeAkt.listeFragment.getTag()).commit();
            }
        });

        ListaAdapter customAdapter = new ListaAdapter(getActivity(), R.layout.listview_layout, listaKnjigaOdabranogZanra);
        lista.setAdapter(customAdapter);
//        lista.getChildAt(0).setBackgroundColor(Color.parseColor("#FFAABBED"));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                    long id) {

                Knjiga OdabranaKnjiga = (Knjiga) lista.getItemAtPosition(position);
                int o=KategorijeAkt.listeFragment.Baza.obojiKnjigu(OdabranaKnjiga);
                try {
                    lista.getChildAt(position).setBackgroundColor(Color.parseColor("#FFAABBED"));
                }
                catch(Exception e) {

                }

                }

        });

    }

    public class ListaAdapter extends ArrayAdapter<Knjiga> {

        public ListaAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListaAdapter(Context context, int resource, List<Knjiga> item) {
            super(context, resource, item);

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.listview_layout, null);

            }

            final Knjiga p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.eNaziv);
                TextView tt2 = (TextView) v.findViewById(R.id.eAutor);
                ImageView tt3 = (ImageView) v.findViewById(R.id.eNaslovna);
                TextView tt4 = (TextView) v.findViewById(R.id.eDatumObjavljivanja);
                TextView tt5 = (TextView) v.findViewById(R.id.eOpis);
                TextView tt6 = (TextView) v.findViewById(R.id.eBrojStranica);
                Button preporuci = (Button) v.findViewById(R.id.dPreporuci);

                tt1.setMovementMethod(new ScrollingMovementMethod());
                tt5.setMovementMethod(new ScrollingMovementMethod());

                //if (p.getOdabrana()) v.setBackgroundColor(Color.parseColor("#FFAABBED"));
                if(KategorijeAkt.listeFragment.Baza.jeLiObojena(p)==1)  v.setBackgroundColor(Color.parseColor("#FFAABBED"));
                if (tt1 != null) {
                    tt1.setText(p.getNazivKnjige());
                }

                if (tt2 != null) {
                    String autori=" ";
                    for(Autor a: KategorijeAkt.listeFragment.Baza.dajAutoreZadaneKnjige(p)) autori=autori+a.getImeIPrezime()+ ", ";
                    tt2.setText(autori);
                }

                if (tt3 != null) {
                  if(p.getSlikaURL()!=null)  Picasso.with(getActivity()).load(p.getSlikaURL().toString()).into(tt3);
                  else  tt3.setImageBitmap(p.getSlika());

                }

                if (tt4 != null) {
                    tt4.setText(p.getDatumObjavljivanja());
                }

                if (tt5 != null) {
                    tt5.setText(p.getOpis());
                }

                if (tt6 != null) {
                    tt6.setText(String.valueOf(p.getBrojStranica()));
                }

                if(preporuci!=null) {
                    preporuci.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentPreporuci.trenutnaKnjiga=p;
                            KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.fragmentPreporuci, KategorijeAkt.fragmentPreporuci.getTag()).commit();
                        }
                    });
                }
            }
            return v;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int arg0) {
            return true;
        }
    }
}

/*
protected String KategorijaClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);

        });

       /* for ( i = 0; i < lista.getChildCount(); i++) {
            Toast.makeText(ListaKnjigaAkt.this,
                    "Dodali ste novu knjigu"+i, Toast.LENGTH_LONG).show();
            Knjiga ObojenaKnjiga= (Knjiga) lista.getItemAtPosition(i);                                      //gledamo sve knjige u lista, ako je neka odabrana(true) bojimo pozadinu u plavo
            if(ObojenaKnjiga.getOdabrana()){
                lista.getChildAt(i).setBackgroundColor(Color.parseColor("#FFAABBED"));
            }
        }

        }

public class ListAdapter extends ArrayAdapter<Knjiga> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<Knjiga> item) {
        super(context, resource, item);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_layout, null);

        }

        Knjiga p = getItem(position);
            /*if(!p.getKategorija().equalsIgnoreCase(KategorijaClick.toString())) {
                return v;
            }


        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.eNaziv);
            TextView tt2 = (TextView) v.findViewById(R.id.eAutor);
            ImageView tt3=(ImageView) v.findViewById(R.id.eNaslovna);
            if(p.getOdabrana()) v.setBackgroundColor(Color.parseColor("#FFAABBED"));
            if (tt1 != null) {
                tt1.setText(p.getNazivKnjige());
            }

            if (tt2 != null) {
                tt2.setText(p.getImeAutora());
            }

            if (tt3 != null) {
                tt3.setImageBitmap(p.getSlika());
            }

        }

        return v;
    }

 */
