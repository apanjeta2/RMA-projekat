package ba.unsa.etf.rma.ajla.projekatrma;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by User on 5.4.2018.
 */

public class DodavanjeKnjigeFragment extends Fragment {

    //public static ArrayList<Knjiga> Knjige=new ArrayList<Knjiga>();
    public static ArrayList<String> NaziviKnjiga;
    ImageView naslovnaStrana;
    public static final int PICK_IMAGE = 1;
    Uri UriSlike;
    public static Bitmap bSlike;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dodavanje_knjige_fragment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button upisiKnjigu=(Button) getView().findViewById(R.id.dUpisiKnjigu);
        Button ponisti=(Button) getView().findViewById(R.id.dPonisti);
        Button nadjiSliku=(Button) getView().findViewById(R.id.dNadjiSliku);
        final Spinner kategorijeKnjige=(Spinner) getView().findViewById(R.id.sKategorijaKnjige);

        final EditText Autor=(EditText) getView().findViewById(R.id.imeAutora);
        final EditText Naziv=(EditText) getView().findViewById(R.id.nazivKnjige);

        naslovnaStrana=(ImageView) getView().findViewById(R.id.naslovnaStr);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, KategorijeAkt.listeFragment.Baza.dajSveNaziveKategorija());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategorijeKnjige.setAdapter(adapter);

        NaziviKnjiga=new ArrayList<String>();

        //String[] novi= KategorijeAkt.nizStringova.toArray(new String[0]);


        upisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kategorijeKnjige;
                Knjiga nova=new Knjiga(Autor.getText().toString(), Naziv.getText().toString(), kategorijeKnjige.getSelectedItem().toString());
                nova.setSlika(bSlike);
                Autor autor= new Autor(Autor.getText().toString());
                ArrayList<Autor> autors = new ArrayList<Autor>(); autors.add(autor);
                nova.setAutori(autors);
                long pomKnjiga=KategorijeAkt.listeFragment.Baza.dodajKnjigu(nova);
                NaziviKnjiga.add(nova.getNazivKnjige());
                long pomAutor=KategorijeAkt.listeFragment.Baza.dodajAutora(autor);

               // if(pomAutor==-2) {autor.setBrKnjiga(autor.getBrKnjiga()+1); }
               // else
                if(pomKnjiga==-2) Toast.makeText(getActivity(),
                        "Knjiga vec postoji u bazi", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(getActivity(),
                            "Dodali ste novu knjigu", Toast.LENGTH_LONG).show();
                    KategorijeAkt.listeFragment.Baza.dodajAutorstvo(nova, autors);
                }
            }
        });


        ponisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //KategorijeAkt.fm =getFragmentManager();

                if(KategorijeAkt.siriL) {
                    FrameLayout f1 = (FrameLayout) getActivity().findViewById(R.id.dLayout);
                    f1.getLayoutParams().height = 900;
                    f1.getLayoutParams().width = 140;
                }
                KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.listeFragment, KategorijeAkt.listeFragment.getTag()).commit();
            }
        });
        nadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otvoriGaleriju();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE /*&& resultCode==RESULT_OK*/) {
            UriSlike=data.getData();
            naslovnaStrana.setImageURI(UriSlike);
            try {
                bSlike = getBitmapFromUri(UriSlike);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void otvoriGaleriju() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


}
