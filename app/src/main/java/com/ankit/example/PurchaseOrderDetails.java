package com.ankit.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ankitagrawal on 26/7/16.
 */
public class PurchaseOrderDetails {

    @SerializedName("BillAddress")
    @Expose
    private String billAddress;
    @SerializedName("BillEmailAddress")
    @Expose
    private String billEmailAddress;
    @SerializedName("BillMobileNo")
    @Expose
    private String billMobileNo;
    @SerializedName("BillName")
    @Expose
    private String billName;
    @SerializedName("CODCharges")
    @Expose
    private Double cODCharges;
    @SerializedName("CouponCode")
    @Expose
    private String couponCode;
    @SerializedName("CouponDiscount")
    @Expose
    private Double couponDiscount;
    @SerializedName("GLCouponDiscount")
    @Expose
    private Double gLCouponDiscount;
    @SerializedName("GLGiftCertificateAmount")
    @Expose
    private Double gLGiftCertificateAmount;
    @SerializedName("Gcc")
    @Expose
    private String gcc;
    @SerializedName("GiftCertificateAmount")
    @Expose
    private Double giftCertificateAmount;
    @SerializedName("LoyaltyCashEarned")
    @Expose
    private Integer loyaltyCashEarned;
    @SerializedName("LoyaltyRedeemCash")
    @Expose
    private Double loyaltyRedeemCash;
    @SerializedName("NetPayment")
    @Expose
    private Double netPayment;
    @SerializedName("POID")
    @Expose
    private String pOID;
    @SerializedName("PaymentName")
    @Expose
    private String paymentName;
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("PaymentStatusId")
    @Expose
    private Integer paymentStatusId;
    @SerializedName("PaymentTypeId")
    @Expose
    private Integer paymentTypeId;
    @SerializedName("RedeemAmount")
    @Expose
    private Double redeemAmount;
    @SerializedName("ShipAddress")
    @Expose
    private String shipAddress;
    @SerializedName("ShipCity")
    @Expose
    private String shipCity;
    @SerializedName("ShipMobileNo")
    @Expose
    private String shipMobileNo;
    @SerializedName("ShipName")
    @Expose
    private String shipName;
    @SerializedName("ShipPhoneNo")
    @Expose
    private String shipPhoneNo;
    @SerializedName("ShipPinCode")
    @Expose
    private Integer shipPinCode;
    @SerializedName("ShipState")
    @Expose
    private String shipState;
    @SerializedName("ShippingCharges")
    @Expose
    private Double shippingCharges;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("TotalItems")
    @Expose
    private Integer totalItems;
    @SerializedName("TotalPayment")
    @Expose
    private Double totalPayment;
    @SerializedName("TotalQuantity")
    @Expose
    private Integer totalQuantity;
    @SerializedName("TotalTax")
    @Expose
    private Double totalTax;
    @SerializedName("UserID")
    @Expose
    private Integer userID;
    @SerializedName("WalletAmount")
    @Expose
    private Double walletAmount;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    /**
     *
     * @return
     * The billAddress
     */
    public String getBillAddress() {
        return billAddress;
    }

