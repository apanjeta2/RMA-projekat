package ba.unsa.etf.rma.ajla.projekatrma;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, MojResultReceiver.Receiver {


    String[] ids= new ArrayList<String>().toArray(new String[0]); //Ovdje swe cuvaju nazivi knjiga koje se dobiju iz Google Api
    private Button povratak;
    private Button run;
    private EditText ime;
    private Spinner knjige;
    public static ArrayList<Knjiga> listaKnjiga = new ArrayList<Knjiga>();



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_online, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        povratak = (Button) getView().findViewById(R.id.dPovratak);
        run = (Button) getView().findViewById(R.id.dRun);
        ime = (EditText) getView().findViewById(R.id.tekstUpit);
        knjige = (Spinner) getView().findViewById(R.id.sRezultat);
        final Spinner kategorije = (Spinner) getView().findViewById(R.id.sKategorije);
        Button dodajKnjigu = (Button) getView().findViewById(R.id.dAdd);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, KategorijeAkt.listeFragment.Baza.dajSveNaziveKategorija());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategorije.setAdapter(adapter);

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rezultat=ime.getText().toString();
                if(rezultat.startsWith("autor:")) {
                    rezultat = rezultat.split(":")[1];
                    new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone) FragmentOnline.this).execute(rezultat);
                }
                else if(rezultat.startsWith("korisnik:"))  {
                    rezultat=rezultat.split(":")[1];
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);
                    MojResultReceiver mReceiver = new MojResultReceiver(new Handler());
                    mReceiver.setReceiver(FragmentOnline.this);
                    intent.putExtra("idKorisnika",rezultat);
                    intent.putExtra("receiver", mReceiver);
                    getActivity().startService(intent);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ids);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    knjige.setAdapter(adapter);
                }
                else if(!rezultat.contains(";")) {
                    new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(rezultat);
                }
                else {
                    String[] sviRezultati=rezultat.split(";");
                    new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(sviRezultati);
                }
            }
        });
        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(KategorijeAkt.siriL) {
                    FrameLayout f1 = (FrameLayout) getActivity().findViewById(R.id.dLayout);
                    f1.getLayoutParams().height = 900;
                    f1.getLayoutParams().width = 140;
                }
                KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.listeFragment, KategorijeAkt.listeFragment.getTag()).commit();
                //KategorijeAkt.fm = getFragmentManager();

            }
        });
        dodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Knjiga nova = new Knjiga(" ", " ", kategorije.getSelectedItem().toString());

                try {
                    for (int i = 0; i < listaKnjiga.size(); i++) {

                        if (listaKnjiga.get(i).getNazivKnjige().equals(knjige.getSelectedItem().toString())) {
                            nova = listaKnjiga.get(i);
                            break;
                        }
                    }

                    Bitmap slika = BitmapFactory.decodeResource(getResources(), R.drawable.slika_naslovna);
                    if(nova.getSlika()==null) nova.setSlika(slika);
                }
                catch(Exception e) {
                    Toast.makeText(getActivity(), "Niste odabrali knjigu", Toast.LENGTH_LONG).show();
                }
                nova.setKategorija(kategorije.getSelectedItem().toString());

                    long idKnjige = KategorijeAkt.listeFragment.Baza.dodajKnjigu(nova);

                    try{
                    for(Autor aa:nova.getAutori()) {
                        KategorijeAkt.listeFragment.Baza.dodajAutora(aa);
                    }

                       if(idKnjige!=-2) {Toast.makeText(getActivity(),
                                "Dodali ste novu knjigu sa " + nova.getAutori().size() + " autora", Toast.LENGTH_LONG).show();
                        KategorijeAkt.listeFragment.Baza.dodajAutorstvo(nova, nova.getAutori()); }
                    else {
                           Toast.makeText(getActivity(),
                                   "Knjiga vec postoji u listi", Toast.LENGTH_LONG).show();
                       }

                }
                catch(Exception e) {
                    Toast.makeText(getActivity(),
                            "Morate izabrati knjigu", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> rez) {
        listaKnjiga=rez;
        ArrayList<String> nazivi=new ArrayList<String>();
        for (int i=0; i< listaKnjiga.size(); i++) {
            nazivi.add(listaKnjiga.get(i).getNazivKnjige());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nazivi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        knjige.setAdapter(adapter);
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> rez) {
        listaKnjiga=rez;
        ArrayList<String> nazivi=new ArrayList<String>();
        for (int i=0; i< listaKnjiga.size(); i++) {
            nazivi.add(listaKnjiga.get(i).getNazivKnjige());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nazivi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        knjige.setAdapter(adapter);
    }

    @Override
    public void onReceiverResult(int resultCode, Bundle resultData) {
        switch(resultCode) {
            case KnjigePoznanika.STATUS_START:
                break;
            case KnjigePoznanika.STATUS_FINISH:
                ids=resultData.getStringArray("resultData");
                String[] result = resultData.getStringArray("result");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ids);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                knjige.setAdapter(adapter);
                break;
            case KnjigePoznanika.STATUS_ERROR:
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
