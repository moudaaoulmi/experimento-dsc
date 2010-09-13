package lancs.mobilemedia.alternative.music.optional;

import javax.microedition.lcdui.Command;

import lancs.mobilemedia.alternative.music.MusicPlayController;
import lancs.mobilemedia.alternative.music.PlayMusicScreen;
import lancs.mobilemedia.optional.copy.CopyTargets;
import lancs.mobilemedia.optional.copy.CopyMultiMediaAspect;

/**
 * @author Eduardo Figueiredo
 *
 */
public aspect CopyAndMusic extends CopyMultiMediaAspect {
	
	// ********  MusicPlayController  ********* //
	
	declare parents : MusicPlayController extends CopyTargets;

	//public boolean handleCommand(Command command)
	public pointcut handleCommandAction(CopyTargets controller, Command c):
		execution(public boolean MusicPlayController.handleCommand(Command)) && args(c) && this(controller);
	
	// ********  PlayMusicScreen  ********* //
	
	// [NC] Added in the scenario 07
	public static final Command copy = new Command("Copy", Command.ITEM, 1);

	//private void initForm(AbstractController controller)
	pointcut initForm(PlayMusicScreen mediaScreen):
		execution(private void PlayMusicScreen.initForm()) && this(mediaScreen);

	before(PlayMusicScreen mediaScreen) : initForm(mediaScreen) {
		// [NC] Added in the scenario 07
		mediaScreen.form.addCommand(copy);
	}
}
