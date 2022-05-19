package com.israel.livraisonexpresspos.data;

import com.israel.livraisonexpresspos.data.services.AuthService;
import com.israel.livraisonexpresspos.data.services.ContactService;
import com.israel.livraisonexpresspos.data.services.CourseService;
import com.israel.livraisonexpresspos.data.services.ErrorService;
import com.israel.livraisonexpresspos.data.services.ModuleService;
import com.israel.livraisonexpresspos.data.services.ProductService;
import com.israel.livraisonexpresspos.data.services.SteedService;

public class Api {
    public static ModuleService preload(){
        return RetrofitClient.getClient().create(ModuleService.class);
    }

    public static AuthService auth(){
        return RetrofitClient.getClient().create(AuthService.class);
    }

    public static CourseService course(){
        return RetrofitClient.getClient().create(CourseService.class);
    }

    public static ProductService products(){
        return RetrofitClient.getClient().create(ProductService.class);
    }
    public static ContactService contacts(){
        return RetrofitClient.getClient().create(ContactService.class);
    }
    //todo :  replace the old method by this one
    public static SteedService order(){
        return RetrofitClient.getClient().create(SteedService.class);
    }

    public static ErrorService error(){
        return RetrofitClient.getClient().create(ErrorService.class);
    }
}
