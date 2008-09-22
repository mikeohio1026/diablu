/*
 * DiABlu Mailman
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

package pt.citar.diablu.mailman.util;

import pt.citar.diablu.mailman.MailMan;
import pt.citar.diablu.mailman.util.datastructures.MailManDeviceClass;
import pt.citar.diablu.mailman.util.datastructures.MailManDevice;
import java.util.Vector;


public class MailManGroupGetter {

    MailMan mailman;
    Vector<MailManDevice> allDevices = new Vector<MailManDevice>();
    Vector<MailManDevice> filteredMajor = new Vector<MailManDevice>();
    Vector<MailManDevice> filteredMinor = new Vector<MailManDevice>();
    Vector<MailManDevice> filtered = new Vector<MailManDevice>();

    public MailManGroupGetter(MailMan mailman) {
        this.mailman = mailman;
    }

    public Vector<MailManDevice> getAllDevices() {
        return allDevices;
    }

    public Vector<MailManDevice> getFilteredMajor() {
        return filteredMajor;
    }

    public Vector<MailManDevice> getFilteredMinor() {
        return filteredMinor;
    }

    public Vector<MailManDevice> getFiltered() {
        return filtered;
    }

    public void filterDevices(Object major, Object minor, Object brand) {


        int majorInt;
        int minorInt;

        try {
            majorInt = Integer.parseInt((String) major);
            filterMajorInt(majorInt);
        } catch (NumberFormatException e) {
            filterMajorStr((String) major);
        }

        try {
            minorInt = Integer.parseInt((String) minor);
            filterMinorInt(minorInt);
        } catch (NumberFormatException e) {
            filterMinorStr((String) minor);
        }

        filterBrand((String) brand);

    }

    public void filterMajorInt(int mj) {
        for (MailManDevice d : allDevices) {
            if (d.getMajorClass() == mj) {
                filteredMajor.add(d);
            }
        }
    }

    public void filterMinorInt(int mn) {
        for (MailManDevice d : filteredMajor) {
            if (d.getMajorClass() == mn) {
                filteredMinor.add(d);
            }
        }
    }

    public void filterMajorAndMinorInt(int major, int minor) {
        for (MailManDevice device : filteredMajor) {
            if (device.getMajorClass() == major && device.getMinorClass() == minor) {
                filteredMinor.add(device);
            }
        }
    }

    public void filterMajorStr(String mj) {
        if (mj.compareTo("*") == 0) {
            for (MailManDevice d : allDevices) {
                filteredMajor.add(d);

            }
        } else {
            for (MailManDeviceClass dc : mailman.getMajorDeviceClasses()) {
                if (((String) mj).compareToIgnoreCase(dc.getDescription()) == 0) {
                    filterMajorInt(dc.getMajor());
                }
            }
        }
    }

    public void filterMinorStr(String mn) {
        if (mn.compareTo("*") == 0) {
            for (MailManDevice d : filteredMajor) {
                filteredMinor.add(d);
            }
        } else {
            for (MailManDeviceClass dc : mailman.getMinorDeviceClasses()) {
                if (((String) mn).compareToIgnoreCase(dc.getDescription()) == 0) {
                    filterMajorAndMinorInt(dc.getMajor(), dc.getMinor());

                }
            }
        }
    }
    
    
    
    public void filterBrand(String brand) {
        if (mailman.hasOUI()) {
            if (brand.compareTo("*") == 0) {
                for (MailManDevice device : getFilteredMinor()) {
                    filtered.add(device);
                }


            } else {
                for (MailManDevice device : getFilteredMinor()) {
                    String uuid = device.getUuid().substring(0, 6);
                    if (mailman.getManufacturerOUI().get(uuid.toLowerCase()).contains(brand.toLowerCase())) {
                        filtered.add(device);
                    }

                }
            }
        }
        else
            mailman.getLogger().log(MailManLogger.OTHER, "File \"oui.txt\" not found. Zero devices Returned");
    }
    public void clear()
    {
        allDevices.clear();
        filteredMajor.clear();
        filteredMinor.clear();
        filtered.clear();
    }
}
