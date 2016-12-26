package ximeio;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.CubicCurve2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;


public class AddAnalysis extends JDialog{

    private JTextField t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11;
    String nr,baumeS,tempBaumeS,alcoholS,tempAlcoholS,AlcoholCS,phS,oxyS,totSulfS,freSulfS,AS,FeS,colour;
    double baume,tempBaume,alcohol,tempAlcohol,AlcoholC,ph,oxy,totSulf,freSulf,A,Fe,baumeC,finalAlcohol;
    double addWater,addSugar,TAcid,CAcid,Me,Lux,Caco3,Tanine,Vitamin;
    JButton cancel,ok,clear;
    JRadioButton choice1, choice2, choice3;
    ButtonGroup group;
    DBConnection conn;
    boolean mustUpdate = true;
    final double[] params=null;
    int kilos;



    public  AddAnalysis(final String WineID) {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-400)/ 2;
        final int y = (dim.height-500)/ 2;
        setLocation(x, y);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(400, 500);
        setLayout(new GridLayout(0, 2));
        setTitle("Καταχώρηση Νεας Ανάλυσης");
        conn = new DBConnection("localhost", "root", "230358", "chemistr");

           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        add(new JLabel("ΑΡΙΘΜΟΣ ΑΝΑΛΥΣΗΣ:"));
        JPanel toolbar = new JPanel();        
        final ButtonGroup group = new ButtonGroup();
        nr="1"; // Default
        choice1 = new JRadioButton("1",true);
        choice1.setActionCommand("1");
        choice2 = new JRadioButton("2");
        choice2.setActionCommand("2");
        choice3 = new JRadioButton("3");
        choice3.setActionCommand("3");


        toolbar.add(choice1);
        toolbar.add(choice2);
        toolbar.add(choice3);

        group.add(choice1);
        group.add(choice2);
        group.add(choice3);


