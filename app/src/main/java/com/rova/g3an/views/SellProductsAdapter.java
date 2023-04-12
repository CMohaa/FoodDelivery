package com.rova.g3an.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rova.g3an.R;
import com.rova.g3an.interfaces.OnProductClickListener;
import com.rova.g3an.models.Products;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SellProductsAdapter extends RecyclerView.Adapter<SellProductsAdapter.ViewHolder>  implements Filterable {
    private List<Products> productsList;
    public Context context;
    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private List<Products> productsList_Filtered;
    private OnProductClickListener onProductClickListener;
    public SellProductsAdapter(List<Products> _productList , OnProductClickListener onProductClickListener )
    {
        this.productsList = _productList;
        this.productsList_Filtered =_productList;
        this.onProductClickListener = onProductClickListener;
    }
    public void setList(List<Products> list) {
        this.productsList = list;
        this.productsList_Filtered =list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_layout , viewGroup , false);
        context = viewGroup.getContext();
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Products products = productsList.get(i);
        String name = products.getName();
        double price = products.getPrice();
        String seller = products.getTrader();
        String img = products.getThumb_image();
        viewHolder.name.setText(name);
        viewHolder.price.setText(String.valueOf(price));
        viewHolder.seller.setText(seller);
        Glide.with(context)
                .load(img) // image url
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_shop) // any placeholder to load at start
                        .error(R.drawable.ic_shop)  // any image in case of error
                        .override(200, 200) // resizing
                        .centerCrop())
                .into(viewHolder.src);  // imageview object
        viewHolder.src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductClickListener.onProductClicked(products, i);

            }
        });

        //viewHolder.price.setText(price);//cant cast to float

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
//                Log.d(TAG_CONTACTS_SEARCH, "ContactListAdapter.getFilter.performFiltering: " +
//                        "charString == " + charString);
                if (charString.isEmpty()) {
                    productsList_Filtered = productsList;
                } else {
                    List<Products> filteredList = new CopyOnWriteArrayList<>();
                    for (Products row : productsList) {
                        // search on the user fullname
                        if (row.getType().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    productsList_Filtered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productsList_Filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productsList_Filtered = (CopyOnWriteArrayList<Products>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }
    @Override
    public int getItemCount() {
        return productsList_Filtered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private CardView cardView;
        private TextView name;
        private ImageView src;
        private TextView price;//discountedCardPrice
        private TextView seller;//itemCardSeller

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            cardView = mView.findViewById(R.id.cardview1);
            name = mView.findViewById(R.id.itemCardTitle);
            src = mView.findViewById(R.id.itemCardImage);
            price = mView.findViewById(R.id.itemCardPrice);
            seller = mView.findViewById(R.id.itemCardSeller);

        }
    }
}
