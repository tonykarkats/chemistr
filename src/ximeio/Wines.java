package ximeio;

//import com.sun.xml.internal.bind.v2.util.EditDistance;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowFocusListener;
import java.util.Vector;
import java.util.logging.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Wines extends JFrame {

    private String[] columnNames = {"Κωδικός Κρασιού","Ημ/νια","No Βαρελιού","Κιλα","Χρώμα","Me","Τυπος Βαρελιου","Πηγη Σταφυλιών"};
    private JTable table;
    private MyDefaultTableModel model;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");
    private JButton add,delete,analysis,update;
    private ImageIcon addI = new ImageIcon("icons/add-2-icon.png");
    private ImageIcon deleteI = new ImageIcon("icons/delete-icon.png");
   
    private ImageIcon analysisI = new ImageIcon("icons/analysis.png");
    private ImageIcon editI = new ImageIcon("icons/Edit-Document-icon.png");


    JPanel panel;
    String selected_id;
    boolean mustUpdate=false,enabledli=true;
    String cid;

    public Wines (final String customerID){


        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        final int x = (dim.width-1024)/ 2;
        final int y = (dim.height-300)/ 2;
        setLocation(x, y);

        cid = customerID;
        setSize(new Dimension(1024,300));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        

         try{
                    Statement stmt = conn.createStatement(true);
                    ResultSet rs = stmt.executeQuery("select * from customer where CustomerID="+customerID);                   
                    rs.next();
                    String customerName = rs.getString("Surname");
                    customerName +=" "+ rs.getString("Name");                    

        setTitle(customerName+" - ΠΡΟΒΟΛΗ ΚΡΑΣΙΩΝ");
        }
        catch(SQLException ex) {

        } ;

        model = new MyDefaultTableModel();
        for(int i = 0 ; i < columnNames.length ; i++ )
            model.addColumn(columnNames[i]);
        initializeTable(customerID);

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        add(table.getTableHeader(),BorderLayout.PAGE_START);
        add(table,BorderLayout.CENTER);
        
        if (table.getRowCount() == 1){
                table.getSelectionModel().setSelectionInterval(0, 0);
                int index;
                Object idOb;
                index =  table.getSelectedRow();
                idOb = model.getValueAt(index, 0);
                selected_id = idOb.toString();   
            
        }

        ListSelectionModel cm = table.getSelectionModel();
        cm.addListSelectionListener(new ListSelectionListener() {

           public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && enabledli) {
                int index;
                Object idOb;
                index =  table.getSelectedRow();
                idOb = model.getValueAt(index, 0);
                selected_id = idOb.toString();
            }
            }
        });


        add= new JButton("Καταχώρηση",addI);

        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AddWine aw = new AddWine(customerID);
                aw.setVisible(true);
                if (aw.mustUpdate == true) mustUpdate = true;
               
            }
        });

        addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e) {
              // if(mustUpdate==true) {
                   updateTable(customerID);
                   if (table.getRowCount() == 1){
                table.getSelectionModel().setSelectionInterval(0, 0);
                int index;
                Object idOb;
                index =  table.getSelectedRow();
                idOb = model.getValueAt(index, 0);
                selected_id = idOb.toString();   
            
        }
                //   mustUpdate = false;
              // }
            }

            public void windowLostFocus(WindowEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
            }
        });


        delete = new JButton("Διαγραφη",deleteI);
        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                    if (selected_id == null){
                        JOptionPane.showMessageDialog(getContentPane(), "Παρακάλώ επιλέξτε βαρέλι.", "Σφάλμα",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    deleteWine(selected_id);

            }
        });

        update = new JButton("Επεξεργασία",editI);
        update.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selected_id == null){
                        JOptionPane.showMessageDialog(getContentPane(), "Παρακάλώ επιλέξτε βαρέλι.", "Σφάλμα",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                UpdateWine uw = new UpdateWine(selected_id);
                uw.setVisible(true);
                if (uw.mustUpdate == true) mustUpdate = true;


            }
        });

        analysis = new JButton("Αναλύσεις",analysisI);
        analysis.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selected_id == null){
                        JOptionPane.showMessageDialog(getContentPane(), "Παρακάλώ επιλέξτε βαρέλι.", "Σφάλμα",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
               Analysis a = new Analysis(selected_id);
               a.setVisible(true);


            }
        });      



        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(add);
        panel.add(delete);
        panel.add(update);
        panel.add(analysis);           
        
        
        add(panel,BorderLayout.SOUTH);

   }

    private void initializeTable(String CustomerID) {
        Vector<String> rowData = new Vector<String>();
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select WineID,DATE_FORMAT(Added,'%d %b %Y') as AddedD,NrBarrel,Kilos,Colour,Me,BarrelType,GrapeSource from Wine where CustomerID="+CustomerID+" and extract(year from Added)= extract(year from CURDATE()) ORDER BY NrBarrel");
                if(!rs.next()) {
                    AddWine aw = new AddWine(CustomerID);
                    aw.setVisible(true);                    
                    if (aw.mustUpdate == true) {mustUpdate = true;}
                   // aw.toFront();
                    aw.attr1.requestFocusInWindow();
                   
                    //aw.requestFocus();
                    this.toFront();
                    
                   
                    
                }
                rs.previous();
                while (rs.next()) {
                    rowData.add(rs.getString("WineID"));
                    rowData.add(rs.getString("AddedD"));
                    rowData.add(rs.getString("NrBarrel"));
                    rowData.add(rs.getString("Kilos"));
                    rowData.add(rs.getString("Colour"));
                    rowData.add(rs.getString("Me"));
                    rowData.add(rs.getString("BarrelType"));
                    rowData.add(rs.getString("GrapeSource"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }                
            } catch (SQLException ex) {

              System.out.println(ex.getMessage());
            }
            

    }


     private void updateTable(String CustomerID) {
         enabledli = false;
        Vector<String> rowData = new Vector<String>();
        if(model.getRowCount() != 0) {
            model = (MyDefaultTableModel) table.getModel();
            model.getDataVector().removeAllElements();
        }
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select WineID,DATE_FORMAT(Added,'%d %b %Y') as AddedD,NrBarrel,Kilos,Colour,Me,BarrelType,GrapeSource from Wine where CustomerID="+CustomerID+" and extract(year from Added)= extract(year from CURDATE()) ORDER BY NrBarrel");
                while (rs.next()) {
                    rowData.add(rs.getString("WineID"));
                    rowData.add(rs.getString("AddedD"));
                    rowData.add(rs.getString("NrBarrel"));
                    rowData.add(rs.getString("Kilos"));
                    rowData.add(rs.getString("Colour"));
                    rowData.add(rs.getString("Me"));
                    rowData.add(rs.getString("BarrelType"));
                    rowData.add(rs.getString("GrapeSource"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
                enabledli = true;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

    }


    public void deleteWine(String id){
        JDialog warnDialog = new JDialog();
                warnDialog.setBounds(500,400,200,200);
                
               int warning = JOptionPane.showConfirmDialog(null, "Είστε βέβαιος?", "Διαγραφή Κρασιού", JOptionPane.YES_NO_CANCEL_OPTION);
                
        if (warning == 0) {
            ResultSet rs3;
        try {

                Statement stmt = conn.createStatement(true);
                rs3 = stmt.executeQuery("select Money from Customer where CustomerID ="+cid);
                rs3.next();
                int oldmoney = Integer.parseInt(rs3.getString("Money"));
                rs3 = stmt.executeQuery("select Money from Wine where WineID="+id);
                rs3.next();
                int newmoney = oldmoney - Integer.parseInt(rs3.getString("Money"));
                stmt.executeUpdate("update Customer set Money="+newmoney+",MoneyOwned="+newmoney+" where CustomerID="+cid);      
                
                
                stmt.executeUpdate("delete from Wine where WineID="+id);

                updateTable(cid);
                table.revalidate();
                table.repaint();
              
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }

    }


    }


}





