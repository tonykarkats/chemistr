package ximeio;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowFocusListener;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
//import sun.swing.table.DefaultTableCellHeaderRenderer;


public class Customers extends JFrame {

    private String[] columnNames = {"Κωδικός","Επωνυμο","Όνομα","Πατρώνυμο","Παρώνυμο","Διεύθυνση","Σταθερό","Κινητό","Email",
                                   "ΑΦΜ","ΔΟΥ"};
    private JTable table;
    private MyDefaultTableModel model;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");
    private JButton b,search,delete,edit,wines,oldwines;
    private ImageIcon addcustomerI = new ImageIcon("icons/add-customer.png");
    private ImageIcon deleteI = new ImageIcon("icons/Remove-Male-User-icon.png");
    private ImageIcon searchI = new ImageIcon("icons/search-icon.png");
    private ImageIcon editI = new ImageIcon("icons/edit-icon.png");
    private ImageIcon winesI = new ImageIcon("icons/add-wine.png");
    private ImageIcon oldwinesI = new ImageIcon("icons/Cellar-Closed-icon.png");

    JPanel panel;
    JTextField text;
    String searchStr;
    String selected_id;
    boolean mustUpdate=false;
    JScrollPane sc;
    boolean enabledli=true;




    public Customers (){


     //  UIManager.put("Button.font", new FontUIResource("Dialog", Font.BOLD, 17));

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        setSize(new Dimension(dim.width,dim.height-50)); // Change height- XX to fit fullscreen
        setLayout(new BorderLayout());
        setTitle("ΠΕΛΑΤΕΣ");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        UIManager.put("Button.font", new FontUIResource("Dialog", Font.BOLD, 17));      
        addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {

                //System.out.println(e.getKeyChar());
            }

            public void keyPressed(KeyEvent e) {
                //System.out.println(e.getKeyChar());
            }

            public void keyReleased(KeyEvent e) {
                //System.out.println(e.getKeyChar());
            }
        });

            ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);


        model = new MyDefaultTableModel();
        for(int i = 0 ; i < columnNames.length ; i++ ){
            model.addColumn(columnNames[i]);

        }
        initializeTable(); 
       

        table = new JTable(model);
        for(int i = 0 ; i < columnNames.length ; i++ ){
        TableColumn column = table.getColumn(columnNames[i]);
        column.setHeaderRenderer(new MyHeaderRenderer());
        }

        TableColumn column = table.getColumn(columnNames[0]);
        column.setPreferredWidth(5);

        TableColumn column1 = table.getColumn(columnNames[1]);
        column1.setPreferredWidth(150);

        table.setFont(new Font("Arial", Font.PLAIN, 14));

      
         sc = new JScrollPane(table);        
        add(sc,BorderLayout.CENTER);

        ListSelectionModel cm = table.getSelectionModel();
        ListSelectionListener lsl = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && enabledli) {
                int index;
                Object idOb;
                index =  table.getSelectedRow();                
                idOb = model.getValueAt(index, 0);
                selected_id = idOb.toString();
            }
            }

        };
        cm.addListSelectionListener(lsl);


            
      
       
        
        b= new JButton("Καταχώρηση",addcustomerI);
        //b.setPreferredSize(new Dimension(100, 50));
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AddCustomer ac = new AddCustomer();               
                ac.setVisible(true);
                if (ac.mustUpdate == true) mustUpdate = true;           }
        });        

        addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e) {
               if(mustUpdate==true) {
                   //dispose();
                   //Customers c = new Customers();
                   //c.setVisible(true);
                   updateTable();
                   mustUpdate = false;
               }
            }

            public void windowLostFocus(WindowEvent e) {
            }
        });

           
        delete = new JButton("Διαγραφη ",deleteI);
        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                    if (selected_id == null){
                        JOptionPane.showMessageDialog(getContentPane(), "Παρακαλώ επιλέξτε πελάτη", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    deleteCustomer(selected_id);                   

            }
        });


        edit = new JButton("Επεξεργασία",editI);
        edit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selected_id == null){
                        JOptionPane.showMessageDialog(getContentPane(), "Παρακαλώ επιλέξτε πελάτη", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                
                UpdateCustomer uc =new UpdateCustomer(selected_id);
                System.out.println(selected_id);
                uc.setVisible(true);
                if (uc.mustUpdate == true) mustUpdate = true;               

            }
        });

        oldwines = new JButton("Παλαιότερα Κρασιά",oldwinesI);
        oldwines.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selected_id == null){
                        JOptionPane.showMessageDialog(getContentPane(), "Παρακαλώ επιλέξτε πελάτη", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                String year = JOptionPane.showInputDialog("Δώσε Χρονιά");
                WineOld wo = new WineOld(selected_id, year);
                wo.setVisible(true);
            }
        });


        text = new JTextField("Πληκτρολογήστε εδώ");
        text.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                text.setText("");
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
                text.setText("Πληκτρολογήστε εδώ");
            }
        });
        text.setPreferredSize(new Dimension(200, 40));
        text.setFont(new Font("Arial", Font.PLAIN, 15));
        search = new JButton("Αναζήτηση",searchI);
        getRootPane().setDefaultButton(search);
        search.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                 searchStr = text.getText();
                 CustomerSearchResults csr= new CustomerSearchResults(searchStr);
                 csr.setVisible(true);
                 mustUpdate = true;                 
                //dispose();


            }
        });


        wines = new JButton("Κρασια Πελάτη",winesI);
        wines.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selected_id == null){
                        JOptionPane.showMessageDialog(getContentPane(), "Παρακαλώ επιλέξτε πελάτη", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                Wines wi = new Wines(selected_id);
                wi.setVisible(true);


            }
        });

        
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 4));
        //panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(b);
        panel.add(wines);
        panel.add(oldwines);
        panel.add(new JButton());
        panel.add(delete);
        panel.add(edit);         
        panel.add(search);
        panel.add(text);
        getContentPane().add(panel,BorderLayout.SOUTH);
    }

    private void initializeTable() {


        Vector<String> rowData = new Vector<String>();
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select CustomerID,Surname,Name,FatherName,Nickname,Address,Phone1,Phone2,mail,afm,doy from Customer order by Surname");
                while (rs.next()) {
                    rowData.add(rs.getString("CustomerID"));
                    rowData.add(rs.getString("Surname"));
                    rowData.add(rs.getString("Name"));
                    rowData.add(rs.getString("FatherName"));
                    rowData.add(rs.getString("Nickname"));
                    rowData.add(rs.getString("Address"));
                    rowData.add(rs.getString("Phone1"));
                    rowData.add(rs.getString("Phone2"));
                    rowData.add(rs.getString("mail"));
                    rowData.add(rs.getString("afm"));
                    rowData.add(rs.getString("doy"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
            } catch (SQLException ex) {

              //  Logger.getLogger(Musician.class.getName()).log(Level.SEVERE, null, ex);
            }

    }  



     private void updateTable() {

        enabledli = false;
        Vector<String> rowData = new Vector<String>();
        if(model.getRowCount() != 0) {
            model = (MyDefaultTableModel) table.getModel();
            model.getDataVector().removeAllElements();
        }
       
       

            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select CustomerID,Surname,Name,FatherName,Nickname,Address,Phone1,Phone2,mail,afm,doy from Customer order by Surname");
                while (rs.next()) {
                    rowData.add(rs.getString("CustomerID"));
                    rowData.add(rs.getString("Surname"));
                    rowData.add(rs.getString("Name"));
                    rowData.add(rs.getString("FatherName"));
                    rowData.add(rs.getString("Nickname"));
                    rowData.add(rs.getString("Address"));
                    rowData.add(rs.getString("Phone1"));
                    rowData.add(rs.getString("Phone2"));
                    rowData.add(rs.getString("mail"));
                    rowData.add(rs.getString("afm"));
                    rowData.add(rs.getString("doy"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
                enabledli = true;
                // System.out.println(model.getRowCount()+"EDW");
                
            } catch (SQLException ex) {

              //  Logger.getLogger(Musician.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

    public void deleteCustomer(String id){
        JDialog warnDialog = new JDialog();
                warnDialog.setBounds(500,400,200,200);               
               int warning = JOptionPane.showConfirmDialog(null, "Θα διαγραφεί ο πελάτης και όλα τα κρασια του.\n Είστε βέβαιος?", "Διαγραφή Πελάτη", JOptionPane.YES_NO_CANCEL_OPTION);
                //Object answer = warning.getSelectionValues();
                // warnDialog.add(warning);
              //  warnDialog.setVisible(true);
            //     System.out.println(warning);
        if (warning == 0) {
        try {          

                Statement stmt = conn.createStatement(true);
               // System.out.println(selected_id);
                stmt.executeUpdate("delete from Customer where CustomerID="+id);

                
                updateTable();                
               // mustUpdate = true;
                
           //     dispose();
           //    Customers c = new Customers();
            //   c.setVisible(true);
               
              //  System.out.println(selected_id);

        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
       
    }


    }

}





