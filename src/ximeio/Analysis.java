package ximeio;

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


public class Analysis extends JFrame {

    private String[] columnNames = {"Κωδικός Ανάλυσης","Αναλυση","Ημερομηνία Ανάλυσης","Baume","Alcohol","Final Alcohol","pH","Oxy","TotSulf","FreSulf","A","Fe"};
    private JTable table;
    private MyDefaultTableModel model;
    DBConnection conn = new DBConnection("localhost", "root", "230358", "chemistr");;
    private JButton add,delete,results;
   // private ImageIcon addcustomerI = new ImageIcon("icons/add-customer.png");
    JPanel panel;
    String selected_id;
    boolean mustUpdate=false,enabledli=true;
    String wid;

    public Analysis (final String wineID){

        wid = wineID;
        setSize(new Dimension(900,300));
        setLayout(new BorderLayout());
        setTitle("ΠΡΟΒΟΛΗ ΑΝΑΛΥΣΕΩΝ");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

           ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        };
        KeyStroke stroke   = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(al, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        model = new MyDefaultTableModel();
        for(int i = 0 ; i < columnNames.length ; i++ )
            model.addColumn(columnNames[i]);
        initializeTable(wineID);

        table = new JTable(model);
        add(table.getTableHeader(),BorderLayout.PAGE_START);
        add(table,BorderLayout.CENTER);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getColumn(columnNames[0]).setPreferredWidth(5);
        table.getColumn(columnNames[1]).setPreferredWidth(10);
        table.getColumn(columnNames[3]).setPreferredWidth(70);
        
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


        add= new JButton("Καταχώρηση Νεας Ανάλυσης");

        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               AddAnalysis aa = new AddAnalysis(wineID);                
                aa.setVisible(true);
                if (aa.mustUpdate == true) mustUpdate = true;         

            }
        });

        addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e) {
               if(mustUpdate==true) {
                   updateTable(wineID);
                   mustUpdate = false;
                   if (table.getRowCount() == 1){
                table.getSelectionModel().setSelectionInterval(0, 0);
                int index;
                Object idOb;
                index =  table.getSelectedRow();
                idOb = model.getValueAt(index, 0);
                selected_id = idOb.toString();   
            
        }
               }
            }

            public void windowLostFocus(WindowEvent e) {
               // throw new UnsupportedOperationException("Not supported yet.");
            }
        });


        delete = new JButton("Διαγραφη Αναλυσης");
        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                
                     if (selected_id == null){
                        JOptionPane.showMessageDialog(getContentPane(), "Παρακάλώ επιλέξτε Ανάλυση.", "Σφάλμα",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    deleteAnalysis(selected_id);
            }
        });
        
        results  = new JButton("Αποτελέσματα");
        results.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                 if (selected_id == null){
                        JOptionPane.showMessageDialog(getContentPane(), "Παρακάλώ επιλέξτε Ανάλυση.", "Σφάλμα",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    DrugsPanel dp = new DrugsPanel(selected_id);
                    dp.setVisible(true);
            }
        });






        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(add);
        panel.add(delete);
        panel.add(results);
          



        add(panel,BorderLayout.SOUTH);






    }

    private void initializeTable(String WineID) {
        Vector<String> rowData = new Vector<String>();
            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select AnalysisID,AnalysisNr,DATE_FORMAT(AnalysisDate,'%d / %m / %Y') as AnalysisDate,BaumeC,AlcoholC,FinalAlcohol,Ph,Oxy,TotalSulfur,FreeSulfur,A,Fe from Analysis where WineID="+WineID);
                while (rs.next()) {
                    rowData.add(rs.getString("AnalysisID"));
                    rowData.add(rs.getString("AnalysisNr"));
                    rowData.add(rs.getString("AnalysisDate"));
                    rowData.add(rs.getString("BaumeC"));
                    rowData.add(rs.getString("AlcoholC"));
                    rowData.add(rs.getString("FinalAlcohol"));
                    rowData.add(rs.getString("Ph"));
                    rowData.add(rs.getString("Oxy"));
                    rowData.add(rs.getString("TotalSulfur"));
                    rowData.add(rs.getString("FreeSulfur"));
                    rowData.add(rs.getString("A"));
                    rowData.add(rs.getString("Fe"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }             

                
            } catch (SQLException ex) {

              System.out.println(ex.getMessage());
            }

    }

    private void updateTable(String WineID) {
        enabledli = false;
        Vector<String> rowData = new Vector<String>();
        if(model.getRowCount() != 0) {
            model = (MyDefaultTableModel) table.getModel();
            model.getDataVector().removeAllElements();
        }

            try {
                Statement stmt = conn.createStatement(true);
                ResultSet rs = stmt.executeQuery("select AnalysisID,AnalysisNr,DATE_FORMAT(AnalysisDate,'%d / %m / %Y') as AnalysisDate,BaumeC,AlcoholC,FinalAlcohol,Ph,Oxy,TotalSulfur,FreeSulfur,A,Fe from Analysis where WineID="+WineID);
                while (rs.next()) {
                    rowData.add(rs.getString("AnalysisID"));
                    rowData.add(rs.getString("AnalysisNr"));
                    rowData.add(rs.getString("AnalysisDate"));
                    rowData.add(rs.getString("BaumeC"));
                    rowData.add(rs.getString("AlcoholC"));
                    rowData.add(rs.getString("FinalAlcohol"));
                    rowData.add(rs.getString("Ph"));
                    rowData.add(rs.getString("Oxy"));
                    rowData.add(rs.getString("TotalSulfur"));
                    rowData.add(rs.getString("FreeSulfur"));
                    rowData.add(rs.getString("A"));
                    rowData.add(rs.getString("Fe"));
                    model.addRow(rowData);
                    rowData = new Vector<String>();
                }
             enabledli = true;

            } catch (SQLException ex) {

              System.out.println(ex.getMessage());
            }

    }


    public void deleteAnalysis(String id){
        JDialog warnDialog = new JDialog();
                warnDialog.setBounds(500,400,200,200);
                //warnDialog.setSize(200, 200);
               int warning = JOptionPane.showConfirmDialog(null, "Ειστε βέβαιος?", "Διαγραφή Ανάλυσης", JOptionPane.YES_NO_CANCEL_OPTION);
                //Object answer = warning.getSelectionValues();
                // warnDialog.add(warning);
              //  warnDialog.setVisible(true);
            //     System.out.println(warning);
        if (warning == 0) {
        try {

                Statement stmt = conn.createStatement(true);
                stmt.executeUpdate("delete from Analysis where AnalysisID="+id);                
                updateTable(wid);
                table.repaint();

        }
        catch(SQLException ex){
            // add here
        }

    }


    }

}





