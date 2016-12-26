package ximeio;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.CubicCurve2D;
import javax.swing.*;
import java.sql.*;


public class DrugsPanel extends JDialog{

    private    JTextField attr1,attr2,attr3,attr4,attr5,attr6,attr7,attr8,attr9,attr10;
    String     attr1Str,attr2Str,attr3Str,attr4Str,attr5Str,attr6Str,attr7Str,attr8Str,attr9Str,attr10Str;
    JButton print,ok;    DBConnection conn;
    


    public DrugsPanel(final String analysisID) {
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-400)/ 2;
        final int y = (dim.height-500)/ 2;
        setLocation(x, y);
        setSize(400, 500);
        setLayout(new GridLayout(0,2));
        conn = new DBConnection("localhost", "root", "230358", "chemistr");
        ResultSet rs;
        String AddWater = null,AddSugar = null,TAcid = null,Cacid = null,Me = null,Lux = null,Caco3 = null,Tanine = null,Vitamin = null;
        setTitle("Αποτελέσματα Ανάλυσης");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

       String query="select AddWater,AddSugar,Tacid,Cacid,Me,Lux,Caco3,Tanine,Vitamin from Analysis where AnalysisID="+analysisID;
       //System.out.println("id="+analysisID);

        try{
        Statement stmt = conn.createStatement(true);
                    rs = stmt.executeQuery(query);
        rs.next();
      
            AddWater = rs.getString("AddWater");
            AddSugar = rs.getString("AddSugar");
            TAcid = rs.getString("Tacid");
            Cacid =  rs.getString("Cacid");
            Me = rs.getString("Me");
            Lux= rs.getString("Lux");
            Caco3 = rs.getString("Caco3");
            Tanine= rs.getString("Tanine");
            Vitamin = rs.getString("Vitamin");   
        

       
        add(new JLabel("ΠΡΟΣΘΗΚΗ ΝΕΡΟΥ:"));
        attr1 = new JTextField(AddWater);        
        add(attr1);

       
        add(new JLabel("ΠΡΟΣΘΗΚΗ ΖΑΧΑΡΗΣ:"));
        attr2 = new JTextField(AddSugar);
        add(attr2);

        add(new JLabel("ΤΡΥΓΙΚΟ:"));
        attr3 = new JTextField(TAcid);
        add(attr3);

        add(new JLabel("ΚΙΤΡΙΚΟ:"));
        attr4 = new JTextField(Cacid);
        add(attr4);

        add(new JLabel("Me:"));
        attr5 = new JTextField(Me);
        add(attr5);

        add(new JLabel("Lux:"));
        attr6 = new JTextField(Lux);
        add(attr6);

        add(new JLabel("Caco3:"));
        attr7 = new JTextField(Caco3);
        add(attr7);

        add(new JLabel("Tanine:"));
        attr8 = new JTextField(Tanine);
        add(attr8);

        add(new JLabel("Vitamin:"));
        attr9 = new JTextField(Vitamin);
        add(attr9);        
        
        attr1.setEditable(false);
        attr2.setEditable(false);
        attr3.setEditable(false);
        attr4.setEditable(false);
        attr5.setEditable(false);
        attr6.setEditable(false);
        attr7.setEditable(false);
        attr8.setEditable(false);
        attr9.setEditable(false);
        
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        };


        ok = new JButton("OK");
        add(ok);
        getRootPane().setDefaultButton(ok);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        print = new JButton("Εκτύπωση Ανάλυσης");
        add(print);
        print.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                 PrintDialog pd =new PrintDialog(analysisID);
                 pd.setVisible(true);
            }
        });











    }
}