    /**
     *
     * @param billAddress
     * The BillAddress
     */
    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress;
    }

    /**
     *
     * @return
     * The billEmailAddress
     */
    public String getBillEmailAddress() {
        return billEmailAddress;
    }

    /**
     *
     * @param billEmailAddress
     * The BillEmailAddress
     */
    public void setBillEmailAddress(String billEmailAddress) {
        this.billEmailAddress = billEmailAddress;
    }

    /**
     *
     * @return
     * The billMobileNo
     */
    public String getBillMobileNo() {
        return billMobileNo;
    }

    /**
     *
     * @param billMobileNo
     * The BillMobileNo
     */
    public void setBillMobileNo(String billMobileNo) {
        this.billMobileNo = billMobileNo;
    }

    /**
     *
     * @return
     * The billName
     */
    public String getBillName() {
        return billName;
    }

    /**
     *
     * @param billName
     * The BillName
     */
    public void setBillName(String billName) {
        this.billName = billName;
    }

    /**
     *
     * @return
     * The cODCharges
     */
    public Double getCODCharges() {
        return cODCharges;
    }

    /**
     *
     * @param cODCharges
     * The CODCharges
     */
    public void setCODCharges(Double cODCharges) {
        this.cODCharges = cODCharges;
    }

    /**
     *
     * @return
     * The couponCode
     */
    public String getCouponCode() {
        return couponCode;
    }

    /**
     *
     * @param couponCode
     * The CouponCode
     */
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    /**
     *
     * @return
     * The couponDiscount
     */
    public Double getCouponDiscount() {
        return couponDiscount;
    }

    /**
     *
     * @param couponDiscount
     * The CouponDiscount
     */
    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    /**
     *
     * @return
     * The gLCouponDiscount
     */
    public Double getGLCouponDiscount() {
        return gLCouponDiscount;
    }

    /**
     *
     * @param gLCouponDiscount
     * The GLCouponDiscount
     */
    public void setGLCouponDiscount(Double gLCouponDiscount) {
        this.gLCouponDiscount = gLCouponDiscount;
    }

    /**
     *
     * @return
     * The gLGiftCertificateAmount
     */
    public Double getGLGiftCertificateAmount() {
        return gLGiftCertificateAmount;
    }

    /**
     *
     * @param gLGiftCertificateAmount
     * The GLGiftCertificateAmount
     */
    public void setGLGiftCertificateAmount(Double gLGiftCertificateAmount) {
        this.gLGiftCertificateAmount = gLGiftCertificateAmount;
    }

    /**
     *
     * @return
     * The gcc
     */
    public String getGcc() {
        return gcc;
    }

    /**
     *
     * @param gcc
     * The Gcc
     */
    public void setGcc(String gcc) {
        this.gcc = gcc;
    }

    /**
     *
     * @return
     * The giftCertificateAmount
     */
    public Double getGiftCertificateAmount() {
        return giftCertificateAmount;
    }

    /**
     *
     * @param giftCertificateAmount
     * The GiftCertificateAmount
     */
    public void setGiftCertificateAmount(Double giftCertificateAmount) {
        this.giftCertificateAmount = giftCertificateAmount;
    }

    /**
     *
     * @return
     * The loyaltyCashEarned
     */
    public Integer getLoyaltyCashEarned() {
        return loyaltyCashEarned;
    }

    /**
     *
     * @param loyaltyCashEarned
     * The LoyaltyCashEarned
     */
    public void setLoyaltyCashEarned(Integer loyaltyCashEarned) {
        this.loyaltyCashEarned = loyaltyCashEarned;
    }

    /**
     *
     * @return
     * The loyaltyRedeemCash
     */
    public Double getLoyaltyRedeemCash() {
        return loyaltyRedeemCash;
    }

    /**
     *
     * @param loyaltyRedeemCash
     * The LoyaltyRedeemCash
     */
    public void setLoyaltyRedeemCash(Double loyaltyRedeemCash) {
        this.loyaltyRedeemCash = loyaltyRedeemCash;
    }

    /**
     *
     * @return
     * The netPayment
     */
    public Double getNetPayment() {
        return netPayment;
    }

    /**
     *
     * @param netPayment
     * The NetPayment
     */
    public void setNetPayment(Double netPayment) {
        this.netPayment = netPayment;
    }

    /**
     *
     * @return
     * The pOID
     */
    public String getPOID() {
        return pOID;
    }

    /**
     *
     * @param pOID
     * The POID
     */
    public void setPOID(String pOID) {
        this.pOID = pOID;
    }

    /**
     *
     * @return
     * The paymentName
     */
    public String getPaymentName() {
        return paymentName;
    }

    /**
     *
     * @param paymentName
     * The PaymentName
     */
    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    /**
     *
     * @return
     * The paymentStatus
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     *
     * @param paymentStatus
     * The PaymentStatus
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     *
     * @return
     * The paymentStatusId
     */
    public Integer getPaymentStatusId() {
        return paymentStatusId;
    }

    /**
     *
     * @param paymentStatusId
     * The PaymentStatusId
     */
    public void setPaymentStatusId(Integer paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    /**
     *
     * @return
     * The paymentTypeId
     */
    public Integer getPaymentTypeId() {
        return paymentTypeId;
    }

    /**
     *
     * @param paymentTypeId
     * The PaymentTypeId
     */
    public void setPaymentTypeId(Integer paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    /**
     *
     * @return
     * The redeemAmount
     */
    public Double getRedeemAmount() {
        return redeemAmount;
    }

    /**
     *
     * @param redeemAmount
     * The RedeemAmount
     */
    public void setRedeemAmount(Double redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    /**
     *
     * @return
     * The shipAddress
     */
    public String getShipAddress() {
        return shipAddress;
    }

    /**
     *
     * @param shipAddress
     * The ShipAddress
     */
    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    /**
     *
     * @return
     * The shipCity
     */
    public String getShipCity() {
        return shipCity;
    }

    /**
     *
     * @param shipCity
     * The ShipCity
     */
    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    /**
     *
     * @return
     * The shipMobileNo
     */
    public String getShipMobileNo() {
        return shipMobileNo;
    }

    /**
     *
     * @param shipMobileNo
     * The ShipMobileNo
     */
    public void setShipMobileNo(String shipMobileNo) {
        this.shipMobileNo = shipMobileNo;
    }

    /**
     *
     * @return
     * The shipName
     */
    public String getShipName() {
        return shipName;
    }

    /**
     *
     * @param shipName
     * The ShipName
     */
    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    /**
     *
     * @return
     * The shipPhoneNo
     */
    public String getShipPhoneNo() {
        return shipPhoneNo;
    }

    /**
     *
     * @param shipPhoneNo
     * The ShipPhoneNo
     */
    public void setShipPhoneNo(String shipPhoneNo) {
        this.shipPhoneNo = shipPhoneNo;
    }

    /**
     *
     * @return
     * The shipPinCode
     */
    public Integer getShipPinCode() {
        return shipPinCode;
    }

    /**
     *
     * @param shipPinCode
     * The ShipPinCode
     */
    public void setShipPinCode(Integer shipPinCode) {
        this.shipPinCode = shipPinCode;
    }

    /**
     *
     * @return
     * The shipState
     */
    public String getShipState() {
        return shipState;
    }

    /**
     *
     * @param shipState
     * The ShipState
     */
    public void setShipState(String shipState) {
        this.shipState = shipState;
    }

    /**
     *
     * @return
     * The shippingCharges
     */
    public Double getShippingCharges() {
        return shippingCharges;
    }

    /**
     *
     * @param shippingCharges
     * The ShippingCharges
     */
    public void setShippingCharges(Double shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The Status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The totalItems
     */
    public Integer getTotalItems() {
        return totalItems;
    }

    /**
     *
     * @param totalItems
     * The TotalItems
     */
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    /**
     *
     * @return
     * The totalPayment
     */
    public Double getTotalPayment() {
        return totalPayment;
    }

    /**
     *
     * @param totalPayment
     * The TotalPayment
     */
    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
    }

    /**
     *
     * @return
     * The totalQuantity
     */
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    /**
     *
     * @param totalQuantity
     * The TotalQuantity
     */
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    /**
     *
     * @return
     * The totalTax
     */
    public Double getTotalTax() {
        return totalTax;
    }

    /**
     *
     * @param totalTax
     * The TotalTax
     */
    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    /**
     *
     * @return
     * The userID
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     *
     * @param userID
     * The UserID
     */
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    /**
     *
     * @return
     * The walletAmount
     */
    public Double getWalletAmount() {
        return walletAmount;
    }

    /**
     *
     * @param walletAmount
     * The WalletAmount
     */
    public void setWalletAmount(Double walletAmount) {
        this.walletAmount = walletAmount;
    }

    /**
     *
     * @return
     * The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @param createdDate
     * The createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
