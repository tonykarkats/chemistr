package ximeio;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.Vector;
import java.awt.event.*;


public class AddCustomer extends JDialog{

    private    JTextField attr1,attr2,attr3,attr4,attr6,attr7,attr8,attr9,attr10;
    private JComboBox attr5;
    String     attr1Str,attr2Str,attr3Str,attr4Str,attr5Str,attr6Str,attr7Str,attr8Str,attr9Str,attr10Str;
    JButton cancel,ok;
    DBConnection conn;
    boolean mustUpdate = true,other=false;

   // private String[] villageNames = null;
    //{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};

   

    public  AddCustomer() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-400)/ 2;
        final int y = (dim.height-500)/ 2;
        setLocation(x, y);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(400, 500);
        setLayout(new GridLayout(0, 2));
        setTitle("Καταχώρηση Νέου Πελάτη");
        conn = new DBConnection("localhost", "root", "230358", "chemistr");

           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);


        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("villages.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;

        Vector<String> villageNames = new Vector(30);
villageNames.add("ΑΓ.ΑΝΤΡΕΑΣ");
villageNames.add("ΑΓ.ΒΑΣΙΛΗΣ");
villageNames.add("ΑΓ.ΔΗΜΗΤΡΗΣ");
villageNames.add("ΑΓ.ΙΩΑΝΝΗΣ");
villageNames.add("ΑΘΗΝΑ");
villageNames.add("ΑΛΕΠΟΧΩΡΙ");
villageNames.add("ΑΠΙΔΙΑ");
villageNames.add("ΑΣΤΕΡΙ");
villageNames.add("ΒΑΣΚΙΝΑ");
villageNames.add("ΒΛΑΧΙΩΤΗ");
villageNames.add("ΓΕΡΑΚΙ");
villageNames.add("ΓΛΥΚΟΒΡΥΣΗ");
villageNames.add("ΓΟΥΒΕΣ");
villageNames.add("ΚΑΡΙΤΣΑ");
villageNames.add("ΚΟΥΠΙΑ");
villageNames.add("ΚΡΕΜΑΣΤΗ");
villageNames.add("ΜΑΚΡΥΝΑΡΑ");
villageNames.add("ΜΑΡΙ");
villageNames.add("ΜΕΤΑΜΟΡΦΩΣΗ");
villageNames.add("ΜΟΛΑΟΙ");
villageNames.add("ΜΥΡΤΙΑ");
villageNames.add("ΝΙΑΤΑ");
villageNames.add("ΠΕΛΕΤΑ");
villageNames.add("ΠΗΓΑΔΙ");
villageNames.add("ΠΙΣΤΑΜΑΤΑ");
villageNames.add("ΠΛΑΚΑ");
villageNames.add("ΠΟΥΛΙΘΡΑ");
villageNames.add("ΣΚΑΛΑ");
villageNames.add("ΧΟΥΝΗ");
villageNames.add("Αλλο..");

        add(new JLabel("ΕΠΩΝΥΜΟ:"));
        attr1 = new JTextField();
        add(attr1);
        
        add(new JLabel("ΟΝΟΜΑ:"));
        attr2 = new JTextField();
        add(attr2);
        
        add(new JLabel("ΠΑΤΡΩΝΥΜΟ:"));
        attr3 = new JTextField();
        add(attr3);
        
        add(new JLabel("ΠΑΡΑΤΣΟΥΚΛΙ:"));
        attr4 = new JTextField();
        add(attr4);
        
        add(new JLabel("ΔΙΕΥΘΥΝΣΗ:"));
        attr5 = new JComboBox(villageNames);
        attr5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (attr5.getSelectedItem().toString().equals("Αλλο..")){
                   attr5Str =  JOptionPane.showInputDialog("Εισάγετε Διεύθυνση");
                   other = true;
                }
            }
        });
        add(attr5);
        

        add(new JLabel("ΣΤΑΘΕΡΟ:"));
        attr6 = new JTextField();
        add(attr6);
        
        add(new JLabel("ΚΙΝΗΤΟ:"));
        attr7 = new JTextField();
        add(attr7);
        
        add(new JLabel("MAIL:"));
        attr8 = new JTextField();
        add(attr8);

        add(new JLabel("ΑΦΜ:"));
        attr9 = new JTextField();
        add(attr9);

        add(new JLabel("ΔΟΥ:"));
        attr10 = new JTextField();
        add(attr10);
        
        

        ok = new JButton("Καταχώρηση");
        add(ok);
        getRootPane().setDefaultButton(ok);

        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {                 

                
                String query="insert into Customer(Added,Surname,Name,FatherName,Nickname,Address,Phone1,Phone2,Mail,Afm,Doy,Money) values(CURDATE(),";
                attr1Str = attr1.getText();                 
                attr2Str = attr2.getText();
                attr3Str = attr3.getText();
                attr4Str = attr4.getText();
                if (!other) attr5Str = attr5.getSelectedItem().toString();
                attr6Str = attr6.getText();
                attr7Str = attr7.getText();
                attr8Str = attr8.getText();
                attr9Str = attr9.getText();
                attr10Str = attr10.getText();
                

                
                query+= "'"+attr1Str+"',";
                query+= "'"+attr2Str+"',";
                query+= "'"+attr3Str+"',";
                query+= "'"+attr4Str+"',";
                query+= "'"+attr5Str+"',";
                query+= "'"+attr6Str+"',";
                query+= "'"+attr7Str+"',";
                query+= "'"+attr8Str+"',";
                query+= "'"+attr9Str+"',";
                query+= "'"+attr10Str+"',";
                query+= "'"+0+"')";
               
                

                try{
                    Statement stmt = conn.createStatement(true);
                    stmt.executeUpdate(query);
                }
                catch(SQLException ex) {
                    System.out.println(ex.getMessage());
                }         
               

                mustUpdate = true;

                dispose();

                JDialog warnDialog = new JDialog();
                warnDialog.setBounds(500,400,200,200);


                int warning = JOptionPane.showConfirmDialog(null, "Θέλετε να προσθέσετε κρασί?", " ", JOptionPane.YES_NO_CANCEL_OPTION);
                if (warning == 0) {
                try{
                    Statement stmt = conn.createStatement(true);
                    ResultSet rs = stmt.executeQuery("SELECT * FROM Customer ORDER BY CustomerID DESC LIMIT 1");
                    rs.next();
                    Wines w = new Wines(rs.getString("CustomerID"));                    
                    w.setVisible(true);
                    w.toBack();
                   
                }
                catch(SQLException ex) {
                    System.out.println(ex.getMessage());
                }                
            }
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

}
