/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package peersim.core;

/**
 * Interface to identify protocols.
 * 
 * @author Alberto Montresor
 * @version $Revision$
 */
public interface Protocol extends Cloneable
{

/**
 * Returns a clone of the protocol. It is important to pay attention to
 * implement this carefully because in peersim all nodes are generated by
 * cloning except a prototype node. That is, the constructor of protocols is
 * used only to construct the prototype. Initialization can be done
 * via {@link Control}s.
 */
public Object clone();

}
