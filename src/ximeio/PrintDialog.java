package ximeio;

import com.mysql.jdbc.Statement;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

public class PrintDialog extends JFrame{

    DBConnection conn= new DBConnection("localhost", "root", "230358", "chemistr");
    private String analysisNr,AnalysisDate,Name,NrBarrel,Kilos,AddWater,AddSugar,FinalAlcohol,CustomerID,Surname,Nickname,MoneyOwned,notes="";
    private JTextField t1,t2,t3,t4,t5,t6,t7,t8,t9;
    private String s1,s2,s3,s4,s5,s6,s7,s8,s9,nchoice;
    JPanel panel;
    JToolBar toolbar;
    JButton b10;
    JRadioButton[] choice;
    JFrame editframe;
    private String param1,param2;


    public PrintDialog(String analysisID){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-500)/ 2;
        final int y = (dim.height-600)/ 2;
        setLocation(x, y);
        setSize(new Dimension(500,600));
        setLayout(new BorderLayout());
        setTitle("ΕΚΤΥΠΩΣΗ ΠΕΛΑΤΗ");

        //Get print parameters

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
        param1 = params[21];
        param2 = params[22];
        //System.out.println(param1+" "+param2);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

         ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        //Retrieve all itmes needed from the database

        ResultSet rs,rs2,rs3;
        try{
            Statement stmt = (Statement)conn.createStatement(true);
            rs = stmt.executeQuery("select WineID,AnalysisNr,DATE_FORMAT(AnalysisDate,'%d/ %m/ %Y') as AnalysisDate,AddWater,AddSugar,FinalAlcohol from Analysis where AnalysisID="+analysisID);
            rs.next();
            String WineID = rs.getString("WineID");
            analysisNr = rs.getString("AnalysisNr");
            AnalysisDate = rs.getString("AnalysisDate");
            AddWater = rs.getString("AddWater");
            AddSugar = rs.getString("AddSugar");
            FinalAlcohol = rs.getString("FinalAlcohol");


            rs2 = stmt.executeQuery("select NrBarrel,Kilos,CustomerID from Wine where WineID="+WineID);
            rs2.next();
            NrBarrel = rs2.getString("NrBarrel");
            Kilos = rs2.getString("Kilos");
            CustomerID = rs2.getString("CustomerID");

            rs3 = stmt.executeQuery("select Surname,Name,Nickname,MoneyOwned from Customer where CustomerID="+CustomerID);
            rs3.next();
            Surname = rs3.getString("Surname");
            Name = rs3.getString("Name");
            Nickname = rs3.getString("Nickname");
            MoneyOwned = rs3.getString("MoneyOwned");


           // System.out.println(WineID+" "+analysisNr+" "+AnalysisDate+" "+AddWater+" "+AddSugar+" "+NrBarrel+" "+Kilos+" "+Surname+" "+Name+" "+Nickname+" "+MoneyOwned);

        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }

        //Set Up Window
        if(analysisNr.equals("1")){
            if(Double.parseDouble(FinalAlcohol)<Double.parseDouble(param1))
                FinalAlcohol = "12.5";
            else if(Double.parseDouble(FinalAlcohol)>Double.parseDouble(param2))
                FinalAlcohol = "12.8";
        }

        if(!MoneyOwned.equals("0"))
            Name+= " *";
        else
            Name+= " **";

       
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        this.add(panel,BorderLayout.CENTER);

        panel.add(new JLabel("ΕΠΩΝΥΜΟ"));
        t1 = new JTextField(Surname);
        t1.setEditable(false);
        panel.add(t1);

        panel.add(new JLabel("ΟΝΟΜΑ"));
        t2 = new JTextField(Name);
        t2.setEditable(false);
        panel.add(t2);

        panel.add(new JLabel("ΠΑΡΑΤΣΟΥΚΛΙ"));
        t3 = new JTextField(Nickname);
        t3.setEditable(false);
        panel.add(t3);

        panel.add(new JLabel("ΑΝΑΛΥΣΗ"));
        t4 = new JTextField(analysisNr);
        t4.setEditable(false);
        panel.add(t4);

