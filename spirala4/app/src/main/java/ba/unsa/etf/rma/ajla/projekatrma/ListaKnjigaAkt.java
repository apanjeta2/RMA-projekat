package ba.unsa.etf.rma.ajla.projekatrma;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.print.PrintDocumentAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListaKnjigaAkt extends AppCompatActivity {

    protected String KategorijaClick;
    public ArrayList<Knjiga> listaKnjigaOdabranogZanra=new ArrayList<>(); //Lista u koju smiještamo sve knjige zanra koji smo odabrali

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);
        final ListView lista = (ListView) findViewById(R.id.listaKnjiga);
        Button povratak = (Button) findViewById(R.id.dPovratak);
        ImageView slike=(ImageView) findViewById(R.id.naslovnaStr);


        KategorijaClick= getIntent().getStringExtra("Kategorija");  //preuzimamao kliknutu kategoriju

        int i=0;
//        for (i=0; i<KategorijeAkt.Knjige.size(); i++) {
//            if(KategorijeAkt.Knjige.get(i).getKategorija().equalsIgnoreCase(KategorijaClick.toString())) {  //izdvajamo samo knjige odabraneđo žanra u jednu listu, koju šaljemo kao parametar adapteru
//                listaKnjigaOdabranogZanra.add(KategorijeAkt.Knjige.get(i));
 //           }
 //       }

        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vrati=new Intent(ListaKnjigaAkt.this, KategorijeAkt.class);
                startActivity(vrati);
            }
        });


        ListAdapter customAdapter = new ListAdapter(this, R.layout.listview_layout, listaKnjigaOdabranogZanra);
        lista.setAdapter(customAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position,
                                    long id) {
                Knjiga OdabranaKnjiga=(Knjiga) lista.getItemAtPosition(position);
                int o=KategorijeAkt.listeFragment.Baza.obojiKnjigu(OdabranaKnjiga);
                for (int i = 0; i < lista.getChildCount(); i++) {
                    if(position == i ){
                        lista.getChildAt(i).setBackgroundColor(Color.parseColor("#FFAABBED"));
                    }
                }
            }
        });

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


            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.eNaziv);
                TextView tt2 = (TextView) v.findViewById(R.id.eAutor);
                TextView tt4 = (TextView) v.findViewById(R.id.eDatumObjavljivanja);
                TextView tt5 = (TextView) v.findViewById(R.id.eOpis);
                TextView tt6 = (TextView) v.findViewById(R.id.eBrojStranica);

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
                if (tt4 != null) {
                    tt4.setText(p.getDatumObjavljivanja());
                }

                if (tt5 != null) {
                    tt5.setText(p.getOpis());
                }

                if (tt6 != null) {
                    tt6.setText(p.getBrojStranica());
                }

            }

            return v;
        }

    }
}
