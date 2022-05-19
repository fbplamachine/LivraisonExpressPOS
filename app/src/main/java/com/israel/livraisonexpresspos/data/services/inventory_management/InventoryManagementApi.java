package com.israel.livraisonexpresspos.data.services.inventory_management;


public class InventoryManagementApi {

    public static HistoryManagementService historyManagementService(){
        return InventoryManagementClient.getClient().create(HistoryManagementService.class);
    }

    public static MovesManagementService movesManagementService(){
        return InventoryManagementClient.getClient().create(MovesManagementService.class);
    }

    public static SiteManagementService siteManagementService(){
        return InventoryManagementClient.getClient().create(SiteManagementService.class);
    }

    public static StockDistributionManagementService stockDistributionManagementService(){
        return InventoryManagementClient.getClient().create(StockDistributionManagementService.class);
    }

    public static StockFilterService filterService() {
        return InventoryManagementClient.getClient().create(StockFilterService.class);
    }
}
