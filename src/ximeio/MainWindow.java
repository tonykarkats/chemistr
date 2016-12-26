package ximeio;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.event.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.DefaultMenuLayout;

class MainWindow extends JFrame{

    JButton button1,button2,button3,button4,button5;

    public MainWindow() {
        try {
            backup();
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        setLayout(new BorderLayout());
        setTitle("ΧΗΜΕΙΟ ΝΙΑΤΩΝ ΛΑΚΩΝΙΑΣ");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

       

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-400)/ 2;
        final int y = (dim.height-600)/ 2;
        setLocation(x, y);




        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 0));
        UIManager.put("Button.font", new FontUIResource("Dialog", Font.BOLD, 24));


        ImageIcon customersI = new ImageIcon("icons/customers.png");
        ImageIcon moneyI = new ImageIcon("icons/money.png");
        ImageIcon addwineI = new ImageIcon("icons/add-wine.png");
        ImageIcon addanalysisI = new ImageIcon("icons/analysis.png");
        ImageIcon viewanalysisI = new ImageIcon("icons/view-analysis.png");
        ImageIcon resultsI = new ImageIcon("icons/results.png");
        ImageIcon statsI = new ImageIcon("icons/statistics-icon.png");
        ImageIcon paramsI = new ImageIcon("icons/configure-icon.png");

        button1 = new JButton("ΠΕΛΑΤΕΣ",customersI);
        panel.add(button1);
        button1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Customers ac = new Customers();
                ac.setVisible(true);
            }
        });     
       
        
        button2 = new JButton("ΠΡΟΒΟΛΗ ΑΝΑΛΥΣΕΩΝ",viewanalysisI);
        panel.add(button2);
        button2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Results r = new Results();
                r.setVisible(true);
            }
        });
        
        button3 = new JButton("ΣΤΑΤΙΣΤΙΚΑ",statsI);
        button3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Statistics s = null;
                try {
                    s = new Statistics();
                } catch (SQLException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                s.setVisible(true);
            }
        });
        panel.add(button3);

        button4 = new JButton("ΟΙΚΟΝΟΜΙΚΑ",moneyI);
        panel.add(button4);
        button4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String code=null;
                code = JOptionPane.showInputDialog(getParent(),"Δωσε κωδικό ασφαλείας");
                Parameters p=null;
                if (code == null) ;
                else if(code.equals("0732"))
                {
                   // System.out.println("Denied");
                    //Access ok
                     CustomersSecure cs = new CustomersSecure();
                     cs.setVisible(true);
                }


                else

                  JOptionPane.showMessageDialog(rootPane, "Λαθος Κωδικός!");

            }
        });


        
        button5 = new JButton("ΡΥΘΜΙΣΗ ΠΑΡΑΜΕΤΡΩΝ",paramsI);
        panel.add(button5);
        button5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String code=null;
                code = JOptionPane.showInputDialog(getParent(),"Δωσε κωδικό ασφαλείας");
                Parameters p=null;
                if (code == null) ;
                else if(code.equals("0732"))
                {
                     p = new Parameters();
                     p.setVisible(true);
                }


                else
                  
                  JOptionPane.showMessageDialog(rootPane, "Λαθος Κωδικός!");

            }
        });

        add(panel,BorderLayout.CENTER);



    }
    
    private void backup() throws IOException, InterruptedException{

                String cmd = "backup.bat";
		Runtime run = Runtime.getRuntime();
		Process pr = run.exec(cmd);
		pr.waitFor();
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			System.out.println(line);
		}

    }

}
