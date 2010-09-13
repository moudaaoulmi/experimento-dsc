package org.maze.eimp.sounds;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import br.upe.dsc.reusable.exception.EmptyBlockAbstractExceptionHandling;

public aspect SoundsHandler extends EmptyBlockAbstractExceptionHandling{

	pointcut internalNewSoundHandler(): execution(private void Player.internalNewSound(File));
	
	public pointcut emptyBlockException(): internalNewSoundHandler();
	
	declare soft: UnsupportedAudioFileException: emptyBlockException();
	declare soft: LineUnavailableException: emptyBlockException();
	declare soft: IOException: emptyBlockException();

//	declare soft: UnsupportedAudioFileException: internalNewSoundHandler();
//	declare soft: LineUnavailableException: internalNewSoundHandler();
//	declare soft: IOException: internalNewSoundHandler();

//	void around(): internalNewSoundHandler() {
//		try {
//			proceed();
//		} catch (UnsupportedAudioFileException e) {
//		} catch (LineUnavailableException e) {
//		} catch (IOException e) {
//		}
//	}
}
