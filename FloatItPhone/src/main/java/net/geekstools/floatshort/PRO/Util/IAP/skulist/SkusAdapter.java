package net.geekstools.floatshort.PRO.Util.IAP.skulist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import net.geekstools.floatshort.PRO.R;
import net.geekstools.floatshort.PRO.Util.IAP.billing.BillingProvider;
import net.geekstools.floatshort.PRO.Util.IAP.skulist.row.RowViewHolder;
import net.geekstools.floatshort.PRO.Util.IAP.skulist.row.SkuRowData;

import java.util.HashMap;
import java.util.List;

public class SkusAdapter extends RecyclerView.Adapter<RowViewHolder> implements RowViewHolder.OnButtonClickListener {

    Activity activity;

    List<SkuRowData> rowDataList;
    BillingProvider billingProvider;

    HashMap<String, String> subscriptionPeriod = new HashMap<String, String>();

    public SkusAdapter(BillingProvider billingProvider, Activity activity) {
        this.billingProvider = billingProvider;
        this.activity = activity;

        /*
        * P1W equates to one week
        * P1M equates to one month
        * P3M equates to three months
        * P6M equates to six months
        * P1Y equates to one year
        */
        subscriptionPeriod.put("P1W", "Weekly");
        subscriptionPeriod.put("P1M", "Monthly");
        subscriptionPeriod.put("P3M", "Every 3 Month");
        subscriptionPeriod.put("P1Y", "Yearly");
    }

    public void updateData(List<SkuRowData> skuRowData) {
        rowDataList = skuRowData;

        notifyDataSetChanged();
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_sku_details_item, parent, false);
        return new RowViewHolder(inflate, this);
    }

    @Override
    public void onBindViewHolder(RowViewHolder rowViewHolder, int position) {

        SkuRowData skuRowData = getData(position);

        switch (skuRowData.getSku()) {
            case "remove.ads": {

                rowViewHolder.purchaseItemIcon.setImageResource(R.drawable.draw_no_ads);
                rowViewHolder.purchaseItemButton.setText(activity.getString(R.string.purchase));

                rowViewHolder.purchaseItemDescription.setText(skuRowData.getDescription()
                        + " | " + skuRowData.getSkuDetails().getPrice()
                        + " " +  subscriptionPeriod.get(skuRowData.getSkuDetails().getSubscriptionPeriod()));

                rowViewHolder.purchaseItemName.setText(skuRowData.getTitle());
                rowViewHolder.purchaseItemPrice.setText(skuRowData.getPrice());
                rowViewHolder.purchaseItemButton.setEnabled(true);

                break;
            }
            case "donation": {

                rowViewHolder.purchaseItemIcon.setImageResource(R.drawable.logo);
                rowViewHolder.purchaseItemButton.setText(activity.getString(R.string.donate));

                rowViewHolder.purchaseItemInfo.setText(R.string.thanks);

                rowViewHolder.purchaseItemName.setText(skuRowData.getTitle());
                rowViewHolder.purchaseItemDescription.setText(skuRowData.getDescription());
                rowViewHolder.purchaseItemPrice.setText(skuRowData.getPrice());
                rowViewHolder.purchaseItemButton.setEnabled(true);

                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return rowDataList == null ? 0 : rowDataList.size();
    }

    @Override
    public void onButtonClicked(int position) {
        SkuRowData skuRowData = getData(position);
        billingProvider.getBillingManager().startPurchaseFlow(skuRowData.getSkuDetails());

    }

    public SkuRowData getData(int position) {
        return rowDataList == null ? null : rowDataList.get(position);
    }
}

