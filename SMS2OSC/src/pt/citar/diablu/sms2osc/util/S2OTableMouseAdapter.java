/*
 * DiABlu SMS2OSC
 * Copyright (C) 2008-2009, CITAR (Research Centre for Science and Technology in Art)
 *
 * This is part of the DiABlu Project, created by Jorge Cardoso - http://diablu.jorgecardoso.eu
 *
 *
 * Contributors:
 * - Pedro Santos <psantos@porto.ucp.pt>
 * - Jorge Cardoso <jccardoso@porto.ucp.pt>
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */

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
