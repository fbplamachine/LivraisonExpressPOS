package com.israel.livraisonexpresspos.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class OrderWithCarts{
    @Embedded
    public Order mOrder;
    @Relation(
            parentColumn = "id",
            entityColumn = "orderId"
    )
    public List<Cart> mCarts;

    @Override
    public String toString() {
        return "OrderWithCarts{" +
                "mOrder=" + mOrder +
                ", mCarts=" + mCarts +
                '}';
    }
}
