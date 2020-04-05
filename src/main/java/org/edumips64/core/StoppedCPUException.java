/*
 * StoppedCPUException
 *
 * (c) Andrea Spadaccini, 2006
 *
 * Exception thrown when the CPU.step() method is invoked when the CPU is in the
 * STOPPED state.
 *
 * This file is part of the EduMIPS64 project, and is released under the GNU
 * General Public License.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.edumips64.core;

/** Exception thrown when the CPU.step() method is invoked when the CPU is in
 * the STOPPED state.
 *
 * @author Andrea Spadaccini
 */
public class StoppedCPUException extends Exception {
    private static final long serialVersionUID = 4078106941100074156L;
}

