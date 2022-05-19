package com.israel.livraisonexpresspos.data.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Room.dao.CartDao;
import com.israel.livraisonexpresspos.data.Room.dao.CommentDao;
import com.israel.livraisonexpresspos.data.Room.dao.ContactDao;
import com.israel.livraisonexpresspos.data.Room.dao.OrderDao;
import com.israel.livraisonexpresspos.data.Room.dao.UnSyncedDao;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Comment;
import com.israel.livraisonexpresspos.models.ContactTable;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.UnSynced;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Database(entities = {Cart.class, ContactTable.class, Order.class, UnSynced.class, Comment.class}, version = 2, exportSchema = false)
public abstract class LivrexRoomDatabase extends RoomDatabase {
    public abstract CartDao mCartDao();
    public abstract CommentDao mCommentDao();
    public abstract ContactDao mContactDao();
    public abstract OrderDao mOrderDao();
    public abstract UnSyncedDao mUnSynced();

    private static LivrexRoomDatabase INSTANCE;

    public static LivrexRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (LivrexRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LivrexRoomDatabase.class, "livrex_pos_database.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

//    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{
//        private ContactDao mDao;
//        private final List<Contact> mContacts;
//
//        public PopulateDbAsync(LivrexRoomDatabase db, WeakReference<Context> reference) {
////            mDao = db.mContactDao();
//            mContacts = new Gson().fromJson(getContactsFromAsset(reference.get()),
//                    new TypeToken<List<Contact>>(){}.getType());
//
//            Date date = Calendar.getInstance().getTime();
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
//            PreferenceUtils.setString(reference.get(), PreferenceUtils.LAST_SYNC_DATE, format.format(date));
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            for (Contact c : mContacts){
//                long id = mDao.insertContact(c);
//                List<Address> addresses = c.getAdresses();
//                if (addresses != null && !addresses.isEmpty()){
//                    for (Address a : addresses){
//                        a.setLocalIdContact((int) id);
//                        mDao.insertAddress(a);
//                    }
//                }
//            }
//            return null;
//        }
//    }

    public static String getContactsFromAsset(Context context){
        InputStream inputStream;
        String string = null;
        try {
            inputStream = context.getAssets().open("Contacts.json");
            int seize = inputStream.available();
            byte[] buffer = new byte[seize];
            inputStream.read(buffer);
            inputStream.close();
            String content = new String(buffer, StandardCharsets.UTF_8);
            JSONObject object = new JSONObject(content);
            JSONArray array = object.getJSONArray("data");
            string = array.toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            App.handleError(e);
        }
        return string;
    }
}