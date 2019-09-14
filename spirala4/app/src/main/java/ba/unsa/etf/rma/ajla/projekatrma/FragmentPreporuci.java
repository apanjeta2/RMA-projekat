package ba.unsa.etf.rma.ajla.projekatrma;

import android.Manifest;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FragmentPreporuci extends Fragment {

    public static Knjiga trenutnaKnjiga = new Knjiga();
    public String AUT="(nepoznat autor)";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_preporuci, container, false);

        return v;
    }

    private ContentResolver cr;
    private List<String> getContactNames() {
        List<String> contacts = new ArrayList<>();
        // Get the ContentResolver
        cr = getActivity().getContentResolver();
        // Get the Cursor of all the contacts
       Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Cursor cur1 = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);


        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur1.moveToNext()) {
                    try {
                        String name = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        contacts.add(name);
                    } catch (Exception e) {
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        cur1.close();

        return contacts;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView book = (TextView) getView().findViewById(R.id.textView6);
        TextView autor = (TextView) getView().findViewById(R.id.wAutor);
        TextView des = (TextView) getView().findViewById(R.id.wOpis);

        final Spinner kontakti = (Spinner) getView().findViewById(R.id.sKontakti);
        Button salji = (Button) getView().findViewById(R.id.dPosalji);
        Button povratak = (Button) getView().findViewById(R.id.fPovratak);

        book.setText(trenutnaKnjiga.getNazivKnjige());
        String autori="";
        for(Autor a: KategorijeAkt.listeFragment.Baza.dajAutoreZadaneKnjige(trenutnaKnjiga)) {
            if(AUT.equalsIgnoreCase("(nepoznat autor)")) AUT=a.getImeIPrezime();
            autori=autori+a.getImeIPrezime()+ ", ";
        }
        autor.setText(autori);
        des.setText(trenutnaKnjiga.getOpis());




        // Request code for READ_CONTACTS. It can be any number > 0.
        int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);

            KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.listeFragment, KategorijeAkt.listeFragment.getTag()).commit();
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            List<String> listaEmailova = getContactNames();
            String[] nizEmailova = new String[listaEmailova.size()];
            nizEmailova = listaEmailova.toArray(nizEmailova);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nizEmailova);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kontakti.setAdapter(adapter1);
        }


        salji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = kontakti.getSelectedItem().toString();
                String ime = " ";


                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                Cursor cur1 = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);


                if (cursor.moveToFirst()) {
                    do {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        cur1 = cr.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (cur1.moveToNext()) {
                            try {
                                if(cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)).equals(email))
                                 ime = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            } catch (Exception e) {
                            }
                        }
                    } while (cursor.moveToNext());
                }
                cur1.close(); cursor.close();
               // Toast.makeText(getActivity(), ime, Toast.LENGTH_SHORT).show();

                String[] TO = {email.toString()};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Preporuka knjige");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Zdravo " +ime + ",\n Procitaj knjigu " + trenutnaKnjiga.getNazivKnjige().toString() + " od " + AUT +"!");
                emailIntent.setType("text/plain");
                AUT="(nepoznat autor)";
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail"));
                    //finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(),
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KategorijeAkt.fm.beginTransaction().replace(R.id.dLayout, KategorijeAkt.listeFragment, KategorijeAkt.listeFragment.getTag()).commit();
            }

    });
}
}

