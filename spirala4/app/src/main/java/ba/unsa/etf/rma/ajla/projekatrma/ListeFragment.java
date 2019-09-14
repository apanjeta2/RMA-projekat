package ba.unsa.etf.rma.ajla.projekatrma;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 5.4.2018.
 */

public class ListeFragment extends Fragment {
     private EditText input;
     private String pomocni;
     private ListView lista;
     private Button dodajKnjigu;
     private Button pretraga;
     private Button dodajKategoriju;
     private Button autori;
     private Button kategorije;
     private boolean PrviPut=true;

    public BazaOpenHelper Baza;
    AdapterAutori customAdapter;

     static boolean prviPut=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View v= inflater.inflate(R.layout.liste_fragment,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Baza = new BazaOpenHelper(getContext());

        final Button dodajKategoriju = (Button) getView().findViewById(R.id.dDodajKategoriju);
        dodajKategoriju.setEnabled(false);
        final Button pretraga =(Button) getView().findViewById(R.id.dPretraga);
        final Button kategorije=(Button) getView().findViewById(R.id.dKategorije);
        Button autori=(Button) getView().findViewById(R.id.dAutori);
        Button dodajKnjigu=(Button) getView().findViewById(R.id.dDodajKnjigu);
        final EditText  input=(EditText) getView().findViewById(R.id.textPretraga);
        final ListView lista=(ListView) getView().findViewById(R.id.listaKategorija);
        final Button dodajOnline = (Button) getView().findViewById(R.id.dDodajOnline);

        pretraga.setBackgroundColor(getResources().getColor(R.color.gray));
        kategorije.setBackgroundColor(getResources().getColor(R.color.darkGrey));
        kategorije.setTextColor(getResources().getColor(R.color.white));
        autori.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
        dodajKnjigu.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
        dodajOnline.setBackgroundColor(getResources().getColor(R.color.colorLightBlue));
        dodajKategoriju.setBackgroundColor(getResources().getColor(R.color.gray));

        if(PrviPut) {
            //KategorijeAkt.Knjige.add(new Knjiga("Orhan Pamuk", "Snijeg", "Roman"));
            //String[] pom = {"Roman", "Drama", "Poezija"};
            //KategorijeAkt.nizStringova = new ArrayList<>(Arrays.asList(pom));
            //KategorijeAkt.nizStringova= Baza.dajSveNaziveKategorija();
            //KategorijeAkt.Autori.add(new Autor("Orhan Pamuk", 1));
        }
        PrviPut=false;


        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Baza.dajSveNaziveKategorija());

        kategorije.setOnClickListener(new View.OnClickListener() {   //postavljanje liste kada se klinkne Kategorije
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Baza.dajSveNaziveKategorija());
                lista.setAdapter(adapter);

                pretraga.setVisibility(View.VISIBLE);
                dodajKategoriju.setVisibility(View.VISIBLE);
                input.setVisibility(View.VISIBLE);
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(lista.getAdapter()==customAdapter) {
                    Autor a=customAdapter.getItem(i);
                    ArrayList<Knjiga> odabraneKnjige = new ArrayList<Knjiga>();
                    String imeAutora=a.getImeIPrezime();

                    Bundle argumenti = new Bundle();
                    argumenti.putParcelableArrayList("ListaK", KategorijeAkt.listeFragment.Baza.dajKnjigeZadanogAutora(a)); //niz knjiga prosljedjujemo u fragment
                    KategorijeAkt.knjigeFragment.setArguments(argumenti);
                    if (KategorijeAkt.siriL)
                        KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout2, KategorijeAkt.knjigeFragment, KategorijeAkt.knjigeFragment.getTag()).commit();
                    else
                        KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.knjigeFragment, KategorijeAkt.knjigeFragment.getTag()).commit();
                }
                else
                {
                    String KategorijaClick = (String) lista.getItemAtPosition(i); //preuzimamao kliknutu kategoriju
                    ArrayList<Knjiga> odabraneKnjige = new ArrayList<Knjiga>();
                        Bundle argumenti = new Bundle();
                        argumenti.putParcelableArrayList("ListaK", KategorijeAkt.listeFragment.Baza.dajKnjigeOdabraneKategorije(KategorijaClick)); //niz knjiga prosljedjujemo u fragment
                        KategorijeAkt.knjigeFragment.setArguments(argumenti);
                        if (KategorijeAkt.siriL)
                            KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout2, KategorijeAkt.knjigeFragment, KategorijeAkt.knjigeFragment.getTag()).commit();
                        else
                            KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.knjigeFragment, KategorijeAkt.knjigeFragment.getTag()).commit();

                }

            }
        });


        autori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customAdapter = new AdapterAutori(getContext(), KategorijeAkt.listeFragment.Baza.dajSveAutore());
                lista.setAdapter(customAdapter);
                pretraga.setVisibility(View.GONE);
                dodajKategoriju.setVisibility(View.GONE);
                input.setVisibility(View.GONE);
            }
        });

        pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lista.setAdapter(adapter);                       //???????????????????????????????????
                String provjera=input.getText().toString();
                pomocni=provjera;
                adapter.getFilter().filter(provjera);                   //provjeravamo ima li rijec u listi
                if(!Baza.dajSveNaziveKategorija().contains(provjera)) {
                    dodajKategoriju.setEnabled(true);
                }
            }
        });

        dodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //KategorijeAkt.nizStringova.add(0, input.getText().toString());
                long id=Baza.dodajKategoriju(input.getText().toString());
                //Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_LONG).show();
                adapter.clear();
                adapter.addAll(Baza.dajSveNaziveKategorija());
                adapter.notifyDataSetChanged();
                dodajKategoriju.setEnabled(false);
            }
        });
        dodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(KategorijeAkt.siriL) {

                    FrameLayout f1=(FrameLayout) getActivity().findViewById(R.id.dLayout);
                    f1.getLayoutParams().height =900;
                    f1.getLayoutParams().width =1200;
                    f1.requestLayout();
                    KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.dodavanjeKnjigeFragment, KategorijeAkt.dodavanjeKnjigeFragment.getTag()).commit();
                }
                KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.dodavanjeKnjigeFragment, KategorijeAkt.dodavanjeKnjigeFragment.getTag()).commit();
            }
        });
        dodajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KategorijeAkt.siriL) {

                    FrameLayout f1=(FrameLayout) getActivity().findViewById(R.id.dLayout);
                    f1.getLayoutParams().height =900;
                    f1.getLayoutParams().width =1200;
                    f1.requestLayout();
                    KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.fragmentOnline, KategorijeAkt.fragmentOnline.getTag()).commit();
                }
                KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.fragmentOnline, KategorijeAkt.fragmentOnline.getTag()).commit();
            }
        });

    }

    public class AdapterAutori extends ArrayAdapter<Autor> {

        private  ArrayList<Autor> item;

        public ArrayList<Autor> getItem() {
            return item;
        }

        public AdapterAutori(Context context, ArrayList<Autor> item) {
            super(context, R.layout.autori_layout, item);
            this.item=item;

        }
        public Autor getItem(int position) {
            return item.get(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater vi=LayoutInflater.from(getContext());
            View v=vi.inflate(R.layout.autori_layout, parent, false);

            Autor p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.eNaziv);
                TextView tt2 = (TextView) v.findViewById(R.id.dBroj);
                if (tt1 != null) {
                    tt1.setText(p.getImeIPrezime());
                }

                if (tt2 != null) {
                    tt2.setText(String.valueOf(KategorijeAkt.listeFragment.Baza.daBrojKnjigaAutora(p)));
                }
            }
            return v;

        }
    }
}
