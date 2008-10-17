 /*
 * org.openmicroscopy.shoola.agents.editor.browser.TextAreasView 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2008 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.agents.editor.browser;

//Java imports

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.Scrollable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

//Third-party libraries

//Application-internal dependencies

import org.openmicroscopy.shoola.agents.editor.model.Field;
import org.openmicroscopy.shoola.agents.editor.model.IField;
import org.openmicroscopy.shoola.agents.editor.model.TreeIterator;

/** 
 * This is the UI panel that displays the tree model in a "Text Document" 
 * representation.
 * Contains a number of text components, corresponding to the fields (or nodes)
 * of the tree, but these are not arranged hierarchically. 
 *
 * @author  William Moore &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:will@lifesci.dundee.ac.uk">will@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class TextAreasView
	extends JPanel 
	implements Scrollable,
	TreeModelListener
	{
	
	/**
	 * A reference to the tree UI used for selection coordination.
	 */
	private JTree 				navTree;
	
	/**
	 * Controller for edits etc.
	 */
	private BrowserControl 		controller;
	
	/**
	 * The model that this UI represents. 
	 * This class is a {@link TreeModelListener} of this treeModel. 
	 */
	private TreeModel 			treeModel;
	 
	/**
	 * Refreshes the contents of this UI. 
	 * Removes all the text components and rebuilds the UI with new 
	 * text components based on the {@link #treeModel}. 
	 * This method is called when the {@link #treeModel} changes structure. 
	 */
	private void refreshTreeDisplay() {
		
		removeAll();
		
		if (treeModel != null)	{
		
			Object r = treeModel.getRoot();
			if (! (r instanceof TreeNode)) 		return;
			TreeNode root = (TreeNode)r;
			
			Iterator<TreeNode> iterator = new TreeIterator(root);
			
			TreeNode tn;
			IField f;
			JTextComponent tc;
			while (iterator.hasNext()) {
				tn = iterator.next();
				if (tn instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)tn;
					Object userOb = node.getUserObject();
					if (userOb instanceof IField) {
						f = (Field)userOb;
						tc = new FieldTextArea(f, navTree, node, controller);
						add(tc);
					}
				}
			}
		}
		
		JPanel spacer = new JPanel();
		spacer.setBackground(null);
		add(spacer);
		revalidate();
		repaint();
	}

	/**
	 * Iterates through all the text components contained in this UI and 
	 * if they are {@link FieldTextArea} objects, the text is refreshed.
	 * This method is called when the {@link treeModel} nodes change, but
	 * the structure is the same. 
	 */
	private void refreshTextAreas()
	{
		Component comp;
		for (int i=0; i<getComponentCount(); i++) {
			comp = getComponent(i);
			if (comp instanceof FieldTextArea) {
				((FieldTextArea)comp).refreshText();
			}
		}
	}

	/**
	 * Creates an instance. 
	 * 
	 * @param tree			Navigation tree for selection
	 * @param controller	Controller for edits.
	 */
	TextAreasView(JTree tree, BrowserControl controller) 
	{
		this.navTree = tree;
		this.controller = controller;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.white);
		setBorder(new EmptyBorder(10,10,10,10));
	}
	
	/**
	 * Sets the model and calls {@link #refreshTreeDisplay()}
	 * 
	 * @param treeModel		The new treeModel
	 */
	void setTreeModel(TreeModel treeModel)
	{
		this.treeModel = treeModel;
		if (treeModel != null)
			treeModel.addTreeModelListener(this);
		
		refreshTreeDisplay();
	}
	
	/**
	 * Implemented as specified by the {@link Scrollable} interface.
	 * Null implementation. 
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return null;
	}

	/**
	 * Implemented as specified by the {@link Scrollable} interface.
	 * Null implementation. 
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 0;
	}

	/**
	 * Implemented as specified by the {@link Scrollable} interface.
	 * Null implementation. 
	 */
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	/**
	 * Implemented as specified by the {@link Scrollable} interface.
	 * Returns true, so that this UI component resizes to the width of
	 * the split pane.  
	 */
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	/**
	 * Implemented as specified by the {@link Scrollable} interface.
	 * Null implementation. 
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 0;
	}

	/**
	 * Implemented as specified by the {@link TreeModelListener} interface.
	 * Calls {@link #refreshTextAreas()} to refresh the text within each
	 * existing node / field.
	 * 
	 * @see TreeModelListener#treeNodesChanged(TreeModelEvent)
	 */
	public void treeNodesChanged(TreeModelEvent e) {
		refreshTextAreas();
	}

	/**
	 * Implemented as specified by the {@link TreeModelListener} interface.
	 * Calls {@link #refreshTreeDisplay()} to rebuild the UI with the 
	 * new structure of the {@link #treeModel};
	 * 
	 * @see TreeModelListener#treeNodesInserted(TreeModelEvent)
	 */
	public void treeNodesInserted(TreeModelEvent e) {
		refreshTreeDisplay();
	}

	/**
	 * Implemented as specified by the {@link TreeModelListener} interface.
	 * Calls {@link #refreshTreeDisplay()} to rebuild the UI with the 
	 * new structure of the {@link #treeModel};
	 * 
	 * @see TreeModelListener#treeNodesRemoved(TreeModelEvent)
	 */
	public void treeNodesRemoved(TreeModelEvent e) {
		refreshTreeDisplay();
	}

	/**
	 * Implemented as specified by the {@link TreeModelListener} interface.
	 * Calls {@link #refreshTreeDisplay()} to rebuild the UI with the 
	 * new structure of the {@link #treeModel};
	 * 
	 * @see TreeModelListener#treeStructureChanged(TreeModelEvent)
	 */
	public void treeStructureChanged(TreeModelEvent e) {
		refreshTreeDisplay();
	}

}
