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
import org.eclipse.jface.preference.IPreferenceStore;
import org.maze.eimp.Environment;
import org.maze.eimp.aim.AIMConnection;
import org.maze.eimp.im.Account;
import org.maze.eimp.im.Buddy;
import org.maze.eimp.im.UserStatus;
/**
 * @author Vilmar
 * 
 * store the list of sounds to be played according to the event
 *  
 */
public class EventSoundList {
	// event ids
	public static final String Login_Complete = "EventSoundList.loginComplete"; //$NON-NLS-1$
	public static final String BUDDY_ONLINE = "EventSoundList.buddyOnline"; //$NON-NLS-1$
	public static final String BUDDY_OFFLINE = "EventSoundList.buddyOffline"; //$NON-NLS-1$	
	public static final String BUDDY_INVISIBLE = "EventSoundList.buddyInvisible"; //$NON-NLS-1$
	public static final String BUDDY_BUSY = "EventSoundList.buddyBusy"; //$NON-NLS-1$
	public static final String BUDDY_IDLE = "EventSoundList.buddyIdle"; //$NON-NLS-1$
	public static final String BUDDY_AWAY_FROM_COMPUTER = "EventSoundList.buddyAwayFromComputer"; //$NON-NLS-1$
	public static final String BUDDY_BE_RIGHT_BACK = "EventSoundList.buddyBeRightBack"; //$NON-NLS-1$
	public static final String BUDDY_ON_THE_PHONE = "EventSoundList.buddyOnThePhone"; //$NON-NLS-1$	
	public static final String BUDDY_ON_THE_LUNCH = "EventSoundList.buddyOnTheLunch"; //$NON-NLS-1$
	public static final String INSTANT_MESSAGE_RECEIVED = "EventSoundList.instantMessageReceived"; //$NON-NLS-1$
	public static final String SESSION_ENDED = "EventSoundList.sessionEnded"; //$NON-NLS-1$
	public static final String SESSION_STARTED = "EventSoundList.sessionStarted"; //$NON-NLS-1$
	public static final String LOGIN_ERROR = "EventSoundList.loginError"; //$NON-NLS-1$
	public static final String LOGOUT_NOTIFY = "EventSoundList.logoutNotify"; //$NON-NLS-1$
	// the list of sounds
	private Sound[] list;
	/**
	 * @param list
	 *                   The list to set.
	 */
	public void setList(Sound[] list) {
		this.list = list;
	}
	private Sound[] returnEventSoundList() {
		Sound[] l = {new Sound(Login_Complete), new Sound(BUDDY_ONLINE),
				new Sound(BUDDY_OFFLINE), new Sound(BUDDY_AWAY_FROM_COMPUTER),
				new Sound(BUDDY_BE_RIGHT_BACK), new Sound(BUDDY_BUSY),
				new Sound(BUDDY_IDLE), new Sound(BUDDY_INVISIBLE),
				new Sound(BUDDY_ON_THE_LUNCH), new Sound(BUDDY_ON_THE_PHONE),
				new Sound(INSTANT_MESSAGE_RECEIVED), new Sound(SESSION_ENDED),
				new Sound(SESSION_STARTED), new Sound(LOGIN_ERROR),
				new Sound(LOGOUT_NOTIFY)};
		return l;
	}
	/**
	 * searches in the EventSoundList looking for a sound with the id informed
	 * in the parameter
	 * 
	 * @param id
	 *                   <code>id</code> to be searched
	 * @return the <code>sound</code> or <code>null</code>
	 */
	public Sound FindSound(String id) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].getEventId().equals(id))
				return list[i];
		}
		return null;
	}
	/**
	 * play a sound according to the event that is identified by the parameter
	 * id
	 * 
	 * @param id
	 *                   <code>id</code> that identifies the sound
	 */
	public void play(String id) {
		Sound sound = FindSound(id);
		if (sound != null && sound.isEnabled())
			sound.play();
	}
	/**
	 * play a sound according to the status of the buddy
	 * 
	 * @param b
	 *                   <code>buddy</code> that identifies the status to be played
	 */
	public void play(Buddy b) {
		String oldSt = b.getOldStatus();
		if (oldSt == null)
			return;
		String st = b.getStatus();
		Account acc = b.getAccount();
		if (acc!=null && (acc.getConnection() instanceof AIMConnection)) {
			if (st.equals(oldSt))
				return;
		}
		if (st.equals(UserStatus.ONLINE))
			play(BUDDY_ONLINE);
		else if (st.equals(UserStatus.AWAY_FROM_COMPUTER))
			play(BUDDY_AWAY_FROM_COMPUTER);
		else if (st.equals(UserStatus.BE_RIGHT_BACK))
			play(BUDDY_BE_RIGHT_BACK);
		else if (st.equals(UserStatus.BUSY))
			play(BUDDY_BUSY);
		else if (st.equals(UserStatus.IDLE))
			play(BUDDY_IDLE);
		else if (st.equals(UserStatus.INVISIBLE))
			play(BUDDY_INVISIBLE);
		else if (st.equals(UserStatus.OFFLINE))
			play(BUDDY_OFFLINE);
		else if (st.equals(UserStatus.ON_THE_LUNCH))
			play(BUDDY_ON_THE_LUNCH);
		else if (st.equals(UserStatus.ON_THE_PHONE))
			play(BUDDY_ON_THE_PHONE);
	}
	/**
	 * @return Returns the list.
	 */
	public Sound[] getList() {
		return list;
	}
	/**
	 * return in a <code>String</code> the sounds that are enabled
	 * 
	 * @return a list delimited by '|'
	 */
	public String asString() {
		StringBuffer b = new StringBuffer();
		boolean first = true;
		for (int i = 0; i < list.length; i++) {
			Sound sound = list[i];
			if (sound.isEnabled()) {
				if (!first)
					b.append('|');
				else
					first = false;
				b.append(sound.getEventId());
				b.append(';');
				b.append(sound.getFileSoundTitle());
				b.append(';');
				b.append(sound.isEnabled()
						? IPreferenceStore.TRUE
						: IPreferenceStore.FALSE);
			}
		}
		return b.toString();
	}
	private static EventSoundList eventSoundList;
	public static EventSoundList getInstance() {
		if (eventSoundList == null) {
			eventSoundList = new EventSoundList();
			Environment.getInstance().loadSoundsByEvents();
		}
		return eventSoundList;
	}
	public Sound[] getCopyEventSoundList() {
		Sound[] copy = new Sound[list.length];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = list[i].copy();
		}
		return copy;
	}
	private EventSoundList() {
		list = returnEventSoundList();
	}
}