package ximeio;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


public class Parameters extends JFrame{

    JLabel l1,l2,l3,l4,l5,l6,l7,l8;
    JTextField t1,t2,t3,t4,t5,t6,t7,t8;
    JLabel[] label;
    JTextField[] text;
    JToolBar toolbar;

    public Parameters(){

        setLayout(new BorderLayout());
        setTitle("ΡΥΘΜΙΣΗ ΠΑΡΑΜΕΤΡΩΝ");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
          ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-400)/ 2;
        final int y = (dim.height-600)/ 2;
        setLocation(x, y);

        JPanel prices = new JPanel();
        JPanel analysis = new JPanel();

        prices.setLayout(new GridLayout(0, 2));
        
        FileInputStream fstream=null;
        try {
            fstream = new FileInputStream("pricelist.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            String[] price = {"","","","","","","",""};

            int i=0;
        try {
            while ((strLine = br.readLine()) != null) {
                price[i++] = (strLine);
                //System.out.println(strLine);
            }
            fstream.close();
        } catch (IOException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }

         FileInputStream fstreamp=null;
        try {
            fstreamp = new FileInputStream("params.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }
            DataInputStream inp = new DataInputStream(fstreamp);
            BufferedReader brp = new BufferedReader(new InputStreamReader(inp));
            String strLinep;
            String[] params = {"","","","","","","","","","","","","","","","","","","","","","",""};

            int j=0;
        try {
            while ((strLinep = brp.readLine()) != null) {
                params[j++] = (strLinep);
            }
            fstreamp.close();
        } catch (IOException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }
         

        l1 = new JLabel("kilos<=150");
        t1 = new JTextField(price[0]);
        prices.add(l1);
        prices.add(t1);

        l2 = new JLabel("151<=kilos<=200");
        t2 = new JTextField(price[1]);
        prices.add(l2);
        prices.add(t2);

        l3 = new JLabel("201<=kilos<=300");
        t3 = new JTextField(price[2]);
        prices.add(l3);
        prices.add(t3);

        l4 = new JLabel("301<=kilos<=400");
        t4 = new JTextField(price[3]);
        prices.add(l4);
        prices.add(t4);

        l5 = new JLabel("401<=kilos<=500");
        t5 = new JTextField(price[4]);
        prices.add(l5);
        prices.add(t5);

        l6 = new JLabel("501<=kilos<=1000 (price/kg)");
        t6 = new JTextField(price[5]);
        prices.add(l6);
        prices.add(t6);

        l7 = new JLabel("1001<=kilos<=2000 (price/kg)");
        t7 = new JTextField(price[6]);
        prices.add(l7);
        prices.add(t7);

        l8 = new JLabel("kilos>2000 (price/kg)");
        t8 = new JTextField(price[7]);
        prices.add(l8);
        prices.add(t8);

        JButton ok = new JButton("Καταχωρηση");
        prices.add(ok);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FileWriter fw=null;
                try {
                    fw = new FileWriter("pricelist.txt");
                } catch (IOException ex) {
                    Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
                }
                BufferedWriter bw = new BufferedWriter(fw);
                try {



                        bw.write(t1.getText()+"\n");
                        bw.write(t2.getText()+"\n");
                        bw.write(t3.getText()+"\n");
                        bw.write(t4.getText()+"\n");
                        bw.write(t5.getText()+"\n");
                        bw.write(t6.getText()+"\n");
                        bw.write(t7.getText()+"\n");
                        bw.write(t8.getText());
                        


                        bw.close();
                        fw.close();
                        dispose();
                } catch (IOException ex) {
                    Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });



        JButton cancel = new JButton("Ακύρωση");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        prices.add(cancel);


        analysis.setLayout(new GridLayout(0, 2));
        label = new JLabel[23];
        text = new JTextField[23];

        label[0] = new JLabel("Minimum Alcohol (for correction)");
        label[1] = new JLabel("Minimum Alcohol final");
        label[2] = new JLabel("Maximum Alcohol (for correction)");
        label[3] = new JLabel("Maximum Alcohol final");
        label[4] = new JLabel("Min oxy for ROZE/KOKKINO");
        label[5] = new JLabel("Max Ph for no Addacid");
        label[6] = new JLabel("only citric ph (#-");
        label[7] = new JLabel("#)");
        label[8] = new JLabel("half acids ph(#-");
        label[9] = new JLabel("#)");
        label[10] = new JLabel("Only tartaric min ph");
        label[11] = new JLabel("max Oxy");
        label[12] = new JLabel("Unused");
        label[13] = new JLabel("Me final (g)");
        label[14] = new JLabel("addme (gr/kg) if Be <5");
        label[15] = new JLabel("addme (gr/kg) if Be [5-8]");
        label[16] = new JLabel("addme (gr/kg) if Be >8");
        label[17] = new JLabel("Min oxy for LEUKO");
        label[18] = new JLabel("max ph for addcaco 1st analysis");
        label[19] = new JLabel("min A for  addcaco 2,3 Analysis");
        label[20] = new JLabel("AND max ph for addcaco 2,3 analysis");
        label[21] = new JLabel("Print parameter 1");
        label[22] = new JLabel("Print parameter 2");
        
       
        
        
        
        
        
        
        for (i=0;i<23;i++){
        try{        
        text[i] = new JTextField(params[i]);       
        analysis.add(label[i]);
        analysis.add(text[i]);
            }
        catch(NullPointerException ex){
            ex.getMessage();
        }
     }

        JButton ok2 = new JButton("Καταχώρηση");
        analysis.add(ok2);
        ok2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                    FileWriter fw2=null;
                try {
                    fw2 = new FileWriter("params.txt");
                } catch (IOException ex) {
                    Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
                }
                BufferedWriter bw2 = new BufferedWriter(fw2);
                try {


                    for (int k=0;k<23;k++){
                        bw2.write(text[k].getText()+"\n");
                    }



                        bw2.close();
                        fw2.close();
                        dispose();
                } catch (IOException ex) {
                    Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton cancel2 = new JButton("Ακύρωση");
        analysis.add(cancel2);
        cancel2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        });




        JTabbedPane p = new JTabbedPane();
        p.addTab("Οικονομικα", prices);       
        p.addTab("Παράμετροι Ανάλυσης", analysis);


        add(p,BorderLayout.CENTER);



    }

}
