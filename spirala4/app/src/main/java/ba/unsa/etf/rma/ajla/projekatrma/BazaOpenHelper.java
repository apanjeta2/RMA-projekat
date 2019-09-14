package ba.unsa.etf.rma.ajla.projekatrma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BazaOpenHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "mojaBaza.db";
    public static int DATABASE_VERSION = 1;


    public static String KATEGORIJA_TABLE = "Kategorija";
    public static String KATEGORIJA_ID = "_id";
    public static String KATEGORIJA_NAZIV = "naziv";

    public static String KNJIGA_TABLE = "Knjiga";
    public static String KNJIGA_ID = "_id";
    public static String KNJIGA_NAZIV = "naziv";
    public static String KNJIGA_OPIS = "opis";
    public static String KNJIGA_DATUM_OBJAVLJIVANJA = "datumObjavljivanja";
    public static String KNJIGA_BROJ_STRANICA = "brojStranica";
    public static String KNJIGA_ID_WEBSERVIS = "idWebServis";
    public static String KNJIGA_ID_KATEGORIJE = "idkategorije";
    public static String KNJIGA_SLIKA = "slika";
    public static String KNJIGA_PREGLEDANA = "pregledana";

    public static String AUTOR_TABLE = "Autor";
    public static String AUTOR_ID = "_id";
    public static String AUTOR_IME = "ime";

    public static String AUTORSTVO_TABLE = "Autorstvo";
    public static String AUTORSTVO_ID = "_id";
    public static String AUTORSTVO_ID_AUTORA = "idautora";
    public static String AUTORSTVO_ID_KNJIGE = "idknjige";

    public static final String KATEGORIJA_DATABASE_CREATE = "create table " + KATEGORIJA_TABLE +
            " (" + KATEGORIJA_ID + " integer primary key autoincrement, " + KATEGORIJA_NAZIV +
            " text not null );";

    public static final String KNJIGA_DATABASE_CREATE = "create table " + KNJIGA_TABLE +
            " (" + KNJIGA_ID + " integer primary key autoincrement," + KNJIGA_NAZIV +
            " text not null, " + KNJIGA_OPIS + " text, " + KNJIGA_DATUM_OBJAVLJIVANJA + " text, " + KNJIGA_BROJ_STRANICA
            + " integer, " + KNJIGA_ID_WEBSERVIS + " text, " + KNJIGA_ID_KATEGORIJE + " integer not null, " +
            KNJIGA_SLIKA + " text, " + KNJIGA_PREGLEDANA + " integer);";

    public static final String AUTOR_DATABASE_CREATE = "create table " + AUTOR_TABLE +
            " (" + AUTOR_ID + " integer primary key autoincrement," + AUTOR_IME +
            " text not null);";

    public static final String AUTORSTVO_DATABASE_CREATE = "create table " + AUTORSTVO_TABLE +
            " (" + AUTORSTVO_ID + " integer primary key autoincrement," + AUTORSTVO_ID_AUTORA +
            " integer not null, " + AUTORSTVO_ID_KNJIGE + " integer not null);";


    public BazaOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(KATEGORIJA_DATABASE_CREATE);
        db.execSQL(KNJIGA_DATABASE_CREATE);
        db.execSQL(AUTOR_DATABASE_CREATE);
        db.execSQL(AUTORSTVO_DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + KATEGORIJA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + KNJIGA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AUTOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AUTORSTVO_TABLE);

        onCreate(db);
    }


    public long dodajKategoriju(String vrijednost) {
        if (dajSveNaziveKategorija().contains(vrijednost)) {
            //Toast.makeText(this, "Knjiga vec postoji u bazi", Toast.LENGTH_LONG).show();
            return -2;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KATEGORIJA_ID, 0);
        values.put(KATEGORIJA_NAZIV, vrijednost);
        long kat_id;
        try {
            kat_id = db.insert(KATEGORIJA_TABLE, null, values);
        } catch (Exception e) {
            return -1;
        }
        return kat_id;
    }

    public ArrayList<Long> dajSveIdKategorije() {
        ArrayList<Long> ids = new ArrayList<Long>();
        String selectQuery = "SELECT  * FROM " + KATEGORIJA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ids.add(Long.valueOf(c.getInt(c.getColumnIndex(KATEGORIJA_ID))));
            } while (c.moveToNext());
        }

        return ids;
    }

    public ArrayList<String> dajSveNaziveKategorija() {
        ArrayList<String> ids = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + KATEGORIJA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ids.add(c.getString(c.getColumnIndex(KATEGORIJA_NAZIV)));
            } while (c.moveToNext());
        }

        return ids;
    }

    public ArrayList<String> dajSveNaziveKnjiga() {
        ArrayList<String> ids = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + KNJIGA_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ids.add(c.getString(c.getColumnIndex(KNJIGA_NAZIV)));
            } while (c.moveToNext());
        }

        return ids;
    }

    public ArrayList<String> dajSveNaziveAutora() {
        ArrayList<String> ids = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + AUTOR_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ids.add(c.getString(c.getColumnIndex(AUTOR_IME)));
            } while (c.moveToNext());
        }

        return ids;
    }

    public int dajIdKategorije(String kategorija) {
        int id = -1;
        String selectQuery = "SELECT _id FROM " + KATEGORIJA_TABLE + " WHERE " + KATEGORIJA_NAZIV + " LIKE \"" + kategorija + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndex(KATEGORIJA_ID));
            } while (c.moveToNext());
        }

        return id;
    }
    public String dajKategorijuIzId(int kategorija) {
        String id = " ";
        String selectQuery = "SELECT "+KATEGORIJA_NAZIV+" FROM " + KATEGORIJA_TABLE + " WHERE " + KATEGORIJA_ID + " LIKE \"" + kategorija + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                id = c.getString(c.getColumnIndex(KATEGORIJA_NAZIV));
            } while (c.moveToNext());
        }

        return id;
    }

    public int dajIdKnjige(String knjiga) {
        int id = -1;
        String selectQuery = "SELECT _id FROM " + KNJIGA_TABLE + " WHERE " + KNJIGA_NAZIV + " LIKE \"" + knjiga + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndex(KNJIGA_ID));
            } while (c.moveToNext());
        }

        return id;
    }

    public int dajIdAutora(String autor) {
        int id = -1;
        String selectQuery = "SELECT _id FROM " + AUTOR_TABLE + " WHERE " + AUTOR_IME + " LIKE \"" + autor + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndex(AUTOR_ID));
            } while (c.moveToNext());
        }

        return id;
    }


    public long dodajKnjigu(Knjiga knjiga) {
        if (dajSveNaziveKnjiga().contains(knjiga.getNazivKnjige())) {
            return -2;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KNJIGA_NAZIV, knjiga.getNazivKnjige());
        values.put(KNJIGA_OPIS, knjiga.getOpis());
        values.put(KNJIGA_DATUM_OBJAVLJIVANJA, knjiga.getDatumObjavljivanja());
        values.put(KNJIGA_BROJ_STRANICA, knjiga.getBrojStranica());
        values.put(KNJIGA_ID_WEBSERVIS, knjiga.getid());
        values.put(KNJIGA_ID_KATEGORIJE, dajIdKategorije(knjiga.getKategorija()));
        values.put(KNJIGA_SLIKA, String.valueOf(knjiga.getSlikaURL()));
        //int odabrana = knjiga.getOdabrana() ? 1 : 0;
        values.put(KNJIGA_PREGLEDANA, 0);

        long knjiga_id;
        try {
            knjiga_id = db.insert(KNJIGA_TABLE, null, values);
        } catch (Exception e) {
            return -1;
        }
        return knjiga_id;
    }

    public long dodajAutora(Autor autor) {
        if (dajSveNaziveAutora().contains(autor.getImeIPrezime())) {
            //Toast.makeText(this, "Knjiga vec postoji u bazi", Toast.LENGTH_LONG).show();
            return -2;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AUTOR_IME, autor.getImeIPrezime());

        long knjiga_id;
        try {
            knjiga_id = db.insert(AUTOR_TABLE, null, values);
        } catch (Exception e) {
            return -1;
        }
        return knjiga_id;
    }

    public void dodajAutorstvo(Knjiga knjiga, ArrayList<Autor> autori) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //ArrayList<Autor> sviAutori = knjiga.getAutori();

        for (Autor a : autori) {

            values.put(AUTORSTVO_ID_AUTORA, dajIdAutora(a.getImeIPrezime()));
            values.put(AUTORSTVO_ID_KNJIGE, dajIdKnjige(knjiga.getNazivKnjige()));

            long knjiga_id = db.insert(AUTORSTVO_TABLE, null, values);
        }

    }

    public int obojiKnjigu(Knjiga knjiga) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KNJIGA_PREGLEDANA, 1);

        // updating row
        return db.update(KNJIGA_TABLE, values, KNJIGA_ID + " = ?",
                new String[]{String.valueOf(dajIdKnjige(knjiga.getNazivKnjige()))});
    }

    public ArrayList<Knjiga> dajKnjigeOdabraneKategorije(String kategorija) {
        ArrayList<Knjiga> tags = new ArrayList<Knjiga>();
        String selectQuery = "SELECT  * FROM " + KNJIGA_TABLE + " WHERE " + KNJIGA_ID_KATEGORIJE + " LIKE \"" + dajIdKategorije(kategorija) + "\"";;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Knjiga t = new Knjiga();
                t.setId(c.getString((c.getColumnIndex(KNJIGA_ID_WEBSERVIS))));
                try {
                    t.setSlikaURL(new URL(c.getString(c.getColumnIndex(KNJIGA_SLIKA))));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if(c.getInt(c.getColumnIndex(KNJIGA_PREGLEDANA))==1) t.setOdabrana();
                t.setNazivKnjige(c.getString((c.getColumnIndex(KNJIGA_NAZIV))));
                t.setBrojStranica(c.getInt((c.getColumnIndex(KNJIGA_BROJ_STRANICA))));
                t.setDatumObjavljivanja(c.getString((c.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA))));
                t.setKategorija(kategorija);
                t.setOpis(c.getString((c.getColumnIndex(KNJIGA_OPIS))));
                // adding to tags list
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }
    public ArrayList<Autor> dajSveAutore() {
        ArrayList<Autor> tags= new ArrayList<Autor>();
        String selectQuery = "SELECT  * FROM " + AUTOR_TABLE ;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Autor t = new Autor(c.getString((c.getColumnIndex(AUTOR_IME))));
                //t.setImeIPrezime(c.getString((c.getColumnIndex(AUTOR_IME))));
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }

    public ArrayList<Knjiga> dajKnjigeZadanogAutora(Autor autor) {
        ArrayList<Knjiga> tags= new ArrayList<Knjiga>();
        String selectQuery = "SELECT  * FROM " + KNJIGA_TABLE + " k, " + AUTOR_TABLE + " a, " + AUTORSTVO_TABLE + " aut " +
                "WHERE k._id=aut." + AUTORSTVO_ID_KNJIGE + " AND a._id=aut." + AUTORSTVO_ID_AUTORA + " AND a." +
                AUTOR_IME + " LIKE \"" + autor.getImeIPrezime()+"\"";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Knjiga t = new Knjiga(autor.getImeIPrezime(), c.getString((c.getColumnIndex(KNJIGA_NAZIV))), "kategorija");
                t.setOpis(c.getString(c.getColumnIndex(KNJIGA_OPIS)));
                t.setKategorija(dajKategorijuIzId(c.getInt(c.getColumnIndex(KNJIGA_ID_WEBSERVIS))));
                t.setDatumObjavljivanja(c.getString(c.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA)));
                t.setBrojStranica(c.getInt(c.getColumnIndex(KNJIGA_BROJ_STRANICA)));
                try {
                    t.setSlikaURL(new URL(c.getString(c.getColumnIndex(KNJIGA_SLIKA))));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                t.setId(c.getString(c.getColumnIndex(KNJIGA_ID_WEBSERVIS)));
                if(c.getInt(c.getColumnIndex(KNJIGA_PREGLEDANA))==1)t.setOdabrana();
                t.setAutori(dajAutoreZadaneKnjige(t));
                //if(!dajSveNaziveKnjiga().contains(t.getNazivKnjige()))
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }
    public ArrayList<Autor> dajAutoreZadaneKnjige(Knjiga knjiga) {
        ArrayList<Autor> tags= new ArrayList<Autor>();
        String selectQuery = "SELECT  * FROM " + KNJIGA_TABLE + " k, " + AUTOR_TABLE + " a, " + AUTORSTVO_TABLE + " aut " +
                "WHERE k._id=aut." + AUTORSTVO_ID_KNJIGE + " AND a._id=aut." + AUTORSTVO_ID_AUTORA + " AND k." +
                KNJIGA_NAZIV + " LIKE \"" + knjiga.getNazivKnjige()+"\"";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Autor t = new Autor(c.getString((c.getColumnIndex(AUTOR_IME))));
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }

    public int daBrojKnjigaAutora(Autor autor) {
        return  dajKnjigeZadanogAutora(autor).size();
    }

    public int jeLiObojena(Knjiga knjiga) {
        String selectQuery = "SELECT  * FROM " + KNJIGA_TABLE + " WHERE " + KNJIGA_PREGLEDANA  +
                "=1 AND " + KNJIGA_NAZIV + " LIKE \"" + knjiga.getNazivKnjige().toString()+"\"";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        int i=0;
        if (c.moveToFirst()) {
            do {
                i++;
            } while (c.moveToNext());
        }
        return i;
    }
}

