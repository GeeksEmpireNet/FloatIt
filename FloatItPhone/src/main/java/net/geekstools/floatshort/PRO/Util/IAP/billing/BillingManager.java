package net.geekstools.floatshort.PRO.Util.IAP.billing;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import net.geekstools.floatshort.PRO.Util.Functions.FunctionsClass;
import net.geekstools.floatshort.PRO.Util.IAP.Util.PurchasesCheckpoint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BillingManager implements PurchasesUpdatedListener {

    private static final String TAG = "BillingManager";

    FunctionsClass functionsClass;

    private BillingClient billingClient;
    private AppCompatActivity appCompatActivity;

    String UserEmailAddress = null;

    private static final HashMap<String, List<String>> SKUS;

    static {
        SKUS = new HashMap<>();
        SKUS.put(BillingClient.SkuType.INAPP, Arrays.asList("donation"));
        SKUS.put(BillingClient.SkuType.SUBS, Arrays.asList("remove.ads"));
    }

    public static final String iapDonation = "donation";
    public static final String iapRemoveAds = "remove.ads";

    public List<String> getSkus(@BillingClient.SkuType String type) {
        return SKUS.get(type);
    }

    public BillingManager(AppCompatActivity appCompatActivity, String UserEmailAddress) {
        this.appCompatActivity = appCompatActivity;
        this.UserEmailAddress = UserEmailAddress;

        functionsClass = new FunctionsClass(appCompatActivity.getApplicationContext(), appCompatActivity);

        billingClient = new PurchasesCheckpoint(appCompatActivity).trigger();
    }

    public BillingResult startPurchaseFlow(SkuDetails skuDetails) {

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();

        return billingClient.launchBillingFlow(appCompatActivity, billingFlowParams);
    }

    public void querySkuDetailsAsync(@BillingClient.SkuType final String itemType, final List<String> skuList, final SkuDetailsResponseListener listener) {
        SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(itemType).build();
        billingClient.querySkuDetailsAsync(skuDetailsParams, new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                listener.onSkuDetailsResponse(billingResult, skuDetailsList);
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        //ResponseCode 7 = Item Owned
        Log.d(TAG, "onPurchasesUpdated() Response: " + billingResult.getResponseCode());

        new PurchasesCheckpoint(appCompatActivity).trigger();
    }
}
