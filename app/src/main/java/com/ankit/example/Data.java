package com.ankit.example;

/**
 * Created by ankitagrawal on 26/7/16.
 */


        import java.util.ArrayList;
        import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("MyAccountShippingDetail")
    @Expose
    private Object myAccountShippingDetail;
    @SerializedName("MyAccountShippingItemDetail")
    @Expose
    private Object myAccountShippingItemDetail;
    @SerializedName("MyaccountPurchaseOrderItemList")
    @Expose
    private List<MyaccountPurchaseOrderItemList> myaccountPurchaseOrderItemList = new ArrayList<MyaccountPurchaseOrderItemList>();
    @SerializedName("PurchaseOrderDetails")
    @Expose
    private PurchaseOrderDetails purchaseOrderDetails;
    @SerializedName("RestrictedBrand")
    @Expose
    private String restrictedBrand;
    @SerializedName("RestrictedSubcategory")
    @Expose
    private String restrictedSubcategory;

    /**
     *
     * @return
     * The myAccountShippingDetail
     */
    public Object getMyAccountShippingDetail() {
        return myAccountShippingDetail;
    }

    /**
     *
     * @param myAccountShippingDetail
     * The MyAccountShippingDetail
     */
    public void setMyAccountShippingDetail(Object myAccountShippingDetail) {
        this.myAccountShippingDetail = myAccountShippingDetail;
    }

    /**
     *
     * @return
     * The myAccountShippingItemDetail
     */
    public Object getMyAccountShippingItemDetail() {
        return myAccountShippingItemDetail;
    }

    /**
     *
     * @param myAccountShippingItemDetail
     * The MyAccountShippingItemDetail
     */
    public void setMyAccountShippingItemDetail(Object myAccountShippingItemDetail) {
        this.myAccountShippingItemDetail = myAccountShippingItemDetail;
    }

    /**
     *
     * @return
     * The myaccountPurchaseOrderItemList
     */
    public List<MyaccountPurchaseOrderItemList> getMyaccountPurchaseOrderItemList() {
        return myaccountPurchaseOrderItemList;
    }

    /**
     *
     * @param myaccountPurchaseOrderItemList
     * The MyaccountPurchaseOrderItemList
     */
    public void setMyaccountPurchaseOrderItemList(List<MyaccountPurchaseOrderItemList> myaccountPurchaseOrderItemList) {
        this.myaccountPurchaseOrderItemList = myaccountPurchaseOrderItemList;
    }

    /**
     *
     * @return
     * The purchaseOrderDetails
     */
    public PurchaseOrderDetails getPurchaseOrderDetails() {
        return purchaseOrderDetails;
    }

    /**
     *
     * @param purchaseOrderDetails
     * The PurchaseOrderDetails
     */
    public void setPurchaseOrderDetails(PurchaseOrderDetails purchaseOrderDetails) {
        this.purchaseOrderDetails = purchaseOrderDetails;
    }

    /**
     *
     * @return
     * The restrictedBrand
     */
    public String getRestrictedBrand() {
        return restrictedBrand;
    }

    /**
     *
     * @param restrictedBrand
     * The RestrictedBrand
     */
    public void setRestrictedBrand(String restrictedBrand) {
        this.restrictedBrand = restrictedBrand;
    }

    /**
     *
     * @return
     * The restrictedSubcategory
     */
    public String getRestrictedSubcategory() {
        return restrictedSubcategory;
    }

    /**
     *
     * @param restrictedSubcategory
     * The RestrictedSubcategory
     */
    public void setRestrictedSubcategory(String restrictedSubcategory) {
        this.restrictedSubcategory = restrictedSubcategory;
    }

}



