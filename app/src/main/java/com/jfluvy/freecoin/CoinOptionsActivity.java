package com.jfluvy.freecoin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CoinOptionsActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Coin coin;
    private List<Purchase> purchaseList;

    TextView nameView;              //1
    TextView iconView;              //2
    TextView quantityView;          //3
    TextView currentlyPView;        //4
    TextView currentlyVView;        //5
    TextView averagePView;          //6
    TextView averageVView;          //7
    TextView wallet1View;           //8
    TextView wallet2View;           //9
    TextView wallet3View;           //10
    TextView wallet4View;           //11
    TextView wallet1NameView;       //12
    TextView wallet2NameView;       //13
    TextView wallet3NameView;       //14
    TextView wallet4NameView;       //15
    TextView coinMarketCapView;     //16

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_options);

        coin=(Coin) getIntent().getSerializableExtra("Coin");

        purchaseList=new ArrayList<>();

        db.collection(coin.getNon()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                //purchaseList.clear();
                for (DocumentSnapshot doc: documentSnapshots){
                    Purchase purchase=doc.toObject(Purchase.class);
                    purchaseList.add(purchase);
                }
            }
        });

        //Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();

        nameView= findViewById(R.id.editNameView);
        iconView= findViewById(R.id.editIconView);
        quantityView=findViewById(R.id.editQuantityView);
        currentlyPView=findViewById(R.id.editCurrentlyPView);
        currentlyVView=findViewById(R.id.editCurrentlyVView);
        averagePView=findViewById(R.id.editAveragePView);
        averageVView=findViewById(R.id.editAverageVView);
        wallet1View=findViewById(R.id.editWallet1View);
        wallet2View=findViewById(R.id.editWallet2View);
        wallet3View=findViewById(R.id.editWallet3View);
        wallet4View=findViewById(R.id.editWallet4View);
        wallet1NameView=findViewById(R.id.editWallet1NameView);
        wallet2NameView=findViewById(R.id.editWallet2NameView);
        wallet3NameView=findViewById(R.id.editWallet3NameView);
        wallet4NameView=findViewById(R.id.editWallet4NameView);
        coinMarketCapView=findViewById(R.id.editCoinMarketCapView);

        nameView.setText(coin.getName());
        iconView.setText(coin.getNon());
        quantityView.setText(Double.toString(coin.getQuantity()));
        currentlyPView.setText(Double.toString(coin.getPrice())+"$");
        currentlyVView.setText(Double.toString(coin.getValue())+"$");
        //
        //
        //
        //
        //
        //
        //
        //
        //
        //
        coinMarketCapView.setText(coin.getCoinMarketCap());


    }

    public void onClickSave(View view){
        coin.setaPrice(CalculateAverage());
        coin.setaValue(coin.getaPrice()*coin.getQuantity());
        averagePView.setText(Double.toString(coin.getaPrice()));
        averageVView.setText(Double.toString(coin.getaValue()));
    }

    public void onClickPurchase(View view){

    }

    private Double CalculateAverage(){
        Double quantCoin=0.0;
        Double priceCoin=0.0;
        Double cAverage;
        for (Purchase doc1:purchaseList){
            quantCoin=quantCoin+ doc1.getQuantity();
            priceCoin=priceCoin+(doc1.getPrice()*doc1.getQuantity());
        }
        cAverage=priceCoin/quantCoin;

        return cAverage;
    }
}