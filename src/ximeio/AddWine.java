package ximeio;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.geom.CubicCurve2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.sql.*;


public class AddWine extends JDialog{

    JTextField attr1,attr2,attr3,attr4,attr5,attr6;
    String     attr1Str,attr2Str,attr3Str,attr4Str,attr5Str,attr6Str,moneyStr;
    JButton cancel,ok;
    JRadioButton choice1, choice2, choice3;
    JRadioButton choiceb1, choiceb2, choiceb3;
    JCheckBox me;
    ButtonGroup colourGroup;
    DBConnection conn= new DBConnection("localhost", "root", "230358", "chemistr");
    boolean mustUpdate = true;
    private JLabel name;
    String choice;


    public  AddWine(final String customerID) {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-500)/ 2;
        final int y = (dim.height-250)/ 2;
        setLocation(x, y);        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

          ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        setSize(500, 250);
        setLayout(new GridLayout(0, 2));
        String customerName;
        ResultSet rs;

        try{
                    Statement stmt = conn.createStatement(true);
                    rs = stmt.executeQuery("select * from customer where CustomerID="+customerID);
                   // System.out.println(customerID);
                    rs.next();
                    customerName = rs.getString("Surname");
                    customerName +=" "+ rs.getString("Name");
                    //System.out.println(customerName);

        setTitle(customerName+" - ΠΡΟΣΘΗΚΗ ΚΡΑΣΙΟΥ");

        add(new JLabel("Νο ΒΑΡΕΛΙΟΥ:"));
        attr1 = new JTextField();
        add(attr1);

        add(new JLabel("ΚΙΛΑ:"));
        attr2 = new JTextField();
        add(attr2);

        add(new JLabel("ΧΡΩΜΑ:"));

        JPanel toolbar = new JPanel();
        final ButtonGroup group = new ButtonGroup();
        choice1 = new JRadioButton("ΡΟΖΕ");
        choice1.setActionCommand("ΡΟΖΕ");
        choice2 = new JRadioButton("ΚΟΚΚΙΝΟ");
        choice2.setActionCommand("ΚΟΚΚΙΝΟ");
        choice3 = new JRadioButton("ΛΕΥΚΟ");
        choice3.setActionCommand("ΛΕΥΚΟ");


        toolbar.add(choice1);
        toolbar.add(choice2);
        toolbar.add(choice3);

        group.add(choice1);
        group.add(choice2);
        group.add(choice3);


        class VoteActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
         attr3Str = group.getSelection().getActionCommand();
         //System.out.println("Selected"+choice);

      }
    };

    ActionListener alisten = new VoteActionListener( );
    choice1.addActionListener(alisten);
    choice2.addActionListener(alisten);
    choice3.addActionListener(alisten);

       // attr2 = new JTextField();
        add(toolbar);
       // System.out.println(choice);

        add(new JLabel("Me:"));
        me = new JCheckBox();
        attr4Str="ΟΧΙ";
        me.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    //System.out.println(e.getStateChange());
                    int choice = e.getStateChange();
                    if (choice == 1) attr4Str="NAI"; else if(choice==2)attr4Str="ΟΧΙ";
                }
            });
        add(me);

        add(new JLabel("ΤΥΠΟΣ ΒΑΡΕΛΙΟΥ:"));

        JPanel toolbarb = new JPanel();
        final ButtonGroup groupb = new ButtonGroup();
        choiceb1 = new JRadioButton("ΞΥΛΙΝΟ");        
        choiceb1.setActionCommand("ΞΥΛΙΝΟ");
        choiceb2 = new JRadioButton("ΠΛΑΣΤΙΚΟ");
        choiceb2.setActionCommand("ΠΛΑΣΤΙΚΟ");
        choiceb3 = new JRadioButton("INOX");
        choiceb3.setActionCommand("INOX");


        toolbarb.add(choiceb1);
        toolbarb.add(choiceb2);
        toolbarb.add(choiceb3);

        groupb.add(choiceb1);
        groupb.add(choiceb2);
        groupb.add(choiceb3);

       
        class VoteActionListenerb implements ActionListener {
        public void actionPerformed(ActionEvent evb) {
         attr5Str = groupb.getSelection().getActionCommand();
         //System.out.println("Selected"+choice);

      }
    };

    ActionListener alistenb = new VoteActionListenerb( );
    choiceb1.addActionListener(alistenb);
    choiceb2.addActionListener(alistenb);
    choiceb3.addActionListener(alistenb);

       // attr2 = new JTextField();
        add(toolbarb);
       // System.out.println(choice);


        add(new JLabel("ΠΗΓΗ ΣΤΑΦΥΛΙΩΝ:"));
        attr6 = new JTextField();
        add(attr6);

        
         }
         catch(SQLException ex) {
             System.out.println(ex.getMessage());
         }



        ok = new JButton("Καταχώρηση");
        add(ok);
        getRootPane().setDefaultButton(ok);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) throws NumberFormatException{

                
                if (attr3Str == null){
                    JOptionPane.showMessageDialog(getContentPane(), "Παρακαλώ διαλέξτε χρώμα", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    return;         
                }
                if (attr5Str == null){
                    JOptionPane.showMessageDialog(getContentPane(), "Παρακαλώ διαλέξτε τύπο βαρελιού", "Σφάλμα", JOptionPane.ERROR_MESSAGE);                    
                    return;
                }

                try{
                int nobarr = Integer.parseInt(attr1.getText());
                }
                catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(getContentPane(), "Λάθος είσοδος για Νο Βαρελιού", "Σφάλμα", JOptionPane.ERROR_MESSAGE);                    
                    return;
                }
                
                String query="insert into Wine(Added,CustomerID,NrBarrel,Kilos,Colour,Me,BarrelType,Grapesource,Money) values(CURDATE(),";
                attr1Str = attr1.getText();
                attr2Str = attr2.getText();
               
                attr6Str = attr6.getText();
                
                


                int money = 0;
                try {
                    int kilos = Integer.parseInt(attr2Str);
                    money = (int)calculateMoney(kilos);
                } catch (IOException ex) {
                    Logger.getLogger(AddWine.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(getContentPane(), "Λάθος είσοδος για ΚΙΛΑ");
                    return;
                }
                moneyStr = Integer.toString(money);               
                
                

                query+= "'"+customerID+"',";
                query+= "'"+attr1Str+"',";
                query+= "'"+attr2Str+"',";
                query+= "'"+attr3Str+"',";
                query+= "'"+attr4Str+"',";
                query+= "'"+attr5Str+"',";
                query+= "'"+attr6Str+"',";

                query+= "'"+moneyStr+"')";
                



                try{
                    Statement stmt = conn.createStatement(true);
                    stmt.executeUpdate(query);
                }
                catch(SQLException ex) {
                    System.out.println("Error wines");
                }
                
                ResultSet rs2;
                Statement stmt2 = conn.createStatement(true);
                try {
                   rs2= stmt2.executeQuery("select Money from Customer where CustomerID="+customerID);
                   rs2.next();                   
                   int oldmoney = Integer.parseInt(rs2.getString("Money"));
                   int newmoney = oldmoney + money;
                   stmt2.executeUpdate("update Customer set Money="+newmoney+",MoneyOwned="+newmoney+" where CustomerID="+customerID);
                } catch (SQLException ex) {
                    Logger.getLogger(AddWine.class.getName()).log(Level.SEVERE, null, ex);
                }


                mustUpdate = true;

                dispose();






            }
        });


        cancel = new JButton("Ακύρωση");
        add(cancel);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private double calculateMoney(int kilos) throws IOException{
        double money=0;

        try {
            FileInputStream fstream = new FileInputStream("pricelist.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            double[] price = {0,0,0,0,0,0,0,0};

            int i=0;
            while ((strLine = br.readLine()) != null)   {
                price[i++] = Double.parseDouble(strLine);

            }
           // for (i=0;i<price.length;i++) System.out.println(price[i]);
           if (kilos<=150) money = price[0];
           else if (151<kilos && kilos<=200) money = price[1];
           else if (201<kilos && kilos<=300) money = price[2];
           else if (301<kilos && kilos<=400) money = price[3];
           else if (401<kilos && kilos<=500) money = price[4];
           else if (501<kilos && kilos<=1000) money = price[5]*kilos;
           else if (1001<kilos && kilos<=2000) money = price[6]*kilos;
           else if (kilos>2001) money = price[7]*kilos;



        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddWine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return money;

    }

}
