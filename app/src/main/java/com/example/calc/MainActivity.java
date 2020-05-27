package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import static java.lang.StrictMath.abs;

public class MainActivity extends AppCompatActivity {

    protected Button numequal, numc, numplus, numminus, nummul, numdivide,conver;
    protected TextView Cal;
    protected int state = 0;
    int minusState = 0;

    class ps {
        float tu = 0;
        float mau = 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        numequal.setEnabled(false);
        Cal.addTextChangedListener(math);
        numc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = "";
                numplus.setEnabled(true);
                numdivide.setEnabled(true);
                numminus.setEnabled(true);
                nummul.setEnabled(true);
                Cal.setText(txt);
                ps_state = 0;
            }
        });
        numequal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ps_state == 0) {
                    equal();
                } else {
                    if (ps_state == 1) {
                        New_Equal();
                    }
                }
                numplus.setEnabled(true);
                numdivide.setEnabled(true);
                numminus.setEnabled(true);
                nummul.setEnabled(true);
                conver.setEnabled(true);
                conver_temp=Cal.getText().toString();
            }
        });
    }
    private int conver_state=0;
    private String conver_temp;
    public void onClickconver(View view) {
        if(Cal.getText().toString().isEmpty()) {
            Toast.makeText(this,"Complete you math or input number first",Toast.LENGTH_LONG).show();
        }
        else
        {
            if (conver_state == 0) {
                toword t = new toword();
                String ss = t.convert(Integer.parseInt(Cal.getText().toString()));
                Cal.setText(ss);
                conver_state = 1;
            } else {
                conver_state = 0;
                Cal.setText(conver_temp);
            }
        }
    }

    public void onClickminus(View view) {
        if (minusState == 0) {
            onClickSpace(view);
        } else {
            onClick(view);
        }
    }

    private TextWatcher math = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            numequal.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    int ps_state = 0;

    public void onClick(View view) {
        if (state == 0) {
            Button btn = findViewById(view.getId());
            String text = Cal.getText().toString();
            text = text + "" + btn.getText().toString();
            Cal.setText(text);
            if (btn == findViewById(R.id.btnps)) {
                Toast.makeText(MainActivity.this, btn.getText().toString(), Toast.LENGTH_LONG).show();
                ps_state = 1;
            }
        } else {
            String txt = "";
            Cal.setText(txt);
            Button btn = findViewById(view.getId());
            String text = Cal.getText().toString();
            text = text + "" + btn.getText().toString();
            Cal.setText(text);
            state = 0;
        }
        conver_temp=Cal.getText().toString();

    }

    public void onClickSpace(View view) {
        Button btn = findViewById(view.getId());
        String text = Cal.getText().toString();
        text = text + " " + btn.getText().toString() + " ";
        Cal.setText(text);
        state = 0;
        minusState = 1;
        numplus.setEnabled(false);
        numdivide.setEnabled(false);
        nummul.setEnabled(false);
        conver.setEnabled(false);
    }


    public void Anhxa() {
        numequal = findViewById(R.id.numequal);
        numc = findViewById(R.id.Clean);
        Cal = findViewById(R.id.Textview);
        numplus = findViewById(R.id.numplus);
        numminus = findViewById(R.id.numminus);
        nummul = findViewById(R.id.nummulty);
        numdivide = findViewById(R.id.numdivide);
        conver=findViewById(R.id.conver);
    }

    public void equal() {
        String[] w = Cal.getText().toString().split(" ");
        if (w.length < 3) {
            Toast.makeText(MainActivity.this, "Error math,missing something important !!" + minusState, Toast.LENGTH_LONG).show();
        } else {
            float a = Float.parseFloat(w[0]);
            float b = Float.parseFloat(w[2]);
            String chon = w[1];
            switch (chon) {
                case "+": {
                    float t = a + b;

                    Cal.setText(checkInt(t));
                    break;
                }
                case "-": {
                    float t = a - b;

                    Cal.setText(checkInt(t));
                    break;
                }
                case "*": {
                    float t = a * b;

                    Cal.setText(checkInt(t));
                    break;
                }
                case "÷": {
                    float t = a / b;
                    Cal.setText(checkInt(t));
                }
                break;
            }
            state = 1;

            ps_state = 0;
        }
    }

    public float UCLN(ps a) {
        a.tu = abs(a.mau);
        a.mau = abs(a.mau);
        if (a.tu * a.mau == 0) return 1;
        while (a.tu != a.mau) {

            if (a.tu > a.mau) a.tu -= a.mau;
            else a.mau -= a.tu;
        }
        return a.tu;
    }

    public ps ps_Rutgon(ps a) {
        float temp = UCLN(a);
        ps b = new ps();
        b.tu = a.tu / temp;
        b.mau = a.mau / temp;
        return b;
    }

    public ps ps_Plus(ps a, ps b) {
        ps c = new ps();
        c.tu = a.tu * b.mau + b.tu * a.mau;
        c.mau = a.mau * b.mau;
        return ps_Rutgon(c);
    }

    public ps ps_Minus(ps a, ps b) {
        ps c = new ps();
        c.tu = a.tu * b.mau - b.tu * a.mau;
        c.mau = a.mau * b.mau;
        return ps_Rutgon(c);
    }

    public ps ps_Multy(ps a, ps b) {
        ps c = new ps();
        c.tu = a.tu * b.tu;
        c.mau = a.mau * b.mau;
        return ps_Rutgon(c);
    }

    public ps ps_Divide(ps a, ps b) {
        ps c = new ps();
        c.tu = a.tu * b.mau;
        c.mau = a.mau * b.tu;
        return ps_Rutgon(c);
    }

    public String convertoString(ps a) {
        if (a.mau == 1) {
            return Float.toString(a.tu);
        } else {
            return a.tu + "/" + a.mau;
        }

    }

    public void New_Equal() {
        String[] w = Cal.getText().toString().split(" ");
        ps a = new ps();
        String[] n = w[0].split("/");
        a.tu = Integer.parseInt(n[0]);
        a.mau = Integer.parseInt(n[1]);
        ps b = new ps();
        String[] m = w[2].split("/");
        b.tu = Integer.parseInt(m[0]);
        b.mau = Integer.parseInt(m[1]);
        String chon = w[1];
        switch (chon) {
            case "+": {
                ps temp;
                temp = ps_Plus(a, b);
                String ketqua = convertoString(temp);
                Cal.setText(ketqua);
                break;
            }
            case "-": {
                ps temp;
                temp = ps_Minus(a, b);
                String ketqua = convertoString(temp);
                Cal.setText(ketqua);
                break;
            }
            case "*": {
                ps temp;
                temp = ps_Multy(a, b);
                String ketqua = convertoString(temp);
                Cal.setText(ketqua);
                break;
            }
            case "÷": {
                ps temp;
                temp = ps_Divide(a, b);
                String ketqua = convertoString(temp);
                Cal.setText(ketqua);
            }
            break;
        }
    }

    public String checkInt(float a) {
        if (a % (int) a == 0) {
            String b = Integer.toString((int) a);
            return b;
        }
        return Float.toString(a);
    }
}