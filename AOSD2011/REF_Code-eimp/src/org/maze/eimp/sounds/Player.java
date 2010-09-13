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

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine.Info;

/**
 * @author Vilmar
 * 
 *         play a sound
 * 
 */
public class Player {
	private Clip clip;

	private void newSound(String fileName) {
		File soundFile = new File(fileName);
		internalNewSound(soundFile);
	}

	private void internalNewSound(File soundFile) {
		AudioInputStream source = AudioSystem.getAudioInputStream(soundFile);
		Info clipInfo = new Info(Clip.class, source.getFormat());
		if (AudioSystem.isLineSupported(clipInfo)) {
			Clip newClip = (Clip) AudioSystem.getLine(clipInfo); // Create
			newClip.open(source);
			if (clip != null) {
				if (clip.isActive()) {
					clip.stop();
				}
				if (clip.isOpen())
					clip.close();
			}
			clip = newClip;
		} else {
		}
	}

	public void play() {
		if (clip != null)
			clip.loop(0);
	}

	public void play(String fileName) {
		if (fileName == null)
			return;
		newSound(fileName);
		play();
	}

	private Player() {
	}

	private static Player player = null;

	public static Player getInstance() {
		if (player == null)
			player = new Player();
		return player;
	}
}