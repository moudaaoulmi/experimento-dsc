package org.maze.eimp.sounds;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public aspect SoundsHandler {

	pointcut internalNewSoundHandler(): execution(private void Player.internalNewSound(File));

	declare soft:UnsupportedAudioFileException:internalNewSoundHandler();
	declare soft:LineUnavailableException:internalNewSoundHandler();
	declare soft:IOException:internalNewSoundHandler();

	void around(): internalNewSoundHandler() {
		try {
			proceed();
		} catch (UnsupportedAudioFileException e) {
		} catch (LineUnavailableException e) {
		} catch (IOException e) {
		}
	}

}
