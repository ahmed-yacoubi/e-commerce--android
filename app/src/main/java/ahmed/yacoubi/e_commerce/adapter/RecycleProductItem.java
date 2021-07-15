package ahmed.yacoubi.e_commerce.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.callback.CallBackProduct;
import ahmed.yacoubi.e_commerce.interfaces.*;
import ahmed.yacoubi.e_commerce.model.Product;

public class RecycleProductItem extends RecyclerView.Adapter<RecycleProductItem.CartViewHolder> {
    private List<Product> list;
    private Context context;
    private String typePage;
    private ClickProduct clickProduct;
    private ClickCounter clickCounter;
    private double totalPrice = 0;

    public RecycleProductItem(Context context, String typePage, ClickProduct clickProduct) {
        this.list = new ArrayList<>();
        this.clickProduct = clickProduct;
        this.context = context;
        this.typePage = typePage;

    }


    public void setList(List<Product> products) {
        this.list = products;
        notifyDataSetChanged();

    }


    public double getTotalPrice() {
        totalPrice = 0;
        for (Product p : list)
            totalPrice += p.getPrice();
        Toast.makeText(context, "" + totalPrice, Toast.LENGTH_SHORT).show();
        return round(totalPrice, 3);
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = -1;

        switch (typePage) {
            case "cart":
                layout = R.layout.cart_item;
                break;
            case "favorite":
                layout = R.layout.product_item_love;
                break;
            case "product":
                layout = R.layout.product_item;


        }
        return new CartViewHolder(LayoutInflater.from(context).inflate(layout, null, false));

    }


    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        switch (typePage) {

            case "cart":
                cartHolder(holder, position);

                break;

            case "favorite":
                favoriteHolder(holder, position);

                break;

            case "product":
                productHolder(holder, position);

                break;


        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public List<Product> getList() {
        return list;
    }


    private void cartHolder(@NonNull CartViewHolder holder, int position) {

        holder.img_imageProduct.setImageBitmap(list.get(position).getBitmap());


        holder.tv_NameProduct.setText(list.get(position).getName());
        holder.tv_details.setText(list.get(position).getDesc());
        holder.tv_totalPrice.setText(list.get(position).getPrice() + "$");

        holder.img_cancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProduct.onClickProduct(list.get(position), 1);
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.tv_ubCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.tv_count.getText().toString());
                if (count >= 2) {
                    count--;
                    holder.tv_count.setText(count + "");
                    holder.tv_totalPrice.setText(round(list.get(position).getPrice() * count, 3) + "$");
                    list.get(position).setCount(count);
                    totalPrice = (totalPrice - list.get(position).getPrice());
                    clickCounter.addCount(totalPrice);

                }
            }
        });
        holder.tv_addCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.tv_count.getText().toString());
                if (list.get(position).getAmount() > count) {
                    count++;
                    holder.tv_count.setText(count + "");
                    holder.tv_totalPrice.setText(round(list.get(position).getPrice() * count, 3) + "$");
                    totalPrice = (totalPrice + list.get(position).getPrice());
                    list.get(position).setCount(count);
                    clickCounter.addCount(totalPrice);
                } else {
                    Toast.makeText(context, "Maximum amount of product available", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void favoriteHolder(@NonNull CartViewHolder holder, int position) {

        holder.img_imageProduct.setImageBitmap(list.get(position).getBitmap());

        holder.tv_NameProduct.setText(list.get(position).getName());
        holder.tv_totalPrice.setText(list.get(position).getPrice() + "$");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProduct.onClickProduct(list.get(position), 1);
            }
        });

        holder.img_buyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickProduct.onClickProduct(list.get(position), 2);

            }
        });
        holder.img_cancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProduct.onClickProduct(list.get(position), 3);
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    private void productHolder(@NonNull CartViewHolder holder, int position) {

        holder.img_imageProduct.setImageBitmap(list.get(position).getBitmap());

        holder.tv_NameProduct.setText(list.get(position).getName());

        if (list.get(position).isFavorite()) {

            holder.img_favorite.setImageResource(R.drawable.ic_love);
        } else {

            holder.img_favorite.setImageResource(R.drawable.ic_love_no_color);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickProduct.onClickProduct(list.get(position), 1);
            }
        });

        holder.img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (list.get(position).isFavorite()) {
                    clickProduct.onClickProduct(list.get(position), 2);
                    holder.img_favorite.setImageResource(R.drawable.ic_love_no_color);
                } else {
                    clickProduct.onClickProduct(list.get(position), 3);
                    holder.img_favorite.setImageResource(R.drawable.ic_love);
                }

            }
        });
    }

    public void setAddCounter(ClickCounter clickCounter) {
        this.clickCounter = clickCounter;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_NameProduct;
        private TextView tv_details;
        private TextView tv_totalPrice;
        private TextView tv_ubCount;
        private TextView tv_count;
        private TextView tv_addCount;
        private ImageView img_imageProduct;
        private ImageView img_cancelProduct;
        private ImageView img_favorite;
        private ImageView img_buyProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_NameProduct = itemView.findViewById(R.id.items_tv_nameProduct);
            tv_details = itemView.findViewById(R.id.items__details);
            tv_totalPrice = itemView.findViewById(R.id.items_tv_totalPrice);
            tv_ubCount = itemView.findViewById(R.id.items__tv_subCount);
            tv_count = itemView.findViewById(R.id.items__tv_count);
            tv_addCount = itemView.findViewById(R.id.items__tv_addCount);
            img_imageProduct = itemView.findViewById(R.id.items_img_imageProduct);
            img_cancelProduct = itemView.findViewById(R.id.items_img_cancelProduct);
            img_favorite = itemView.findViewById(R.id.items_img_love);
            img_buyProduct = itemView.findViewById(R.id.item_img_buyProduct);
        }
    }

}
