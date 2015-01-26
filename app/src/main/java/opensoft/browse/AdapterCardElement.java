package opensoft.browse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import opensoft.com.opensoft.R;

/**
 * Created by vigneshm on 26/01/15.
 */
public class AdapterCardElement extends RecyclerView.Adapter<AdapterCardElement.CardElementHolder>{
    private List<CardElement> list;

    public AdapterCardElement(List<CardElement> list) {
        this.list = list;
    }

    public int getItemCount() {
        return list.size();
    }
    public void removeFromList(int pos){
        list.remove(pos);
        notifyItemRemoved(pos);
    }
    public void onBindViewHolder(CardElementHolder contactViewHolder, int i) {
        CardElement cardElement = list.get(i);
        contactViewHolder.title.setText(cardElement.title);
        contactViewHolder.content.setText(cardElement.content);
        contactViewHolder.icon.setImageResource(cardElement.icon_ref);
    }

    public CardElementHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_element, viewGroup, false);

        return new CardElementHolder(itemView);
    }

    public static class CardElementHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView content;
        protected ImageView icon;

        public CardElementHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.title_card);
            content = (TextView)  v.findViewById(R.id.content_card);
            icon = (ImageView)  v.findViewById(R.id.icon_card);
        }
    }
}
