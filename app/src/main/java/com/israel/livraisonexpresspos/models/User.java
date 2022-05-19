package com.israel.livraisonexpresspos.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private static User currentUser;

    private int id;
    private String provider_id;
    private String provider_name;
    private String username;
    private String email = "";
    private String telephone = "";
    private String fullname;
    private String firstname;
    private String lastname;
    private String telephone_alt;
    private String onesignal_email_auth_hash;
    private List<Integer> magasins_ids;
    private List<Integer> zones = new ArrayList<>();
    private List<Integer> city = new ArrayList<>();

    private List<String> roles;
    private String token;

    public User() {
    }

    public static User getCurrentUser(Context context) {
        if (currentUser == null){
            try {
                currentUser = new Gson().fromJson(PreferenceUtils.getString(context, PreferenceUtils.CURRENT_USER),
                        new TypeToken<User>(){}.getType());
            }catch (Exception e){
                App.handleError(e);
            }
        }
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    protected User(Parcel in) {
        id = in.readInt();
        provider_id = in.readString();
        provider_name = in.readString();
        username = in.readString();
        email = in.readString();
        telephone = in.readString();
        fullname = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        telephone_alt = in.readString();
        onesignal_email_auth_hash = in.readString();
        roles = in.createStringArrayList();
        token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(provider_id);
        dest.writeString(provider_name);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(telephone);
        dest.writeString(fullname);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(telephone_alt);
        dest.writeStringList(roles);
        dest.writeString(token);
        dest.writeString(onesignal_email_auth_hash);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTelephone_alt() {
        return telephone_alt;
    }

    public void setTelephone_alt(String telephone_alt) {
        this.telephone_alt = telephone_alt;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOnesignal_email_auth_hash() {
        return onesignal_email_auth_hash;
    }

    public void setOnesignal_email_auth_hash(String onesignal_email_auth_hash) {
        this.onesignal_email_auth_hash = onesignal_email_auth_hash;
    }

    public List<Integer> getMagasins_ids() {
        return magasins_ids;
    }

    public void setMagasins_ids(List<Integer> magasins_ids) {
        this.magasins_ids = magasins_ids;
    }

    public List<Integer> getZones() {
        return zones;
    }

    public void setZones(List<Integer> zones) {
        this.zones = zones;
    }

    public List<Integer> getCity() {
        return city;
    }

    public void setCity(List<Integer> city) {
        this.city = city;
    }

    public static boolean isManager(){
        if(currentUser == null)return false;
        return currentUser.getRoles().contains("gerant");
    }

    public static boolean isDeliveryMan(){
        if(currentUser == null)return false;
        return currentUser.getRoles().contains("coursier");
    }

    public static boolean isCommercial(){
        if(currentUser == null)return false;
        return currentUser.getRoles().contains("commercial");
    }

    public static boolean isPartner(){
        if(currentUser == null)return false;
        return currentUser.getRoles().contains("partenaire");
    }



    /**
     * role 1 = deliveryMan
     * role 2 = commercial
     * role 3 = manager
     * @return roleCode
     */
    public static int getRoleCode(){
        int roleCode = 0;
        if (isPartner()){
            roleCode = -1;
        }
        if (isDeliveryMan()) {
            roleCode = 1;
        }
        if (isCommercial()) {
            roleCode = 2;
        }
        if (isManager()) {
            roleCode = 3;
        }
        return roleCode;
    }
}
