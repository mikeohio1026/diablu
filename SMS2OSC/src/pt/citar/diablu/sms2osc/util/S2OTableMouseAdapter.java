package pt.citar.diablu.sms2osc.util;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import pt.citar.diablu.sms2osc.S2O;

public class S2OTableMouseAdapter extends MouseAdapter {

    S2O s2o;

    public S2OTableMouseAdapter(S2O s2o) {
        this.s2o = s2o;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Point p = e.getPoint();
            int row = s2o.getGui().getSmsTable().rowAtPoint(p);
            s2o.getMessagePopUp().setNumber((String) s2o.getGui().getSmsTable().getValueAt(row, 0));
            s2o.getMessagePopUp().setDirection((String) s2o.getGui().getSmsTable().getValueAt(row, 1));
            s2o.getMessagePopUp().setMessage((String) s2o.getGui().getSmsTable().getValueAt(row, 2));
            s2o.getMessagePopUp().setVisible(true);
        }
    }
}
