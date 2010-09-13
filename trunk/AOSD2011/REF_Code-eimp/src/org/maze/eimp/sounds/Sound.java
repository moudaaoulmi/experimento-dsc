/*******************************************************************************
 * Copyright (c) 2004, Loya Liu
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the MAZE.ORG nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************/
package org.maze.eimp.sounds;
import org.maze.eimp.app.Messages;
/**
 * @author Vilmar
 * 
 * encapsulate a sound that must be played in a specific event
 *  
 */
public class Sound {
	private Player player; // the player used to play the sound
	private String eventId; // identifies the event
	private String eventDescription; // describe the event
	private String fileSoundTitle; // the sound to be played
	private boolean enabled;
	/**
	 * @return Returns the enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled
	 *                   The enabled to set.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Sound(String eventId) {
		this.eventId = eventId;
		this.eventDescription = Messages.getString(eventId);
		player = Player.getInstance();
		fileSoundTitle = "";
	}
	/**
	 * @return Returns the eventDescription.
	 */
	public String getEventDescription() {
		return eventDescription;
	}
	/**
	 * @param eventDescription
	 *                   The eventDescription to set.
	 */
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	/**
	 * @return Returns the fileSoundTitle.
	 */
	public String getFileSoundTitle() {
		return fileSoundTitle;
	}
	/**
	 * @param fileSoundTitle
	 *                   The fileSoundTitle to set.
	 */
	public void setFileSoundTitle(String fileSoundTitle) {
		this.fileSoundTitle = fileSoundTitle;
	}
	/**
	 * @return Returns the eventId.
	 */
	public String getEventId() {
		return eventId;
	}
	/**
	 * play the sound
	 */
	public void play() {
		if (isEnabled()) {
			player.play(getFileSoundTitle());
		}
	}
	/**
	 * @return a copy of this object
	 */
	public Sound copy() {
		Sound s = new Sound(eventId);
		s.setEnabled(isEnabled());
		s.setEventDescription(getEventDescription());
		s.setFileSoundTitle(getFileSoundTitle());
		return s;
	}
}