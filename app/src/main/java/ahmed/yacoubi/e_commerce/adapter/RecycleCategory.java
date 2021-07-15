package ahmed.yacoubi.e_commerce.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import ahmed.yacoubi.e_commerce.R;
import ahmed.yacoubi.e_commerce.interfaces.*;
import ahmed.yacoubi.e_commerce.model.Category;

public class RecycleCategory extends RecyclerView.Adapter<RecycleCategory.ViewHolderCategory> {

    // we went use two type of view in one recycle  ...
    private List<Category> list = new ArrayList<>();
    private Context context;
    private static boolean isSmall = true;
    private ViewHolderCategory tempCategoryHolder;
    private boolean mustSmall = false;

    private ClickCategory clickCategory;

    public RecycleCategory(Context context, ClickCategory clickCategory) {
        this.context = context;
        this.clickCategory = clickCategory;
    }

    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCategory(LayoutInflater.from(context).inflate(R.layout.category_item, null, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategory holder, int position) {

        if (list.size() == (position + 1) || list.size() == (position + 2)) {
            bigView(holder, position);
        } else {
            if (list.get(position).isSmall() || mustSmall) {
                if (isSmall) {
                    this.tempCategoryHolder = holder;
                    skipView(holder);
                    isSmall = false;
                    mustSmall = true;
                } else {

                    smallView(holder, this.tempCategoryHolder);
                    mustSmall = false;
                    this.tempCategoryHolder = null;
                }
            } else {

                bigView(holder, position);

            }

        }


    }

    private void skipView(ViewHolderCategory holder) {
        holder.categoryFrameSmall1.setVisibility(View.GONE);
        holder.categoryFrameSmall2.setVisibility(View.GONE);
        holder.categoryFrameBig.setVisibility(View.GONE);
    }

    private void bigView(ViewHolderCategory holder, int position) {
        holder.categoryFrameSmall1.setVisibility(View.GONE);
        holder.categoryFrameSmall2.setVisibility(View.GONE);
        holder.categoryFrameBig.setVisibility(View.VISIBLE);
        holder.categoryTvNameB.setText(list.get(position).getName());


        holder.categoryTvNameS2.setText(list.get(position).getName());


        holder.categoryImgCategoryImageB.setImageBitmap(list.get(position).getBitmap());

        holder.categoryFrameBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCategory.onClickCategory(list.get(position).getId() + "");
            }
        });


    }

    private void smallView(ViewHolderCategory holder1, ViewHolderCategory holder2) {
        holder1.categoryFrameBig.setVisibility(View.GONE);
        holder1.categoryFrameSmall1.setVisibility(View.VISIBLE);
        holder1.categoryFrameSmall2.setVisibility(View.VISIBLE);
        holder1.categoryTvNameS1.setText(list.get(holder1.getPosition()).getName()
        );

        holder1.categoryImgCategoryImageS1.setImageBitmap(list.get(holder1.getPosition()).getBitmap());
        holder1.categoryFrameSmall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCategory.onClickCategory(list.get(holder1.getPosition()).getId() + "");
            }
        });


        holder2.categoryTvNameS2.setText(list.get(holder2.getPosition()).getName());


        holder2.categoryImgCategoryImageS2.setImageBitmap(list.get(holder2.getPosition()).getBitmap());
        holder2.categoryFrameSmall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCategory.onClickCategory(list.get(holder2.getPosition()).getId() + "");

            }
        });
        isSmall = true;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolderCategory extends RecyclerView.ViewHolder {
        private FrameLayout categoryFrameBig;
        private TextView categoryTvNameB;
        private ImageView categoryImgCategoryImageB;
        private FrameLayout categoryFrameSmall2;
        private TextView categoryTvNameS2;
        private TextView categoryTvDescS2;
        private ImageView categoryImgCategoryImageS2;
        private FrameLayout categoryFrameSmall1;
        private TextView categoryTvNameS1;
        private TextView categoryTvDescS1;
        private ImageView categoryImgCategoryImageS1;

        public ViewHolderCategory(@NonNull View itemView) {
            super(itemView);
            categoryFrameBig = itemView.findViewById(R.id.category_frame_big);
            categoryTvNameB = itemView.findViewById(R.id.category_tv_nameB);
            categoryImgCategoryImageB = itemView.findViewById(R.id.category_img_categoryImageB);
            categoryFrameSmall2 = itemView.findViewById(R.id.category_frame_small2);
            categoryTvNameS2 = itemView.findViewById(R.id.category_tv_nameS2);
            categoryTvDescS2 = itemView.findViewById(R.id.category_tv_descS2);
            categoryImgCategoryImageS2 = itemView.findViewById(R.id.category_img_categoryImageS2);
            categoryFrameSmall1 = itemView.findViewById(R.id.category_frame_small1);
            categoryTvNameS1 = itemView.findViewById(R.id.category_tv_nameS1);
            categoryTvDescS1 = itemView.findViewById(R.id.category_tv_descS1);
            categoryImgCategoryImageS1 = itemView.findViewById(R.id.category_img_categoryImageS1);

        }
    }


}