        panel.add(new JLabel("Νo ΒΑΡΕΛΙΟΥ"));        
        t5 = new JTextField(NrBarrel);
        t5.setEditable(false);
        panel.add(t5);

        panel.add(new JLabel("ΚΙΛΑ"));        
        t6 = new JTextField(Kilos);
        t6.setEditable(false);
        panel.add(t6);

        panel.add(new JLabel("ΠΡΟΣΘΗΚΗ ΝΕΡΟΥ"));
        t7 = new JTextField(AddWater);
        panel.add(t7);

        panel.add(new JLabel("ΠΡΟΣΘΗΚΗ ΖΑΧΑΡΗΣ"));
        t8 = new JTextField(AddSugar);
        panel.add(t8);

        panel.add(new JLabel("ΤΕΛΙΚΟΙ ΒΑΘΜΟΙ"));
        t9 = new JTextField(FinalAlcohol);
        panel.add(t9);

        panel.add(new JLabel("ΠΑΡΑΤΗΡΗΣΕΙΣ.."));
        b10 = new JButton("...");
        panel.add(b10);
        b10.addActionListener(new ActionListener() {
            private JFrame notes1;
            private ButtonGroup g1;

            public void actionPerformed(ActionEvent e) {
                if(analysisNr.equals("1")){
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    Dimension dim = toolkit.getScreenSize();
                    notes1 = new JFrame("ΠΑΡΑΤΗΤΗΣΕΙΣ ΓΙΑ 1η ΑΝΑΛΥΣΗ");
                    JPanel notes1p1 = new JPanel();
                    JPanel notes1p2 = new JPanel();
                    notes1.setLayout(new BorderLayout());
                    notes1.add(notes1p1,BorderLayout.CENTER);
                    notes1.add(notes1p2,BorderLayout.SOUTH);


                    notes1.setSize(dim.width, dim.height-80);
                    notes1p1.setLayout(new GridLayout(0,1));
                    notes1p2.setLayout(new GridLayout(0, 2));
                    //final int x = (dim.width-950)/ 2;
                    //final int y = (dim.height-600)/ 2;
                    //notes1.setLocation(x, y);
                    notes1.setVisible(true);

                    g1 = new ButtonGroup();

                    choice = new JRadioButton[6];
                    for(int i=0;i<choice.length;i++){
                        choice[i] = new JRadioButton();
                        choice[i].setFont(new Font("Arial", Font.PLAIN, 23));
                        g1.add(choice[i]);
                    }


                    choice[0].setText("<html>Η ΖΑΧΑΡΗ ΝΑ ΔΙΑΛΥΘΕΙ ΣΕ ΧΛΙΑΡΟ ΜΟΥΣΤΟ ΚΑΙ ΝΑ ΠΕΣΕΙ ΜΟΛΙΣ ΑΡΧΙΖΕΙ ΝΑ \nΒΡΑΖΕΙ O ΜΟΥΣΤΟΣ.<br/>\n"
                            + "Το Νο1 διαλυμένο σε χλιαρό μούστο την 1η μέρα.\nΤο Νο2 διαλυμένο σε χλιαρό μούστο την 2η μέρα.</html>");
                    
                    choice[1].setText("<html>Η ΖΑΧΑΡΗ ΝΑ ΔΙΑΛΥΘΕΙ ΣΕ ΧΛΙΑΡΟ ΜΟΥΣΤΟ ΚΑΙ ΝΑ ΠΕΣΕΙ ΜΟΛΙΣ ΑΡΧΙΖΕΙ ΝΑ \nΒΡΑΖΕΙ Ο ΜΟΥΣΤΟΣ.<br/>\n"
                            + "Το Νο1 διαλυμένο σε χλιαρό μούστο.</html>");
                    choice[2].setText("<html>Το Νο1 διαλυμένο σε "+Double.parseDouble(t7.getText())/2+" κιλά χλιαρό νερό την 1η μέρα.<br/>\nΤο Νο2 διαλυμένο σε "+Double.parseDouble(AddWater)/2+" κιλά  χλιαρό νερό την 2η μέρα.</html>");
                    choice[3].setText("Το Νο1 διαλυμένο σε "+t7.getText()+" κιλά χλιαρό νερό.");
                    choice[4].setText("<html>Το Νο1 διαλυμένο σε χλιαρό μούστο την 1η μέρα.<br/>\nΤο Νο2 διαλυμένο σε χλιαρό μούστο την 2η μέρα.</html>");
                    choice[5].setText("Το Νο1 διαλυμένο σε χλιαρό μούστο.");

                    for(int i=0;i<choice.length;i++){
                        notes1p1.add(choice[i]);
                        choice[i].setActionCommand(choice[i].getText().replace("<html>", "").replace("</html>", "").replace("<br/>", ""));
                    }

                    JButton ok = new JButton("OK");
                    notes1p2.add(ok);

                     class VoteActionListener implements ActionListener {
                        public void actionPerformed(ActionEvent ev) {
                        notes =  g1.getSelection().getActionCommand();

                        }
                     };

                     ActionListener alisten = new VoteActionListener( );
                     for(int i=0;i<choice.length;i++)
                        choice[i].addActionListener(alisten);


                 /*   ok.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                                notes = nchoice;
                                System.out.println(notes);
                        }
                    }); */

                     JButton edit = new JButton("Επεξεργασία");
                     notes1p2.add(edit);
                     edit.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                              editframe = new JFrame("Επεξεργασία");
                              editframe.setLayout(new BorderLayout());
                              editframe.setSize(800,420);
                              Toolkit toolkit = Toolkit.getDefaultToolkit();
                              Dimension dim = toolkit.getScreenSize();
                              final int x = (dim.width-800)/ 2;
                              final int y = (dim.height-420)/ 2;
                              editframe.setLocation(x, y);

                              editframe.setVisible(true);
                              final JTextArea text = new JTextArea(notes);
                              text.setLineWrap(true);
                              editframe.add(text,BorderLayout.CENTER);
                              JButton ok = new JButton("OK");
                              editframe.add(ok,BorderLayout.SOUTH);
                              ok.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent e) {
                                        notes = text.getText();
                                        editframe.dispose();
                                        notes1.dispose();
                                }
                            });

                        }
                    });


                     ok.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            notes1.dispose();
                           // System.out.println(notes);
                        }
                    });

                }

            else if (analysisNr.equals("2") || analysisNr.equals("3")){
                       Toolkit toolkit = Toolkit.getDefaultToolkit();
                    Dimension dim = toolkit.getScreenSize();
                    notes1 = new JFrame("ΠΑΡΑΤΗΤΗΣΕΙΣ ΓΙΑ 2η-3η ΑΝΑΛΥΣΗ");
                    JPanel notes1p1 = new JPanel();
                    JPanel notes1p2 = new JPanel();
                    notes1.setLayout(new BorderLayout());
                    notes1.add(notes1p1,BorderLayout.CENTER);
                    notes1.add(notes1p2,BorderLayout.SOUTH);


                    notes1.setSize(dim.width, dim.height-80);
                    notes1p1.setLayout(new GridLayout(0,1));
                    notes1p2.setLayout(new GridLayout(0,2));
                    //final int x = (dim.width-1024)/ 2;
                    //final int y = (dim.height-600)/ 2;
                    //notes1.setLocation(x, y);
                    notes1.setVisible(true);

                    g1 = new ButtonGroup();

                    choice = new JRadioButton[12];
                    for(int i=0;i<choice.length;i++){
                        choice[i] = new JRadioButton();
                        choice[i].setFont(new Font("Arial", Font.PLAIN, 17));
                        g1.add(choice[i]);
                    }


                    choice[0].setText("<html>ΝΑ ΓΙΝΕΙ ΑΠΟΛΑΣΠΩΣΗ ΠΡΙΝ ΠΕΣΟΥΝ ΟΙ ΟΙΝΟΛΟΓΙΚΕΣ ΟΥΣΙΕΣ.<br/>\nΤο Νο1 διαλυμένο σε χλιαρό κρασί την 1η μέρα.\n"
                            + "Το Νο2 διαλυμένο σε χλιαρό κρασί την 2η μέρα.<br/>\nΤο Νο3 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");

                    choice[1].setText("<html>ΝΑ ΓΙΝΕΙ ΑΠΟΛΑΣΠΩΣΗ ΠΡΙΝ ΠΕΣΟΥΝ ΟΙ ΟΙΝΟΛΟΓΙΚΕΣ ΟΥΣΙΕΣ.</br>\nΤο Νο1 διαλυμένο σε χλιαρό κρασί την 1η μέρα.\n"
                            + "<br/>Το Νο2 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");
                    choice[2].setText("<html>ΝΑ ΓΙΝΕΙ ΑΠΟΛΑΣΠΩΣΗ ΠΡΙΝ ΠΕΣΟΥΝ ΟΙ ΟΙΝΟΛΟΓΙΚΕΣ ΟΥΣΙΕΣ.<br/>\nΤο Νο1 διαλυμένο σε "+Double.parseDouble(t7.getText())/2+" κιλά  χλιαρό νερό την 1η μέρα.<br/>\n"
                            + "Το Νο2 διαλυμένο σε "+Double.parseDouble(t7.getText())/2+" κιλά χλιαρό νερό την 2η μέρα.\nΤο Νο3 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");
                    choice[3].setText("<html>ΝΑ ΓΙΝΕΙ ΑΠΟΛΑΣΠΩΣΗ ΠΡΙΝ ΠΕΣΟΥΝ ΟΙ ΟΙΝΟΛΟΓΙΚΕΣ ΟΥΣΙΕΣ.<br/>\nΤο Νο1 διαλυμένο σε "+Double.parseDouble(t7.getText())+"κιλά "
                            + "χλιαρό νερό την 1η μέρα.<br/>\nΤο Νο2 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");
                    choice[4].setText("<html>ΚΑΛΟ ΑΝΑΚΑΤΕΜΑ ΓΙΑ ΝΑ ΞΕΒΡΑΣΕΙ (ΓΙΑ 3-4 ΗΜΕΡΕΣ).<br/>\nΤο Νο1 διαλυμένο σε χλιαρό κρασί την 1η μέρα.\n"
                            + "Το Νο2 διαλυμένο σε χλιαρό κρασί την 2η μέρα.<br/>\nΤο Νο3 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");
                    choice[5].setText("<html>ΚΑΛΟ ΑΝΑΚΑΤΕΜΑ ΓΙΑ ΝΑ ΞΕΒΡΑΣΕΙ (ΓΙΑ 3-4 ΗΜΕΡΕΣ).<br/>\nΤο Νο1 διαλυμένο σε χλιαρό κρασί την 1η μέρα.<br/>\n"
                            + " Το Νο2 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");
                    choice[6].setText("<html>ΚΑΛΟ ΑΝΑΚΑΤΕΜΑ ΓΙΑ ΝΑ ΞΕΒΡΑΣΕΙ (ΓΙΑ 3-4 ΗΜΕΡΕΣ).<br/>\nΤο Νο1 διαλυμένο σε "+Double.parseDouble(t7.getText())/2+" κιλά χλιαρό νερό την 1η μέρα.\n"
                            + "Το Νο2 διαλυμένο σε "+Double.parseDouble(t7.getText())/2+" χλιαρό νερό την 2η μέρα.<br/>\nΤο Νο3 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");
                    choice[7].setText("<html>ΚΑΛΟ ΑΝΑΚΑΤΕΜΑ ΓΙΑ ΝΑ ΞΕΒΡΑΣΕΙ (ΓΙΑ 3-4 ΗΜΕΡΕΣ).\nΤο Νο1 διαλυμένο σε "+Double.parseDouble(t7.getText())+" κιλά χλιαρό νερό την 1η μέρα.\n"
                            + "<br/>Το Νο2 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");
                    choice[8].setText("<html>Το Νο1 διαλυμένο σε χλιαρό κρασί την 1η μέρα.\nΤο Νο2 διαλυμένο σε χλιαρό κρασί την 2η μέρα.\n"
                            + "<br/>Το Νο3 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");
                    choice[9].setText("Το Νο1 διαλυμένο σε χλιαρό κρασί την 1η μέρα.\nΤο Νο2 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).");
                    choice[10].setText("<html>Το Νο1 διαλυμένο σε "+Double.parseDouble(t7.getText())/2+" κιλά χλιαρό νερό την 1η μέρα.\nΤο Νο2 διαλυμένο σε "+Double.parseDouble(AddWater)/2+" κιλά  χλιαρό νερό την 2η μέρα.\n"
                            + "<br/>Το Νο3 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).</html>");
                    choice[11].setText("Το Νο1 διαλυμένο σε "+Double.parseDouble(t7.getText())+" κιλά χλιαρό νερό την 1η μέρα. Το Νο2 ΑΔΙΑΛΥΤΟ μόλις σφραγιστεί το βαρέλι (σε 5-6 ημέρες).");



                    for(int i=0;i<choice.length;i++){
                        notes1p1.add(choice[i]);
                        choice[i].setActionCommand(choice[i].getText().replace("<html>", "").replace("</html>", "").replace("<br/>", ""));
                    }

                    JButton ok = new JButton("OK");
                    notes1p2.add(ok);

                     class VoteActionListener implements ActionListener {
                        public void actionPerformed(ActionEvent ev) {
                        notes =  g1.getSelection().getActionCommand();

                        }
                     };

                     ActionListener alisten = new VoteActionListener( );
                     for(int i=0;i<choice.length;i++)
                        choice[i].addActionListener(alisten);


                 /*   ok.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                                notes = nchoice;
                                System.out.println(notes);
                        }
                    }); */

                     JButton edit = new JButton("Επεξεργασία");
                     notes1p2.add(edit);
                     edit.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                              editframe = new JFrame("Επεξεργασία");
                              editframe.setLayout(new BorderLayout());
                              editframe.setSize(800,420);
                              Toolkit toolkit = Toolkit.getDefaultToolkit();
                              Dimension dim = toolkit.getScreenSize();
                              final int x = (dim.width-800)/ 2;
                              final int y = (dim.height-420)/ 2;
                              editframe.setLocation(x, y);

                              editframe.setVisible(true);
                              final JTextArea text = new JTextArea(notes);
                              text.setLineWrap(true);

                              editframe.add(text,BorderLayout.CENTER);
                              JButton ok = new JButton("OK");
                              editframe.add(ok,BorderLayout.SOUTH);
                              ok.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent e) {
                                        notes = text.getText();
                                        editframe.dispose();
                                        notes1.dispose();
                                }
                            });

                        }
                    });


                     ok.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {                           
                            notes1.dispose();
                            //System.out.println(notes);
                        }
                    });

            }
            }
        });


        toolbar = new JToolBar();
        add(toolbar,BorderLayout.SOUTH);
        toolbar.setLayout(new GridLayout(0, 2));

        JButton print = new JButton("Εκτύπωση");
        print.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                     AddWater = t7.getText();
                     AddSugar = t8.getText();
                     FinalAlcohol = t9.getText();
                    //Make body String and print it via PrintAnalysis
                     String[] body = new String[9];

                     String analysis;
                     if (analysisNr.equals("1"))
                         analysis = "A";
                     else if (analysisNr.equals("2"))
                         analysis = "B";
                     else analysis = "Γ";

                      body[0] = "ΑΝΑΛΥΣΗ : "+analysis;
                      body[1]= AnalysisDate;
                      body[2] =Surname+"  "+Name;
                      body[3] = NrBarrel;
                      body[4] = Kilos;
                      if (AddWater.equals("0"))
                          body[5]="";
                      else
                          body[5] = AddWater+" ΚΙΛΑ";
                      if (AddSugar.equals("0"))
                          body[6]="";
                      else
                          body[6] = AddSugar+" ΚΙΛΑ" ;
                      
                      body[7] =FinalAlcohol;
                      body[8] = notes;



                      PrintAnalysis pa = new PrintAnalysis(body);
                      
            }
        });
        toolbar.add(print);
        getRootPane().setDefaultButton(print);

        JButton cancel = new JButton("Ακύρωση");
        toolbar.add(cancel);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        });

    }



}