        class VoteActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
         nr = group.getSelection().getActionCommand();
         //System.out.println("Selected"+choice);

      }
    };

    ActionListener alisten = new VoteActionListener( );
    choice1.addActionListener(alisten);
    choice2.addActionListener(alisten);
    choice3.addActionListener(alisten);

       // attr2 = new JTextField();
     add(toolbar);

     FileInputStream fstream=null;
        try {
            fstream = new FileInputStream("temp.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            String[] t = {"","","","","","","","","",""};

            int i=0;
        try {
            while ((strLine = br.readLine()) != null) {
                t[i++] = (strLine);
            }
            fstream.close();
        } catch (IOException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }


        add(new JLabel("Baume:"));
        t1 = new JTextField(t[0]);
        add(t1);

        add(new JLabel("Baume temp:"));
        t2 = new JTextField(t[1]);
        add(t2);

        add(new JLabel("Alcohol:"));
        t3 = new JTextField(t[2]);
        add(t3);

        add(new JLabel("Alcohol temp:"));
        t4 = new JTextField(t[3]);
        add(t4);

      //  add(new JLabel("Alcohol Corrected:"));
      //  t5 = new JTextField(t[4]);
      //  add(t5);

        add(new JLabel("pH:"));
        t6 = new JTextField(t[4]);
        add(t6);

        add(new JLabel("Oxy:"));
        t7 = new JTextField(t[5]);
        add(t7);

        add(new JLabel("Total Sulfur:"));
        t8 = new JTextField(t[6]);
        add(t8);

        add(new JLabel("Free Sulfur:"));
        t9 = new JTextField(t[7]);
        add(t9);

        add(new JLabel("A:"));
        t10 = new JTextField(t[8]);
        add(t10);

        add(new JLabel("Fe:"));
        t11 = new JTextField(t[9]);
        add(t11);

        ok = new JButton("Καταχώρηση");
        add(ok);
        getRootPane().setDefaultButton(ok);


        //Load parameters for results

        FileInputStream fstreamp=null;
        try {
            fstreamp = new FileInputStream("params.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }
            DataInputStream inp = new DataInputStream(fstreamp);
            BufferedReader brp = new BufferedReader(new InputStreamReader(inp));
            String strLinep;
            String[] paramsS = {"","","","","","","","","","","","","","","","","","","","","","",""};

            int j=0;
        try {
            while ((strLinep = brp.readLine()) != null) {
                paramsS[j++] = (strLinep);
            }
            fstreamp.close();
        } catch (IOException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }

        final double[] params = new double[23];

        for(int l=0;l<23;l++){
            params[l] = Double.parseDouble(paramsS[l]);
            //System.out.println(params[l]);
        }
       


        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                
                baumeS = t1.getText().replace(",", ".");
                tempBaumeS = t2.getText().replace(",", ".");
                alcoholS = t3.getText().replace(",", ".");
                tempAlcoholS = t4.getText().replace(",", ".");
                phS = t6.getText().replace(",", ".");
                oxyS = t7.getText().replace(",", ".");
                totSulfS = t8.getText().replace(",", ".");
                freSulfS = t9.getText().replace(",", ".");
                AS = t10.getText().replace(",", ".");
                FeS = t11.getText().replace(",", ".");


                FileWriter fw=null;
                try {
                    fw = new FileWriter("temp.txt");
                } catch (IOException ex) {
                    Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
                }
                BufferedWriter bw = new BufferedWriter(fw);
                try {
                        bw.write(baumeS+"\n");
                        bw.write(tempBaumeS+"\n");
                        bw.write(alcoholS+"\n");
                        bw.write(tempAlcoholS+"\n");
                       // bw.write(AlcoholCS+"\n");
                        bw.write(phS+"\n");
                        bw.write(oxyS+"\n");
                        bw.write(totSulfS+"\n");
                        bw.write(freSulfS+"\n");
                        bw.write(AS+"\n");
                        bw.write(FeS);

                        bw.close();
                        fw.close();

                } catch (IOException ex) {
                    Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println("Analysis: 3"+nr);

                //Epe3ergasia edw

                baume = Double.parseDouble(baumeS);
                tempBaume = Double.parseDouble(tempBaumeS);
                alcohol = Double.parseDouble(alcoholS);
                tempAlcohol = Double.parseDouble(tempAlcoholS);
                
                ph = Double.parseDouble(phS);
                oxy = Double.parseDouble(oxyS);
                totSulf = Double.parseDouble(totSulfS);
                freSulf = Double.parseDouble(freSulfS);
                A = Double.parseDouble(AS);
                Fe = Double.parseDouble(FeS);
             
                if (alcohol > 15.9){
                       JOptionPane.showMessageDialog(getContentPane(), "Εισάγετε alcohol < 15.9","Σφάλμα",JOptionPane.ERROR_MESSAGE);
                       return;
                }
                if (tempAlcohol < 15 || tempAlcohol > 27){
                    JOptionPane.showMessageDialog(getContentPane(), "Εισάγετε Θερμοκρασία 15 < T < 27","Σφάλμα" ,JOptionPane.ERROR_MESSAGE );
                    return;
                }                
                if (baume != 0){
                    if (tempBaume>20)
                        baumeC = (tempBaume-20)*0.05+baume;
                    else
                        baumeC = baume - (20-tempBaume)*0.05;
                }
                else
                    baumeC = 0;
                
                

                //Apey8eias ba8mos apo baume an dothei alcohol =0 tempalcohol=0
                if (nr.equals("1") && (alcohol == 0)){
                    AlcoholC = calculateAlcoholFromBaume(alcohol,baumeC);

                }

                else{
               AlcoholCS =  correctAlcohol(tempAlcoholS,alcoholS);
               AlcoholC = Double.parseDouble(AlcoholCS);
                //System.out.println("Corrected:"+AlcoholC);
                }

                oxy *= 1.5;
                totSulf *= 25.6;
                freSulf *= 25.6;
                A =(0.19*A)/2;
                finalAlcohol = baumeC + AlcoholC;
               // System.out.println("WIne id ="+WineID);

                //get kilos of barrel

                Statement stmt1 = conn.createStatement(true);
                try {
                    ResultSet rs = stmt1.executeQuery("select Kilos,Colour from Wine where wineID = " + WineID);
                    rs.next();                    
                    kilos = Integer.parseInt(rs.getString(1));
                    colour = rs.getString(2);


                } catch (SQLException ex) {
                    Logger.getLogger(AddAnalysis.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Calculate results

                if (nr.equals("1")){
                    //Dior8wsh alcohol
                    if(finalAlcohol < params[0])
                        addSugar = (params[1] - finalAlcohol)*1.95*kilos/100;
                    if (finalAlcohol > params[2])
                        addWater = kilos*((finalAlcohol - params[3])/params[3]);
                    if (baumeC < 5)
                        Me = params[14]*kilos/100;
                    else if (baumeC >= 5 && baumeC <= 8)
                        Me  = params[15]*kilos/100;
                    else
                        Me = params[16]*kilos/100;


                }
                else if (nr.equals("2")){
                     if (finalAlcohol > params[2])
                        addWater = kilos*(finalAlcohol - params[3])/params[3];
                     if (totSulf < params[13])
                        Me = (params[13] - totSulf)*2*kilos/1000;
                     else
                         Me = 0;
                     Lux = (6.5/100)*kilos;
                }
                else {
                    if (totSulf < params[13])
                        Me = (params[13] - totSulf)*2*kilos/1000;
                    else
                        Me=0;
                    Lux = (6.5/100)*kilos;
                }

                //Dior8wsh o3ythtas (1 +2 +3)
                double oxyFinal;
                double addcaco = 0;
                double maxCaco1,maxCaco2;
                double maxC,maxT;
                maxT = 200*kilos/100;
                maxC = 50*kilos/100;
                maxCaco1 = 134*kilos/100;
                maxCaco2 = 200*kilos/100;
                if(colour.equals("ΡΟΖΕ") || colour.equals("ΚΟΚΚΙΝΟ")) oxyFinal = params[4];
                        else    oxyFinal = params[17];
                //System.out.println(oxyFinal);

                    if (oxy <= oxyFinal) {
                        double addacidT = (oxyFinal-oxy)*kilos*1.5 + addWater*5;
                        double addacidC = (oxyFinal-oxy)*kilos + addWater*5;

                        if (ph < params[5]){
                            CAcid = 0;
                            TAcid = 0;
                        }
                        else if(ph >= params[6] && ph <= params[7]){
                            CAcid = addacidC;
                            TAcid = 0;
                        }
                        else if(ph >= params[8] && ph <= params[9]){
                            CAcid = addacidC/2;
                            TAcid = addacidT/2;
                        }
                        else if(ph > params[10]){
                            CAcid = 0;
                            TAcid = addacidT;
                        }
                        if (CAcid > maxC) CAcid = maxC;
                        if (TAcid > maxT) TAcid = maxT;
                    }
                    else if (oxy >= params[11]){
                        if (nr.equals("1")){
                            if(ph < params[18])
                                addcaco = (oxy - params[11])*0.67*kilos;
                            if (addcaco > maxCaco1) addcaco = maxCaco1;
                        }
                        else{
                            if ((A > params[19]) && (ph < params[20]))
                            addcaco = (oxy - params[11])*0.67*kilos;
                            if (addcaco > maxCaco2) addcaco = maxCaco2;

                    }
                        
                    }
                

                
                addWater = (double)Math.round(addWater * 100) / 100;
                addSugar = (double)Math.round(addSugar * 100) / 100;
                baumeC = (double)Math.round(baumeC * 10) / 10;
                AlcoholC = (double)Math.round(AlcoholC * 10) / 10;
                finalAlcohol = (double)Math.round(finalAlcohol * 10) / 10;
                ph = (double)Math.round(ph * 100) / 100;
                oxy = (double)Math.round(oxy * 100) / 100;
                A = (double)Math.round(A * 10) / 10;
                Fe = (double)Math.round(Fe * 100) / 100;
                Lux = (double)Math.round(Lux * 10) / 10;
                Tanine = (double)Math.round(Tanine * 10) / 10;                
                totSulf = (double)Math.round(totSulf);
                freSulf = (double)Math.round(freSulf);
                TAcid = (double)Math.round(TAcid);
                CAcid = (double)Math.round(CAcid);
                Me = (double)Math.round(Me);
                Caco3 = (double)Math.round(Caco3);
                Vitamin = (double)Math.round(Vitamin);








                
                 

                
                String query="insert into Analysis(AnalysisNr,AnalysisDate,WineID,BaumeC,AlcoholC,FinalAlcohol,Ph,Oxy,TotalSulfur,FreeSulfur,A,AddWater,AddSugar,TAcid,Cacid,Me,Lux,Caco3,Tanine,Vitamin,Fe) values('"+
                        nr+"',"+"NOW()"+",'"+WineID+"','"+baumeC+"','"+AlcoholC+"','"+finalAlcohol+"','"+ph+"','"+oxy+"','"+totSulf+"','"+freSulf+"','"+A+"','"+addWater+"','"+addSugar+"','"+TAcid+"','"+CAcid+"','"+Me+"','"+Lux+"','"+Caco3+"','"+Tanine+"','"+Vitamin+"','"+Fe+"')";
                        



                try{
                    Statement stmt = conn.createStatement(true);
                    stmt.executeUpdate(query);
                }
                catch(SQLException ex) {
                    System.out.println(ex.getMessage());
                }


                mustUpdate = true;

                dispose();

            }
        });        

        clear = new JButton("Εκκαθάριση");        
        add(clear);
        
        clear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                t1.setText("");
                t2.setText("");
                t3.setText("");
                t4.setText("");
                t6.setText("");
                t7.setText("");
                t8.setText("");
                t9.setText("");
                t10.setText("");
                t11.setText("");

            }
        });


    }

    public String correctAlcohol (String tmp,String alco){

        FileInputStream fstream=null;
        try {
            fstream = new FileInputStream("alcohol_table.txt");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine=null;
            String[][] alc = new String[200][200];

            int i=0,j=0;
        try {
            while ((strLine = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(strLine);
                while (st.hasMoreTokens()) {
                    alc[i][j++] = st.nextToken();
                 }
                i++;
                j=0;
            }
            fstream.close();
        } catch (IOException ex) {
            //Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }

        j=0;
        while (!alc[0][j++].equals(tmp)) {};
            int tmpf = j-1;

        int k=0 ;
        while (!alc[k++][0].equals(alco)) {};
            int alcf = k-1;
            //System.out.println("Alc"+alc[alcf][tmpf]);

        return (alc[alcf][tmpf]);
    }

    public double calculateAlcoholFromBaume(double alcohol,double baume){
        FileInputStream fstream=null;
        try {
            fstream = new FileInputStream("baume.txt");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine=null;
            String[][] table = new String[50][2];

            int i=0,j=0;
        try {
            while ((strLine = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(strLine);
                while (st.hasMoreTokens()) {
                    table[i][j++] = st.nextToken();                   
                 }
                i++;
                j=0;
            }
           // System.out.println(table[0][0]+"DDDDD");
            fstream.close();
        } catch (IOException ex) {
            //Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }

        int k=0 ;
        int baumef;
        while (!table[k++][0].equals(Double.toString(baume))) {};
            baumef = k-1;
            //System.out.println("Alc"+alc[alcf][tmpf]);

        return (Double.parseDouble(table[baumef][1]));
    }



}
