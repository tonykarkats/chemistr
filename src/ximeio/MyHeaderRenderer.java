package ximeio;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class MyHeaderRenderer implements TableCellRenderer
{
public Component getTableCellRendererComponent(JTable table,
Object value, boolean isSelected, boolean hasFocus,
int row, int column)
{
String txt = value.toString();
JLabel label = new JLabel("<html><body><font size=4 face=TimesNewRoman><b>" + txt +
"</b></font></body></html>");
return label;
}
    }