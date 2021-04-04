package com.example.ceougnas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    String TAG = "game";
    int nbJeux = 4;
    int previousnbJeux = nbJeux;
    String currentColor = "green";
    String previousColor = currentColor;
    String playerName = "SARAH";
    int mise = 0;
    BlackJack game;


    private void addToPanel(LinearLayout lay, String token){
        ImageView imv = new ImageView(this);
        int resID = getResources().getIdentifier("card_"+ token.toLowerCase(), "drawable", getPackageName());
        Bitmap tempBMP = BitmapFactory.decodeResource(getResources(), resID);
        imv.setPadding(10, 10, 10, 10);
        imv.setImageBitmap(tempBMP);
        lay.addView(imv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            game = new BlackJack(nbJeux);
        } catch (EmptyDeckException e) {
            EmptyDeckDialog(e);
        }
        setContentView(R.layout.activity_main);
        MiseDialog();
    }

    public void updatePlayerPanel(){
        LinearLayout layPC = findViewById(R.id.LinearLayoutHandPlayerCard);
        TextView PlayerResult = (TextView) findViewById(R.id.textViewPR);
        TextView PlayerName = (TextView) findViewById(R.id.textViewPlayerName);
        TextView PlayerMise = (TextView) findViewById(R.id.textViewMise);

        layPC.removeAllViewsInLayout();

        PlayerName.setText(playerName+" - Pot : "+game.getCagnotte()+" $");
        PlayerMise.setText("Bet : "+mise+" $");
        PlayerResult.setText(game.getPlayerHandString());

        for (int i=0; i<game.getPlayerCardList().size(); i++){
            Card card = (Card) game.getPlayerCardList().get(i);
            String token = card.getColorName()+"_"+card.getValueSymbole();
            addToPanel(layPC, token);
        }
    }
    public void updateBankPanel(){
        LinearLayout layBC = findViewById(R.id.LinearLayoutHandBankCard);

        TextView BankResult = (TextView) findViewById(R.id.textViewBR);
        TextView BankName = (TextView) findViewById(R.id.textViewBankName);

        layBC.removeAllViewsInLayout();

        BankName.setText("BANK");
        BankResult.setText(game.getBankHandString());

        for (int i=0; i<game.getBankCardList().size(); i++){
            Card card = (Card) game.getBankCardList().get(i);
            String token = card.getColorName()+"_"+card.getValueSymbole();
            addToPanel(layBC, token);
        }
    }

    public void myConfigFunction(final View v){
        View myView =  getLayoutInflater().inflate(R.layout.dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("BlackJack Configuration");
        builder.setView(R.layout.dialog);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                previousColor = currentColor;
                if (nbJeux != previousnbJeux){
                    previousnbJeux = nbJeux;
                    game.resetDeck(nbJeux);
                }
                resetGame();
                Toast.makeText(MainActivity.this, "Configuration confirmed", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (previousColor != currentColor){
                    changeBackground(previousColor);
                }
                if (nbJeux != previousnbJeux) {
                    nbJeux = previousnbJeux;
                }

                Toast.makeText(MainActivity.this, "Configuration canceled", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        RadioGroup radioColGroup = (RadioGroup) alert.findViewById(R.id.radioColor);
        RadioButton green = (RadioButton) alert.findViewById(R.id.radioVert);
        RadioButton red = (RadioButton) alert.findViewById(R.id.radioRed);
        if (currentColor == "green"){
            green.setChecked(true);
        } else if (currentColor == "red"){
            red.setChecked(true);
        }

        radioColGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioVert) {
                    changeBackground("green");
                } else if (checkedId == R.id.radioRed) {
                    changeBackground("red");
                }
            }
        });
        EditText alertTextView = (EditText) alert.findViewById(R.id.editText);
        alertTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int value = Integer.parseInt(editable.toString());
                    if (value>0 && value <6) {
                        nbJeux = value;
                    }
                } catch (NumberFormatException | NullPointerException nfe) {
                    nbJeux = 4;
                }
            }
        });

        EditText alertPlayername = (EditText) alert.findViewById(R.id.editTextPlayerName);
        alertPlayername.setText(playerName);
        alertPlayername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    playerName = editable.toString();
                }
            }
        });
        EditText alertTextCagnotte = (EditText) alert.findViewById(R.id.editTextCagnotteI);
        alertTextCagnotte.setText(Integer.toString(game.getCagnotte()));
        alertTextCagnotte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int value = Integer.parseInt(editable.toString());
                    if (value>0) {
                        game.setCagnotte(value);
                    } else {
                        game.setCagnotte(5000);
                    }
                } catch (NumberFormatException | NullPointerException nfe) {
                    game.setCagnotte(5000);
                }
            }
        });
    }

    public void changeBackground(String newColor){
        LinearLayout LAll = findViewById(R.id.LinearLayoutAll);
        Button LB1 = findViewById(R.id.anothercard);
        Button LB2 = findViewById(R.id.nomorecards);
        Button LB3 = findViewById(R.id.reset);
        Button LB4 = findViewById(R.id.config);
        TextView LT1 = findViewById(R.id.textViewBankName);
        TextView LT2 = findViewById(R.id.textViewPlayerName);

        if (newColor == "green"){
            LAll.setBackgroundColor(Color.parseColor("#00574B"));
            LB1.setBackgroundColor(Color.parseColor("#013220"));
            LB2.setBackgroundColor(Color.parseColor("#013220"));
            LB3.setBackgroundColor(Color.parseColor("#013220"));
            LB4.setBackgroundColor(Color.parseColor("#013220"));
            LT1.setBackgroundColor(Color.parseColor("#013220"));
            LT2.setBackgroundColor(Color.parseColor("#013220"));
            currentColor = "green";
        } else if (newColor == "red"){
            LAll.setBackgroundColor(Color.parseColor("#580000"));
            LB1.setBackgroundColor(Color.parseColor("#3F0000"));
            LB2.setBackgroundColor(Color.parseColor("#3F0000"));
            LB3.setBackgroundColor(Color.parseColor("#3F0000"));
            LB4.setBackgroundColor(Color.parseColor("#3F0000"));
            LT1.setBackgroundColor(Color.parseColor("#3F0000"));
            LT2.setBackgroundColor(Color.parseColor("#3F0000"));
            currentColor = "red";
        }
    }

    public void AnotherCardFunction(View view) {
        try {
            game.playerDrawAnotherCard();
        } catch (EmptyDeckException e) {
            EmptyDeckDialog(e);
        }
        updatePlayerPanel();
    }

    public void ResetFunction(View view) {
        if (mise > 0 ){
            NoMoreCardsFunction(view);
        }
        resetGame();
    }
    public void resetGame(){
        try {
            game.reset();
        } catch (EmptyDeckException e) {
            EmptyDeckDialog(e);
        }
        LinearLayout layBR = findViewById(R.id.BankResultCards);
        LinearLayout layPR = findViewById(R.id.PlayerResultsCards);
        layPR.removeAllViewsInLayout();
        layBR.removeAllViewsInLayout();
        MiseDialog();
    }

    public void NoMoreCardsFunction(View view){
        try {
            game.bankLastTurn();
        } catch (EmptyDeckException e) {
            EmptyDeckDialog(e);
        }
        updateBankPanel();
        LinearLayout layBR = findViewById(R.id.BankResultCards);
        LinearLayout layPR = findViewById(R.id.PlayerResultsCards);
        String winnertoken = "winner";
        String loosertoken = "looser";
        String drawtoken = "draw";
        String blackjacktoken = "blackjack";
        if (game.getPlayerBest() == 21){
            game.addToCagnotte(3*mise);
            mise = 0;
            addToPanel(layPR, blackjacktoken);
        }
        if (game.getBankBest() == 21){
            addToPanel(layBR, blackjacktoken);
        }
        if(game.isPlayerWinner()){
            game.addToCagnotte(2*mise);
            mise = 0;
            addToPanel(layPR, winnertoken);
            addToPanel(layBR, loosertoken);
        } else if(game.isBankWinner()){
            if (game.getCagnotte() == 0) {
                EmptyTuneDialog(new EmptyTuneException("The player's broke !"));
            }
            mise = 0;
            addToPanel(layPR, loosertoken);
            addToPanel(layBR, winnertoken);
        } else {
            game.addToCagnotte(mise);
            mise = 0;
            addToPanel(layPR, drawtoken);
            addToPanel(layBR, drawtoken);
        }
        updateBankPanel();
        updatePlayerPanel();
    }
    public void EmptyDeckDialog(final EmptyDeckException ex){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(ex.getMessage())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        game.resetDeck(4);
                        resetGame();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    public void MiseDialog(){
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("Bet")
                .setView(R.layout.miselayout)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (mise>game.getCagnotte()){
                            mise = game.getCagnotte();
                        }
                        game.setCagnotte(game.getCagnotte()-mise);
                        updateBankPanel();
                        updatePlayerPanel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        TextView miseCagnotte = alert.findViewById(R.id.textViewMiseCagnotte);
        final TextView miseText = alert.findViewById(R.id.textViewMise);
        final SeekBar seekMise = alert.findViewById(R.id.seekBar);
        miseCagnotte.setText("Pot : "+game.getCagnotte()+" $");
        mise = seekMise.getProgress();
        miseText.setText("Bet : " +mise+ " $");

        seekMise.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mise = seekMise.getProgress();
                miseText.setText("Bet : " +mise+ " $");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void EmptyTuneDialog(final EmptyTuneException ex){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(ex.getMessage())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        game.setCagnotte(5000);
                        resetGame();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
