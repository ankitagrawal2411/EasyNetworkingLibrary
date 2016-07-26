package com.ankit.example;

/**
 * Created by ankitagrawal on 26/7/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyaccountPurchaseOrderItemList {

    @SerializedName("ActualPrice")
    @Expose
    private Double actualPrice;
    @SerializedName("Bid")
    @Expose
    private Integer bid;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("CurrentStock")
    @Expose
    private Integer currentStock;
    @SerializedName("Discount")
    @Expose
    private Double discount;
    @SerializedName("EstimatedDeliveryDate")
    @Expose
    private String estimatedDeliveryDate;
    @SerializedName("Fde")
    @Expose
    private String fde;
    @SerializedName("InvoiceLevelDiscount")
    @Expose
    private Double invoiceLevelDiscount;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("LoyaltyCash")
    @Expose
    private Double loyaltyCash;
    @SerializedName("MRP")
    @Expose
    private Double mRP;
    @SerializedName("NDD")
    @Expose
    private Integer nDD;
    @SerializedName("OrderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("POItemID")
    @Expose
    private String pOItemID;
    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductURL")
    @Expose
    private String productURL;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("SiteType")
    @Expose
    private Integer siteType;
    @SerializedName("Size")
    @Expose
    private String size;
    @SerializedName("SubCatID")
    @Expose
    private Integer subCatID;
    @SerializedName("TNB")
    @Expose
    private Integer tNB;
    @SerializedName("gsourl")
    @Expose
    private String gsourl;
    @SerializedName("sameDayDelivery")
    @Expose
    private Integer sameDayDelivery;
    @SerializedName("shippingwarehouseid")
    @Expose
    private Integer shippingwarehouseid;
    @SerializedName("stocktype")
    @Expose
    private String stocktype;

    /**
     *
     * @return
     * The actualPrice
     */
    public Double getActualPrice() {
        return actualPrice;
    }

    /**
     *
     * @param actualPrice
     * The ActualPrice
     */
    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    /**
     *
     * @return
     * The bid
     */
    public Integer getBid() {
        return bid;
    }

    /**
     *
     * @param bid
     * The Bid
     */
    public void setBid(Integer bid) {
        this.bid = bid;
    }

    /**
     *
     * @return
     * The color
     */
    public String getColor() {
        return color;
    }

    /**
     *
     * @param color
     * The Color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @return
     * The currentStock
     */
    public Integer getCurrentStock() {
        return currentStock;
    }

    /**
     *
     * @param currentStock
     * The CurrentStock
     */
    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    /**
     *
     * @return
     * The discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     *
     * @param discount
     * The Discount
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     *
     * @return
     * The estimatedDeliveryDate
     */
    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    /**
     *
     * @param estimatedDeliveryDate
     * The EstimatedDeliveryDate
     */
    public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    /**
     *
     * @return
     * The fde
     */
    public String getFde() {
        return fde;
    }

    /**
     *
     * @param fde
     * The Fde
     */
    public void setFde(String fde) {
        this.fde = fde;
    }

    /**
     *
     * @return
     * The invoiceLevelDiscount
     */
    public Double getInvoiceLevelDiscount() {
        return invoiceLevelDiscount;
    }

    /**
     *
     * @param invoiceLevelDiscount
     * The InvoiceLevelDiscount
     */
    public void setInvoiceLevelDiscount(Double invoiceLevelDiscount) {
        this.invoiceLevelDiscount = invoiceLevelDiscount;
    }

    /**
     *
     * @return
     * The isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     * The IsActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     *
     * @return
     * The loyaltyCash
     */
    public Double getLoyaltyCash() {
        return loyaltyCash;
    }

    /**
     *
     * @param loyaltyCash
     * The LoyaltyCash
     */
    public void setLoyaltyCash(Double loyaltyCash) {
        this.loyaltyCash = loyaltyCash;
    }

    /**
     *
     * @return
     * The mRP
     */
    public Double getMRP() {
        return mRP;
    }

    /**
     *
     * @param mRP
     * The MRP
     */
    public void setMRP(Double mRP) {
        this.mRP = mRP;
    }

    /**
     *
     * @return
     * The nDD
     */
    public Integer getNDD() {
        return nDD;
    }

    /**
     *
     * @param nDD
     * The NDD
     */
    public void setNDD(Integer nDD) {
        this.nDD = nDD;
    }

    /**
     *
     * @return
     * The orderStatus
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     *
     * @param orderStatus
     * The OrderStatus
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     *
     * @return
     * The pOItemID
     */
    public String getPOItemID() {
        return pOItemID;
    }

    /**
     *
     * @param pOItemID
     * The POItemID
     */
    public void setPOItemID(String pOItemID) {
        this.pOItemID = pOItemID;
    }

    /**
     *
     * @return
     * The productID
     */
    public Integer getProductID() {
        return productID;
    }

    /**
     *
     * @param productID
     * The ProductID
     */
    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    /**
     *
     * @return
     * The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @param productName
     * The ProductName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return
     * The productURL
     */
    public String getProductURL() {
        return productURL;
    }

    /**
     *
     * @param productURL
     * The ProductURL
     */
    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    /**
     *
     * @return
     * The quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     * The Quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     * The siteType
     */
    public Integer getSiteType() {
        return siteType;
    }

    /**
     *
     * @param siteType
     * The SiteType
     */
    public void setSiteType(Integer siteType) {
        this.siteType = siteType;
    }

    /**
     *
     * @return
     * The size
     */
    public String getSize() {
        return size;
    }

    /**
     *
     * @param size
     * The Size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     *
     * @return
     * The subCatID
     */
    public Integer getSubCatID() {
        return subCatID;
    }

    /**
     *
     * @param subCatID
     * The SubCatID
     */
    public void setSubCatID(Integer subCatID) {
        this.subCatID = subCatID;
    }

    /**
     *
     * @return
     * The tNB
     */
    public Integer getTNB() {
        return tNB;
    }

    /**
     *
     * @param tNB
     * The TNB
     */
    public void setTNB(Integer tNB) {
        this.tNB = tNB;
    }

    /**
     *
     * @return
     * The gsourl
     */
    public String getGsourl() {
        return gsourl;
    }

    /**
     *
     * @param gsourl
     * The gsourl
     */
    public void setGsourl(String gsourl) {
        this.gsourl = gsourl;
    }

    /**
     *
     * @return
     * The sameDayDelivery
     */
    public Integer getSameDayDelivery() {
        return sameDayDelivery;
    }

    /**
     *
     * @param sameDayDelivery
     * The sameDayDelivery
     */
    public void setSameDayDelivery(Integer sameDayDelivery) {
        this.sameDayDelivery = sameDayDelivery;
    }

    /**
     *
     * @return
     * The shippingwarehouseid
     */
    public Integer getShippingwarehouseid() {
        return shippingwarehouseid;
    }

    /**
     *
     * @param shippingwarehouseid
     * The shippingwarehouseid
     */
    public void setShippingwarehouseid(Integer shippingwarehouseid) {
        this.shippingwarehouseid = shippingwarehouseid;
    }

    /**
     *
     * @return
     * The stocktype
     */
    public String getStocktype() {
        return stocktype;
    }

    /**
     *
     * @param stocktype
     * The stocktype
     */
    public void setStocktype(String stocktype) {
        this.stocktype = stocktype;
    }

}
