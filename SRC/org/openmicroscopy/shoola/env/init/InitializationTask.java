/*
 * org.openmicroscopy.shoola.env.init.InitializationTask
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.env.init;

//Java imports

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.Container;

/** 
 * Declares the interface for configuring and executing an initialization
 * task.
 * <p>Tasks are encapsulated by objects and treated as commands by the
 * {@link Initializer}, which first configures them and then executes them all.
 * </p>
 * <p>This class factors out a reference to the {@link Container} singleton, as
 * this object is needed by all initialization tasks to perform their activity.
 * </p>
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * <br><b>Internal version:</b> $Revision$ $Date$
 * @version 2.2
 * @since OME2.2
 */
abstract class InitializationTask
{
	/** Reference to the singleton {@link Container}. */
	protected Container		container;
	
	/**
	 * Forces every subclass to have this constructor.
	 * 
	 * @param c	Reference to the singleton {@link Container}.
	 */
	protected InitializationTask(Container c)
	{
		container = c;
	}
	
	/**
	 * Returns the name of this task.
	 * 
	 * @return	See above.
	 */
	abstract String getName();
	
	/**
	 * Prepare the task for execution.
	 * This method is called before {@link #execute()}. 
	 *
	 */
	abstract void configure();
	
	/**
	 * Carries out the initialization task.
	 * 
	 * @throws StartupException	If an error occurs.
	 */
	abstract void execute() throws StartupException;
}
