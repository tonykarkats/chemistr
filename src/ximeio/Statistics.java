package ximeio;

import com.mysql.jdbc.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.Vector;
import java.sql.*;


public class Statistics extends JFrame{

    JLabel l1,l2,l3,l4,l5,l6,l7,l8,bl,wl,pl,il;
    JButton b1,b2,b3,b4,b5,b6,b7,b8;
    String first,second,third,yearbar;
    JTextField yearinput,yearinp;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");
    private ImageIcon cpvI = new ImageIcon("icons/people-icon.png");
    private ImageIcon newI = new ImageIcon("icons/Accept-Male-User-icon.png");
    private ImageIcon lostI = new ImageIcon("icons/Male-User-Warning-icon.png");
    private ImageIcon barrelsI = new ImageIcon("icons/Wine-barrel-icon.png");
    private ImageIcon pendingI = new ImageIcon("icons/document-clock-icon.png");
    private ImageIcon sumI = new ImageIcon("icons/sum-icon.png");



    public Statistics() throws SQLException{


        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-400)/ 2;
        final int y = (dim.height-650)/ 2;
        setLocation(x, y);
        setSize(400, 650);
        setLayout(new BorderLayout());
        setTitle("ΣΤΑΤΙΣΤΙΚΑ");

            ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel customers = new JPanel();
        JPanel analysis = new JPanel();
        JPanel drugs = new JPanel();
        JPanel money = new JPanel();        

        //Customers panel
        customers.setLayout(new GridLayout(0, 1));
        b1 = new JButton("Πελάτες / Χωριό",cpvI);
        b1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFrame cpv = new JFrame("Πελάτες / χωρίο");
                JPanel panel = new JPanel();
                cpv.setLayout(new BorderLayout());
                cpv.add(panel,BorderLayout.SOUTH);                
                cpv.setVisible(true);
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension dim = toolkit.getScreenSize();
                cpv.setSize(dim.width/6,dim.height/2);
                final int x = (dim.width-dim.width/6)/ 2;
                final int y = (dim.height-dim.height/2)/ 2;
                cpv.setLocation(x, y);
                MyDefaultTableModel model = new MyDefaultTableModel();
                model.addColumn("Χωριό");
                model.addColumn("# Πελατών");

