package com.israel.livraisonexpresspos.ui.products;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.repository.CartRepository;
import com.israel.livraisonexpresspos.data.Room.repository.OrderRepository;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.OrderWithCarts;
import com.israel.livraisonexpresspos.models.Product;
import com.israel.livraisonexpresspos.utils.OrderStatus;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Product>> mProducts;
    private final MutableLiveData<String> mError;
    private final MutableLiveData<Boolean> mLoading;
    private final CartRepository mCartRepository;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        mProducts = new MutableLiveData<>();
        mError = new MutableLiveData<>();
        mLoading = new MutableLiveData<>();
        mCartRepository = new CartRepository(application);
    }

    public LiveData<Boolean> getLoading() {
        return mLoading;
    }

    public LiveData<List<Product>> getProducts() {
        return mProducts;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public void fetchProducts(final Context context){
        String stringProducts = PreferenceUtils.getString(context,
                PreferenceUtils.PRODUCTS + Values.shop.getSlug());
        if (stringProducts.equals("")){
            mLoading.setValue(true);
        }else {
            List<Product> products = new Gson().fromJson(stringProducts,
                    new TypeToken<List<Product>>(){}.getType());
            mProducts.setValue(products);
        }

        if (App.isConnected){
            Api.products().getProductsFromShop(Values.shop.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    mLoading.setValue(false);
                    if (response.isSuccessful() && response.body() != null){
                        try{
                            String output = response.body().string();
                            JSONArray data = new JSONObject(output).getJSONArray("data");
                            List<Product> products = new Gson().fromJson(data.toString(),
                                    new TypeToken<List<Product>>(){}.getType());

                            JsonArray array = new Gson().toJsonTree(products,
                                    new TypeToken<List<Product>>(){}.getType()).getAsJsonArray();

                            PreferenceUtils.setString(context
                                    , PreferenceUtils.PRODUCTS + Values.shop.getSlug()
                                    , array.toString());

                            mProducts.setValue(products);
                        }catch (JSONException | IOException e){
                            e.printStackTrace();
                            App.handleError(e);
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    mLoading.setValue(false);
                }
            });
        }

    }

    public void insertCart(Cart cart){
        mCartRepository.insert(cart);
    }

    public LiveData<List<Cart>> getCartItems(int orderId, int shopId){
        return mCartRepository.getAllLiveItems(orderId, shopId);
    }

    public void addProductToCart(Product product, int size, int orderId, int shopId){
        OrderRepository repository = new OrderRepository(getApplication());
        if (Values.order == null){
            Order order = new Order();
            order.setDateTime(Calendar.getInstance().getTimeInMillis());
            order.setStatus(OrderStatus.saved.toString());
            order.setShop(Values.shop.getNom());
            order.setShopId(Values.shop.getId());
            order.setShopObject(new Gson().toJson(Values.shop));
            order.setModuleObject(new Gson().toJson(Values.module));
            order.setModuleSlug(Values.module.getSlug());
            order.setCity(Values.city);
            Delivery delivery = new Delivery();
            delivery.setSender(Values.sender);
            delivery.setReceiver(Values.receiver);
            order.setStringDelivery(Values.deliveryToString(delivery));
            Values.order = order;
        }else if (size > 0){
            Values.order.setShop(Values.shop.getNom());
            Values.order.setCity(Values.city);
            Values.order.setShopId(Values.shop.getId());
            Values.order.setShopObject(new Gson().toJson(Values.shop));
            Values.order.setModuleObject(new Gson().toJson(Values.module));
            Values.order.setModuleSlug(Values.module.getSlug());
            repository.update(Values.order);
        }
        Values.setSender(Values.shop);
        Cart cart = new Cart();
        cart.setLibelle(product.getLibelle());
        cart.setImage(product.getImage());
        cart.setPrix_unitaire(product.getPrix_unitaire());
        cart.setMontant_total(String.valueOf(product.getPrix_unitaire()));
        cart.setMagasin_id(product.getMagasin_id());
        //cart.setCategorie_id(product.getCategorie_id());
        cart.setQuantite(1);
        cart.setProduct_id(product.getId());
        cart.setOrderId(Values.order.getId());
        if (Values.order.getId() == 0){
            OrderWithCarts orderWithCarts = new OrderWithCarts();
            orderWithCarts.mOrder = Values.order;
            List<Cart> carts = new ArrayList<>();
            carts.add(cart);
            orderWithCarts.mCarts = carts;
            repository.insertOrderWithCarts(orderWithCarts);
        }else {
            cart.setOrderId(Values.order.getId());
            checkIfProductExist(cart, orderId, shopId);
        }

        Toasty.success(getApplication(), "Produit ajouté avec succès", Toast.LENGTH_SHORT).show();
    }

    public void checkIfProductExist(Cart cart, int orderId, int shopId){
        new Thread(() -> {
           List<Cart> carts = mCartRepository. getCartDao().getItems(orderId, shopId);
           boolean productExist = false;
           for(Cart c : carts){
               if (c.getProduct_id() == cart.getProduct_id() && c.getPrix_unitaire() == cart.getPrix_unitaire()){
                   c.setQuantite(c.getQuantite() + cart.getQuantite());
                   mCartRepository.update(c);
                   productExist = true;
                   break;
               }
           }
           if(!productExist){
               mCartRepository.insert(cart);
           }
        }).start();
    }

}
