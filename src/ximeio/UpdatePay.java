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


public class UpdatePay extends JDialog{

    private    JTextField attr1,attr2;
    String     attr1Str,attr2Str;
    JButton cancel,ok;
    DBConnection conn;
    boolean mustUpdate=true;


    public UpdatePay(final String id) {


        setSize(500, 200);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-500)/ 2;
        final int y = (dim.height-200)/ 2;
        setLocation(x, y);
        setLayout(new GridLayout(0,2));
        conn = new DBConnection("localhost", "root", "230358", "chemistr");
        ResultSet rs;
        String Surname = null,Name = null,Nickname=null,Money=null;
        setTitle("Ενημέρωση Πληρωμών");

          ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

       String query="select * from Customer where CustomerID="+id;
      // System.out.println("id="+id);

        try{
        Statement stmt = conn.createStatement(true);
                    rs = stmt.executeQuery(query);
        rs.next();
      //  while(rs.next()) {
            Surname = rs.getString("Surname");
            Name = rs.getString("Name");
            Nickname = rs.getString("Nickname");
            Money = rs.getString("Money");




      //  }


       // System.out.println(rs.)
        add(new JLabel(Surname+" "+Name+" "+Nickname));
        add(new JLabel(""));


        add(new JLabel("ΠΟΣΟ:"));
        attr1 = new JTextField(Money);
        add(attr1);

        add(new JLabel("ΠΡΟΚΑΤΑΒΟΛΗ:"));
        attr2 = new JTextField();
        add(attr2);


        }
        catch(SQLException ex) {
            System.out.println(ex.getMessage());
        };


        ok = new JButton("Καταχώρηση");
        add(ok);
        getRootPane().setDefaultButton(ok);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                int owned=0;


                attr1Str = attr1.getText();
                attr2Str = attr2.getText();

                owned=Integer.parseInt(attr1Str)-Integer.parseInt(attr2Str);


                //System.out.println(owned+"edw");

                String query="UPDATE Customer SET Money='"+attr1Str+"',MoneyGiven='"+attr2Str+"',MoneyOwned='"+owned+"' WHERE CustomerID="+id;



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
