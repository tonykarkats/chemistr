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


public class UpdateCustomer extends JDialog{

    private    JTextField attr1,attr2,attr3,attr4,attr5,attr6,attr7,attr8,attr9,attr10;
    String     attr1Str,attr2Str,attr3Str,attr4Str,attr5Str,attr6Str,attr7Str,attr8Str,attr9Str,attr10Str;
    JButton cancel,ok;
    DBConnection conn;
    boolean mustUpdate=true;


    public UpdateCustomer(final String id) {
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-400)/ 2;
        final int y = (dim.height-500)/ 2;
        setLocation(x, y);
        setSize(400, 500);
        setLayout(new GridLayout(0,2));
        conn = new DBConnection("localhost", "root", "230358", "chemistr");
        ResultSet rs;
        String Surname = null,Name = null,FatherName = null,Nickname = null,Address = null,Phone1 = null,Phone2 = null,mail = null,afm = null,doy = null;
        setTitle("Ενημέρωση Πελάτη");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

       String query="select * from Customer where CustomerID="+id;
       System.out.println("id="+id);

        try{
        Statement stmt = conn.createStatement(true);
                    rs = stmt.executeQuery(query);
        rs.next();
      //  while(rs.next()) {
            Surname = rs.getString("Surname");
            Name = rs.getString("Name");
            FatherName = rs.getString("FatherName");
            Nickname =  rs.getString("Nickname");
           Address = rs.getString("Address");
          Phone1= rs.getString("Phone1");
          Phone2 = rs.getString("Phone2");
           mail= rs.getString("mail");
          afm = rs.getString("afm");
          doy = rs.getString("doy");



      //  }
        

       // System.out.println(rs.)
        add(new JLabel("ΕΠΩΝΥΜΟ:"));
        attr1 = new JTextField(Surname);
        add(attr1);

       
        add(new JLabel("ΟΝΟΜΑ:"));
        attr2 = new JTextField(Name);
        add(attr2);

        add(new JLabel("ΠΑΤΡΩΝΥΜΟ:"));
        attr3 = new JTextField(FatherName);
        add(attr3);

        add(new JLabel("ΠΑΡΩΝΥΜΟ:"));
        attr4 = new JTextField(Nickname);
        add(attr4);

        add(new JLabel("ΔΙΕΥΘΥΝΣΗ:"));
        attr5 = new JTextField(Address);
        add(attr5);

        add(new JLabel("ΣΤΑΘΕΡΟ:"));
        attr6 = new JTextField(Phone1);
        add(attr6);

        add(new JLabel("ΚΙΝΗΤΟ:"));
        attr7 = new JTextField(Phone2);
        add(attr7);

        add(new JLabel("MAIL:"));
        attr8 = new JTextField(mail);
        add(attr8);

        add(new JLabel("ΑΦΜ:"));
        attr9 = new JTextField(afm);
        add(attr9);

        add(new JLabel("ΔΟΥ:"));
        attr10 = new JTextField(doy);
        add(attr10);
        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        };


        ok = new JButton("Καταχώρηση");
        add(ok);
        getRootPane().setDefaultButton(ok);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {


                
                attr1Str = attr1.getText();
                attr2Str = attr2.getText();
                attr3Str = attr3.getText();
                attr4Str = attr4.getText();
                attr5Str = attr5.getText();
                attr6Str = attr6.getText();
                attr7Str = attr7.getText();
                attr8Str = attr8.getText();
                attr9Str = attr9.getText();
                attr10Str = attr10.getText();
               // System.out.println(id);
                String query="UPDATE Customer SET Surname='"+attr1Str+"', Name='"+attr2Str+"', FatherName='"+attr3Str+"', Nickname='"+attr4Str+"', Address='"+attr5Str+"', Phone1='"+attr6Str+"', Phone2='"+attr7Str+"', mail='"+attr8Str+"', afm='"+attr9Str+"', doy='"+attr10Str+"' WHERE CustomerID="+id;


               



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

        cancel = new JButton("Ακύρωση");
        add(cancel);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });











    }

}
