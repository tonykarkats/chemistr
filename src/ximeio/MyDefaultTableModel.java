package ximeio;

import javax.swing.table.DefaultTableModel;

public class MyDefaultTableModel extends DefaultTableModel{

    public MyDefaultTableModel () {
        super();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
