package lancs.mobilemedia.alternative.video.optional;

import javax.microedition.lcdui.Command;

import lancs.mobilemedia.alternative.video.PlayVideoController;
import lancs.mobilemedia.alternative.video.PlayVideoScreen;
import lancs.mobilemedia.optional.copy.CopyTargets;
import lancs.mobilemedia.optional.copy.CopyMultiMediaAspect;

public aspect CopyAndVideo extends CopyMultiMediaAspect {
	
	// ********  PlayVideoController  ********* //
	
	// [NC] Added in the scenario 07
	declare parents : PlayVideoController extends CopyTargets;

	//public boolean handleCommand(Command command)
	public pointcut handleCommandAction(CopyTargets controller, Command c):
		execution(public boolean PlayVideoController.handleCommand(Command)) && args(c) && this(controller);
	
	
	// ********  PlayMusicScreen  ********* //
	
	// [NC] Added in the scenario 07
	public static final Command copy = new Command("Copy", Command.ITEM, 1);

	//private void initForm(AbstractController controller)
	pointcut initForm(PlayVideoScreen mediaScreen):
		execution(private void PlayVideoScreen.initForm()) && this(mediaScreen);

	before(PlayVideoScreen mediaScreen) : initForm(mediaScreen) {
		// [NC] Added in the scenario 07
		mediaScreen.addCommand(copy);
	}
}
