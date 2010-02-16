/*
 * org.openmicroscopy.shoola.env.data.views.AdminViewImpl 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2010 University of Dundee. All rights reserved.
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
package org.openmicroscopy.shoola.env.data.views;


//Java imports
import java.util.List;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.data.model.AdminObject;
import org.openmicroscopy.shoola.env.data.views.calls.AdminLoader;
import org.openmicroscopy.shoola.env.data.views.calls.AdminSaver;
import org.openmicroscopy.shoola.env.event.AgentEventListener;
import pojos.DataObject;
import pojos.ExperimenterData;
import pojos.GroupData;

/** 
 * Implementation of the {@link AdminView} implementation.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since 3.0-Beta4
 */
class AdminViewImpl 
	implements AdminView
{

	/**
	 * Implemented as specified by the {@link AdminView} interface.
	 * @see AdminView#updateExperimenter(ExperimenterData, 
	 * 										AgentEventListener)
	 */
	public CallHandle updateExperimenter(ExperimenterData exp, 
			AgentEventListener observer)
	{
		BatchCallTree cmd = new AdminLoader(exp);
		return cmd.exec(observer);
	}

	/**
	 * Implemented as specified by the {@link AdminView} interface.
	 * @see AdminView#updateGroup(GroupData, AgentEventListener)
	 */
	public CallHandle updateGroup(GroupData group, AgentEventListener observer)
	{
		BatchCallTree cmd = new AdminLoader(group);
		return cmd.exec(observer);
	}
	
	/**
	 * Implemented as specified by the {@link AdminView} interface.
	 * @see AdminView#changePassword(String, String, AgentEventListener)
	 */
	public CallHandle changePassword(String oldPassword, String newPassword, 
			AgentEventListener observer)
	{
		BatchCallTree cmd = new AdminLoader(oldPassword, newPassword);
		return cmd.exec(observer);
	}


	/**
	 * Implemented as specified by the {@link AdminView} interface.
	 * @see AdminView#getDiskSpace(long, AgentEventListener)
	 */
	public CallHandle getDiskSpace(long userID, AgentEventListener observer)
	{
		BatchCallTree cmd = new AdminLoader(userID, AdminLoader.SPACE);
		return cmd.exec(observer);
	}

	/**
	 * Implemented as specified by the {@link AdminView} interface.
	 * @see AdminView#createExperimenters(AdminObject, AgentEventListener)
	 */
	public CallHandle createExperimenters(AdminObject object,
			AgentEventListener observer)
	{
		BatchCallTree cmd = new AdminSaver(object);
		return cmd.exec(observer);
	}

	/**
	 * Implemented as specified by the {@link AdminView} interface.
	 * @see AdminView#createGroup(AdminObject, AgentEventListener)
	 */
	public CallHandle createGroup(AdminObject object,
			AgentEventListener observer)
	{
		BatchCallTree cmd = new AdminSaver(object);
		return cmd.exec(observer);
	}

	/**
	 * Implemented as specified by the {@link AdminView} interface.
	 * @see AdminView#loadExperimenterGroups(AgentEventListener)
	 */
	public CallHandle loadExperimenterGroups(AgentEventListener observer)
	{
		BatchCallTree cmd = new AdminLoader(-1, AdminLoader.GROUPS);
		return cmd.exec(observer);
	}

	/**
	 * Implemented as specified by the {@link AdminView} interface.
	 * @see AdminView#loadExperimenters(long, AgentEventListener)
	 */
	public CallHandle loadExperimenters(long groupID,
			AgentEventListener observer)
	{
		BatchCallTree cmd = new AdminLoader(groupID, AdminLoader.EXPERIMENTERS);
		return cmd.exec(observer);
	}

	/**
	 * Implemented as specified by the {@link AdminView} interface.
	 * @see AdminView#deleteObjects(List, AgentEventListener)
	 */
	public CallHandle deleteObjects(List<DataObject> objects,
			AgentEventListener observer)
	{
		BatchCallTree cmd = new AdminSaver(objects, AdminSaver.DELETE);
		return cmd.exec(observer);
	}

}
