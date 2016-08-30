/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher.swing;

import com.skcraft.launcher.Instance;
import com.skcraft.launcher.InstanceList;
import com.skcraft.launcher.util.SharedLocale;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class InstanceTableModel extends AbstractTableModel {

	private final InstanceList instances;

	public InstanceTableModel(InstanceList instances) {
		this.instances = instances;
	}

	public void update() {
		instances.sort();
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "";
			case 1:
				return SharedLocale.tr("launcher.modpackColumn");
			default:
				return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return Icon.class;
			case 1:
				return String.class;
			default:
				return null;
		}
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				instances.get(rowIndex).setSelected((Boolean) value);
				break;
			case 1:
			default:
				break;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return false;
			case 1:
				return false;
			default:
				return false;
		}
	}

	@Override
	public int getRowCount() {
		return instances.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Instance instance;
		switch (columnIndex) {
			case 0:
				instance = instances.get(rowIndex);
				return instance.getIcon() == null ? new EmptyIcon(16, 16) :  new ImageIcon(instance.getIcon());
			case 1:
				instance = instances.get(rowIndex);
				return instance.getTitle();
			default:
				return null;
		}
	}

}

	