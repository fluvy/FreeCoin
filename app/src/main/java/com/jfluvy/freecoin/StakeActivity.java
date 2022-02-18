package com.jfluvy.freecoin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.err;

public class StakeActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<Coin> coinList;
    private Adapter adapter;
    private int quantCoin=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stake);

        coinList=new ArrayList<>();

        db.collection("coins").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                //coinList.clear();
                for (DocumentSnapshot doc : documentSnapshots){
                    Coin coin = doc.toObject(Coin.class);
                    coin.setIcon(doc.getId());
                    coinList.add(coin);
                }
                adapter.notifyDataSetChanged();

            }
        });
        



        //definicions per al RecyclerView
        RecyclerView recyclerViewStake=findViewById(R.id.recyclerViewStake);
        recyclerViewStake.setLayoutManager(new LinearLayoutManager(this));

        adapter=new Adapter();
        recyclerViewStake.setAdapter(adapter);

    }

    //creem clase ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView nonView;
        TextView nameView;
        TextView quantityView;
        TextView priceView;
        TextView valueView;
        //ImageView iconView;

        public ViewHolder(View itemView){
            super(itemView);
            this.nonView=itemView.findViewById(R.id.nonView);
            this.nameView=itemView.findViewById(R.id.nameView);
            this.quantityView=itemView.findViewById(R.id.quantityView);
            this.priceView=itemView.findViewById(R.id.priceView);
            this.valueView=itemView.findViewById(R.id.valueView);
            //this.iconView=itemView.findViewById(R.id.iconView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    onClickCoin(pos);
                }
            });

        }
    }

    //creem clase Adapter
    class Adapter extends RecyclerView.Adapter<ViewHolder>{

        @Override
        public int getItemCount() { return coinList.size(); }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView=getLayoutInflater().inflate(R.layout.coin_view,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Coin coinItem=coinList.get(position);
            holder.nonView.setText(coinItem.getNon());
            holder.nameView.setText(coinItem.getName());
            holder.quantityView.setText(Double.toString(coinItem.getQuantity()));
            holder.priceView.setText(Double.toString(coinItem.getPrice()));
            holder.valueView.setText(Double.toString(coinItem.getValue()));
            //holder.iconView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
            //holder.iconView.setVisibility(View.GONE);
        }


    }

    public void onClickRef(View view){
        Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
        for(Coin coin1 : coinList){
            if(coin1.getCoinMarketCap().equals("0")){

            }
            else {
                getPrice(coin1.getCoinMarketCap(),coin1.getIcon());
            }
        }
        //getPrice(coinList.get(1).getCoinMarketCap(),coinList.get(1).getIcon());
    }

    public void onClickAdd(View view){
        Toast.makeText(this,"has clicat",Toast.LENGTH_LONG).show();
        final AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView =inflater.inflate(R.layout.alert_dialog_coin,null);
        dialogBuilder.setView(dialogView);

        //omplir valors
        TextView diagNonView=(TextView) dialogView.findViewById(R.id.diagNonView);
        TextView diagNameView=(TextView) dialogView.findViewById(R.id.diagNameView);
        TextView diagPriceView=(TextView) dialogView.findViewById(R.id.diagPriceView);
        TextView diagQuantityView=(TextView) dialogView.findViewById(R.id.diagQuantityView);
        TextView diagDateView=(TextView) dialogView.findViewById(R.id.diagDateView);


        final AlertDialog alertDialog=dialogBuilder.create();
        alertDialog.show();

        //boto per a cancelar AlertDialog
        Button cancel_btn=(Button)dialogView.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //boto per a augmentar cripto
        Button add_btn1=(Button)dialogView.findViewById(R.id.add_btn1);
        add_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String non=diagNonView.getText().toString();
                final String name=diagNameView.getText().toString();
                final Double price=Double.parseDouble(diagPriceView.getText().toString());
                final Double quantity=Double.parseDouble(diagQuantityView.getText().toString());
                final int date=Integer.parseInt(diagDateView.getText().toString());
                final Double value=quantity*price;
                final String icon="sdf";
                final Coin coin1=new Coin(non,name,quantity,price,value,icon);
                Purchase date1=new Purchase(date,quantity,price);
                boolean n=false;

                //afegim cripto al firebase
                for(Coin coin2 : coinList){
                    if(coin2.getNon().equals(non)){
                    //    coinList.get(n).setQuantity(coinList.get(n).getQuantity()+coinList1.get(0).getQuantity());
                    db.collection("coins").document(coin2.getIcon()).update("quantity",coin2.getQuantity()+coin1.getQuantity());
                    n=true;
                    }
                }
                if(!n) {
                    db.collection("coins").add(coin1);
                }
                db.collection(coin1.getNon()).add(date1);
                alertDialog.dismiss();
                adapter.notifyDataSetChanged();
                finish();
            }
        });

    }


    //permetem cliclar el layout
    private void onClickCoin(int pos){
        quantCoin=1;
        Toast.makeText(this, "has clicat" + pos, Toast.LENGTH_SHORT).show();
        Intent intent =new Intent(this, CoinOptionsActivity.class);
        intent.putExtra("Coin",coinList.get(pos));
        startActivity(intent);
    }

    private void getPrice(String url,String id){

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                String newPrice;
                Double newPrice1;
                try {
                    Document doc =Jsoup.connect(url).get();
                    Element element=doc.getElementsByClass("priceValue").first();
                    builder.append(element.text());
                    newPrice=builder.toString();
                    newPrice1= Double.valueOf(newPrice.replace("$","").replace(",",""));
                }
                catch (IOException e){
                    builder.append("ERROR :");
                    newPrice1=0.0;
                }
                Double finalNewPrice = newPrice1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //adjuntar
                        db.collection("coins").document(id).update("price", finalNewPrice);
                        Toast.makeText(StakeActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }


}

