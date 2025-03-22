package com.example.stars.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.stars.R;
import com.example.stars.beans.Star;
import com.example.stars.service.StarService;

import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {
    private static final String TAG = "StarAdapter";
    private List<Star> stars;         // Full list of stars
    private List<Star> starsFilter;   // Filtered list
    private Context context;
    private NewFilter mfilter;

    public StarAdapter(Context context, List<Star> stars) {
        this.context = context;
        this.stars = stars != null ? stars : new ArrayList<>();  // ✅ Prevent null crashes
        this.starsFilter = new ArrayList<>(this.stars);  // ✅ Always initialize filtered list
        this.mfilter = new NewFilter(this);  // ✅ Initialize filter
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.star_item, viewGroup, false);
        final StarViewHolder holder = new StarViewHolder(v);
        holder.itemView.setOnLongClickListener(v1 -> {
            int position = holder.getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return false;
            }

            Star star = starsFilter.get(position);

            // Show confirmation dialog before deleting
            new AlertDialog.Builder(context)
                    .setTitle("Confirmer Suppression")
                    .setMessage("Est ce que vous supprimer" + star.getName() + "?")
                    .setPositiveButton("Confirmer", (dialog, which) -> {
                        StarService.getInstance().delete(star);
                        starsFilter.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, starsFilter.size());
                    })
                    .setNegativeButton("Annuler", null)
                    .show();

            return true; // Indicates the long press event was handled
        });
        holder.itemView.setOnClickListener(view -> {
            int position = holder.getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }

            // Get the selected Star object
            Star star = starsFilter.get(position);

            // Create a popup layout
            View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null, false);
            final ImageView img = popup.findViewById(R.id.img);
            final RatingBar bar = popup.findViewById(R.id.ratingBar);
            final TextView idss = popup.findViewById(R.id.idss);

            // Load the existing image
            img.setImageDrawable(holder.img.getDrawable());
            bar.setRating(holder.stars.getRating());
            idss.setText(String.valueOf(star.getId()));

            new AlertDialog.Builder(context)
                    .setTitle("Update Rating")
                    .setMessage("Give a rating between 1 and 5:")
                    .setView(popup)
                    .setPositiveButton("Save", (dialog, which) -> {
                        float newRating = bar.getRating();


                        star.setStar(newRating);


                        for (Star originalStar : stars) {
                            if (originalStar.getId() == star.getId()) {
                                originalStar.setStar(newRating);
                                break;
                            }
                        }


                        notifyItemChanged(position);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder starViewHolder, int i) {
        Star star = starsFilter.get(i);
        Glide.with(context)
                .asBitmap()
                .load(star.getImg())
                .apply(new RequestOptions().override(100, 100))
                .into(starViewHolder.img);
        starViewHolder.name.setText(star.getName().toUpperCase());
        starViewHolder.stars.setRating(star.getStar());
        starViewHolder.idss.setText(String.valueOf(star.getId()));
    }

    @Override
    public int getItemCount() {
        return starsFilter != null ? starsFilter.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    public class StarViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        ImageView img;
        TextView name;
        RatingBar stars;
        RelativeLayout parent;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    public class NewFilter extends Filter {
        private RecyclerView.Adapter adapter;

        public NewFilter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Star> filteredList = new ArrayList<>();
            final FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(stars);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Star star : stars) {
                    if (star.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(star);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter.clear();
            if (filterResults.values != null) {
                starsFilter.addAll((List<Star>) filterResults.values);
            }
            adapter.notifyDataSetChanged();
        }
    }
}