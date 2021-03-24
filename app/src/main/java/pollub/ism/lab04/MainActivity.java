package pollub.ism.lab04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private String[] savedState = new String[9];
    private  boolean[] savedEnable = new boolean[9];
    private byte whichPlayer  = 1;
    private byte turnCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillArrays();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button button;
        TextView textTurn = (TextView) findViewById(R.id.turnText);


        textTurn.setText((whichPlayer==1 ? R.string.TurnO : R.string.TurnX));

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                button = (Button) findViewById(getResources().getIdentifier("button_" + (i+1) +"_"+ (j+1), "id", this.getPackageName()));
                button.setText(savedState[i*3+j]);
                button.setEnabled(savedEnable[i*3+j]);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArray("arrayState",savedState);
        outState.putBooleanArray("arrayEnable",savedEnable);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        savedState = savedInstanceState.getStringArray("arrayState");
        savedEnable = savedInstanceState.getBooleanArray("arrayEnable");

    }

    public void buttonClick(View view){
        String nazwaPrzycisku = view.getResources().getResourceEntryName(view.getId());
        String[] pozycja = nazwaPrzycisku.substring(7).split("_");

        TextView varText = (TextView) findViewById(R.id.turnText);

        short wiersz = Short.parseShort(pozycja[0]);
        short kolumna = Short.parseShort(pozycja[1]);

        wiersz--;
        kolumna--;
        int index = wiersz*3 + kolumna;

        if(whichPlayer==1) {
            savedState[index] = "o";
            whichPlayer=2;
            varText.setText(R.string.TurnX);
        }
        else if(whichPlayer==2){
            savedState[index] = "x";
            whichPlayer=1;
            varText.setText(R.string.TurnO);
        }

        Button varButton = (Button) findViewById(view.getId());
        varButton.setText(savedState[index]);
        varButton.setEnabled(false);
        savedEnable[index]=false;

        turnCounter++;
        if(checkWin()){
            blockAll();
            varText.setText(R.string.GameOver);
        }

    }

    private void blockAll(){
        for(short i=0;i<9;i++)
            savedEnable[i]=false;
        onResume();
    }

    public void resetGame(View view){
        for(short i=0;i<9;i++){
            savedEnable[i]=true;
            savedState[i]=null;
            turnCounter=0;
        }
        onResume();
    }
    private boolean checkWin(){
        String var;
        for(int i=0;i<3;i++){

            var = savedState[i];
            if(var!=null) {
                if (savedState[i + 3] == (var) && savedState[i + 6] == (var)) {
                    Toast.makeText(this, "Wygrana " + var, Toast.LENGTH_LONG).show();
                    return true;
                }
            }

            var = savedState[3*i];
            if(var!=null) {
                    if (savedState[3 * i + 1] == (var) && savedState[3 * i + 2] == (var)) {
                        Toast.makeText(this, "Wygrana " + var, Toast.LENGTH_LONG).show();
                        return true;
                    }
            }

        }
        var = savedState[4];
        if(var!=null) {
            if (savedState[0] == (var) && savedState[8] ==(var)) {
                Toast.makeText(this, "Wygrana " + var, Toast.LENGTH_LONG).show();
                return true;
            }

            if (savedState[2] == (var) && savedState[6] ==(var)) {
                Toast.makeText(this, "Wygrana " + var, Toast.LENGTH_LONG).show();
                return true;
            }
        }

        if(turnCounter==9)
            Toast.makeText(this,"Remis",Toast.LENGTH_SHORT).show();
        return false;
    }

    private void fillArrays(){
        for(int i=0;i<9;i++)
            savedEnable[i]=false;
    }
}