                Vector<String> rowData = new Vector<String>();
            try {
                Statement stmt = (Statement) conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select Address,count(Address) from Customer,CurrentCustomers where Customer.CustomerID=CurrentCustomers.CustomerID GROUP BY Address ");
                while (rs.next()) {
                    rowData.add(rs.getString(1));
                    rowData.add(rs.getString(2));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
                Statement stmt2 = (Statement) conn.createStatement(true);
                ResultSet rs2 = stmt2.executeQuery("select count(Customer.CustomerID) from Customer,CurrentCustomers where Customer.CustomerID=CurrentCustomers.CustomerID");
                rs2.next();
                panel.add(new JLabel("Σύνολο Πελατών : "+rs2.getString(1)),BorderLayout.SOUTH);
            } catch (SQLException ex) {

                System.out.println(ex.getMessage());
            }

                JTable table = new JTable(model);               
                cpv.add(table,BorderLayout.CENTER);
            }


        });


        b2 = new JButton("Νεοι Πελάτες",newI);
        b2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                   NewCustomers nc = new NewCustomers();
                   nc.setVisible(true);
            }
        });

        b3 = new JButton("Πελάτες που δεν ήρθαν",lostI);
        b3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                LostCustomers lc = new LostCustomers();
                lc.setVisible(true);
            }
        });
        b4 = new JButton("Βαρέλια",barrelsI);
        b4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFrame b = new JFrame("Στατιστικά Βαρελιών");
                b.setVisible(true);
                b.setSize(300,300);
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension dim = toolkit.getScreenSize();
                final int x = (dim.width-300)/ 2;
                final int y = (dim.height-300)/ 2;
                b.setLocation(x, y);
                JPanel panel = new JPanel();
                JPanel toolbar = new JPanel();
                toolbar.setLayout(new GridLayout(0,2));
                panel.setLayout(new GridLayout(4, 1));              
               
               

                JButton ok = new JButton("OK");
                yearinp = new JTextField("Δώσε Ετος");
                yearinp.addMouseListener(new MouseListener() {

                    public void mouseClicked(MouseEvent e) {
                        yearinp.setText("");
                    }

                    public void mousePressed(MouseEvent e) {
                        
                    }

                    public void mouseReleased(MouseEvent e) {
                        
                    }

                    public void mouseEntered(MouseEvent e) {
                        
                    }

                    public void mouseExited(MouseEvent e) {
                        
                    }

                });
                toolbar.add(yearinp);
                toolbar.add(ok);               
                ok.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        yearbar = yearinp.getText();
                Statement stmt2 = (Statement) conn.createStatement(true);
                try {
                    ResultSet rs2 = stmt2.executeQuery("select count(WineID) from Wine where extract(year from Wine.Added)="+yearbar);
                    rs2.next();
                    int ba = rs2.getInt(1);
                    ResultSet rs3 = stmt2.executeQuery("select count(WineID) from Wine where extract(year from Wine.Added)="+yearbar+" and Barreltype='ΞΥΛΙΝΟ'");
                    rs3.next();
                    int w = rs3.getInt(1);
                    ResultSet rs4 = stmt2.executeQuery("select count(WineID) from Wine where extract(year from Wine.Added)="+yearbar+" and Barreltype='ΠΛΑΣΤΙΚΟ'");
                    rs4.next();
                    int p = rs4.getInt(1);
                    ResultSet rs5 = stmt2.executeQuery("select count(WineID) from Wine where extract(year from Wine.Added)="+yearbar+" and Barreltype='INOX'");
                    rs5.next();
                    int i = rs5.getInt(1);

                   double wp = ((double)w/(double)ba)*100.0;
                   double pp = ((double)p/(double)ba)*100.0;
                   double ip = ((double)i/(double)ba)*100.0;
                    wp = (double)Math.round(wp * 10) / 10;
                 pp = (double)Math.round(pp * 10) / 10;
                 ip = (double)Math.round(ip * 10) / 10;

                 bl.setText("Συνολο Βαρελιών: "+ ba);
                 wl.setText("Ξυλινα: "+ wp+" %");
                 pl.setText("Πλαστικά: "+ pp+" %");
                 il.setText("Ανοξείδωτα: "+ ip+" %");


                } catch (SQLException ex) {
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
                }
                    }
                });
                

                
                b.add(panel,BorderLayout.CENTER);
                b.add(toolbar,BorderLayout.SOUTH);
                bl = new JLabel("Συνολο Βαρελιών: ");
                wl = new JLabel("Ξυλινα: ");
                pl = new JLabel("Πλαστικά: ");
                il = new JLabel("Ανοξείδωτα: ");
                panel.add(bl);
                panel.add(wl);
                panel.add(pl);
                panel.add(il);
              
            }
        });

        customers.add(b1);
        customers.add(b2);
        customers.add(b3);
        customers.add(b4);

        //Analysis panel

        analysis.setLayout(new GridLayout(0, 1));
        b5 = new JButton("Σύνολο Κιλών",sumI);
        b5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String year;
                year =  JOptionPane.showInputDialog("Δώσε Χρονιά (ΥΥΥΥ)");
                JFrame k = new JFrame("Σύνολο Κιλών");
                k.setVisible(true);
                k.setSize(300,300);
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension dim = toolkit.getScreenSize();
                final int x = (dim.width-300)/ 2;
                final int y = (dim.height-300)/ 2;
                k.setLocation(x, y);
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(3, 1));
                try{
                Statement stmt3 = (Statement) conn.createStatement(true);
                ResultSet rs6 = stmt3.executeQuery("select sum(kilos) from Wine,Analysis where Analysis.WineID=Wine.WineID and Analysis.AnalysisNr='1' and extract(year from Wine.Added) ="+year);
                rs6.next();
                first = rs6.getString(1);
                ResultSet rs7 = stmt3.executeQuery("select sum(kilos) from Wine,Analysis where Analysis.WineID=Wine.WineID and Analysis.AnalysisNr='2' and extract(year from Wine.Added) ="+year);
                rs7.next();
                second = rs7.getString(1);
                ResultSet rs8 = stmt3.executeQuery("select sum(kilos) from Wine,Analysis where Analysis.WineID=Wine.WineID and Analysis.AnalysisNr='3' and extract(year from Wine.Added) ="+year);
                rs8.next();
                third = rs8.getString(1);
                }
                catch (SQLException exs){

                }
                k.add(panel,BorderLayout.CENTER);
                panel.add(new JLabel("Συνολο 1ης Ανάλυσης:    "+ first+" kg"));
                panel.add(new JLabel("Συνολο 2ης Ανάλυσης:    "+ second+" kg"));
                panel.add(new JLabel("Συνολο 3ης Ανάλυσης:    "+ third+" kg"));


            }
        });

         b6 = new JButton("Εκκρεμεί 2η Ανάλυση",pendingI);
         b6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NoSecondAnalysis no = new NoSecondAnalysis();
                no.setVisible(true);
            }
        });


        analysis.add(b5);
        analysis.add(b6);

        //Drugs Panel

        drugs.setLayout(new BorderLayout());
        JPanel panel1 = new JPanel(new GridLayout(0, 2));
        JPanel panel2 = new JPanel(new GridLayout(1, 2));

        drugs.add(panel1,BorderLayout.CENTER);
        final JLabel sugar,water,tacid,cacid,me,lux,caco;
        sugar = new JLabel();
        water = new JLabel();
        tacid = new JLabel();
        cacid = new JLabel();
        me = new JLabel();
        lux = new JLabel();
        caco = new JLabel();

        panel1.add(new JLabel("Sugar"));
        panel1.add(sugar);
        panel1.add(new JLabel("Water"));
        panel1.add(water);
        panel1.add(new JLabel("Tartaric Acid"));
        panel1.add(tacid);
        panel1.add(new JLabel("Citric Acid"));
        panel1.add(cacid);
        panel1.add(new JLabel("Me"));
        panel1.add(me);
        panel1.add(new JLabel("Lux"));
        panel1.add(lux);
        panel1.add(new JLabel("Caco3"));
        panel1.add(caco);

        String year;
        yearinput = new JTextField("Δώσε έτος");
        yearinput.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                yearinput.setText("");
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

        panel2.add(yearinput);
        JButton ok = new JButton("OK");
        panel2.add(ok);
        getRootPane().setDefaultButton(ok);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String year = yearinput.getText();
                Statement stmt5 = (Statement)conn.createStatement(true);
                try {
                    ResultSet rs = stmt5.executeQuery("SELECT sum(AddSugar),sum(AddWater),sum(Tacid),sum(Cacid),sum(Me),sum(Lux),sum(Caco3) from Analysis WHERE extract(year from AnalysisDate)="+year);
                    rs.next();
                    sugar.setText(rs.getString(1));
                    water.setText(rs.getString(2));
                    tacid.setText(rs.getString(3));
                    cacid.setText(rs.getString(4));
                    me.setText(rs.getString(5));
                    lux.setText(rs.getString(6));
                    caco.setText(rs.getString(7));

                } catch (SQLException ex) {
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
                }



            }
        });
       drugs.add(panel2,BorderLayout.SOUTH);
       

       //Money panel

       final JLabel money_fake,money_real,paid;
       final JButton own;
       final  JPanel panel4 = new JPanel(new GridLayout(0, 2));
       final  JPanel panel5 = new JPanel(new GridLayout(0, 2));
       money.setLayout(new BorderLayout());
       money_fake = new JLabel();
       money_real = new JLabel();
       paid = new JLabel();
       own = new JButton("Πελάτες που χρωστούν");
       own.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Owning ow = new Owning();
                ow.setVisible(true);
            }
        });
      
       panel4.add(new JLabel("Τζίρος Χημείου  (Euro)"));
       panel4.add(money_fake);
       panel4.add(new JLabel("Πραγματικός Τζίρος  (Euro)"));
       panel4.add(money_real);
       panel4.add(new JLabel("Εχουν πληρώσει"));
       panel4.add(paid);

        
      
                Statement stmt5 = (Statement)conn.createStatement(true);
                try {
                    ResultSet rs = stmt5.executeQuery("select sum(Money),sum(MoneyGiven) from Customer,CurrentCustomers where Customer.CustomerID=CurrentCustomers.CustomerID");
                    rs.next();
                    money_fake.setText(rs.getString(1));
                    money_real.setText(rs.getString(2));


                } catch (SQLException ex) {
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
                }

                int paidc = 0,customersn = 0;
                try {
                    ResultSet rs = stmt5.executeQuery("select count(*) from Customer,CurrentCustomers where Customer.CustomerID=CurrentCustomers.CustomerID");
                    rs.next();
                    customersn = rs.getInt(1);
                } catch (SQLException ex) {
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    ResultSet rs = stmt5.executeQuery("select count(*) from Customer,CurrentCustomers where Customer.CustomerID=CurrentCustomers.CustomerID and MoneyGiven = Money");
                    rs.next();
                    paidc = rs.getInt(1);
                } catch (SQLException ex) {
                    Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
                }
                paid.setText(Integer.toString(paidc)+" / "+Integer.toString(customersn));
       


       money.add(panel4,BorderLayout.CENTER);
       money.add(own,BorderLayout.SOUTH);
       

        JTabbedPane p = new JTabbedPane();
        p.addTab("ΠΕΛΑΤΕΣ", customers);
        p.addTab("ΑΝΑΛΥΣΕΙΣ", analysis);
        p.addTab("ΦΑΡΜΑΚΑ", drugs);
        p.addTab("ΟΙΚΟΝΟΜΙΚΑ", money);


        add(p,BorderLayout.CENTER);

    }

}

