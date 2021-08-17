package com.example.summerdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter <SliderAdapter.SliderViewHolder> {
    private List<SliderItems> sliderItems;
    private ViewPager2 viewPager2;

    SliderAdapter(List <SliderItems>sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder (
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.pager_item, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewHolder holder, int position) {
        Glide.with(holder.itemView).load(sliderItems.get(position).getImage()).into(holder.imageView);
        //holder.setImage(sliderItems.get(position));
        holder.title.setText(sliderItems.get(position).getTitle());
        holder.category.setText(sliderItems.get(position).getCategory());
        holder.author.setText(sliderItems.get(position).getAuthor());
        holder.desc.setText(sliderItems.get(position).getDesc());
        holder.tag.setText(sliderItems.get(position).getTag());
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView title;
        public TextView category;
        public TextView author;
        public TextView desc;
        public TextView tag;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_today);
            title = itemView.findViewById(R.id.text_title_today);
            category = itemView.findViewById(R.id.text_category_today);
            author = itemView.findViewById(R.id.text_author_today);
            desc = itemView.findViewById(R.id.text_desc_today);
            tag = itemView.findViewById(R.id.text_tag_today);
        }
    }
}