README
=================
Compile and package eimp for eclipse

Compile and package eimp standalone
1)checkout code from CVS
2)use eclipse to compile the sourcecode
3)prepare libs for standalone
4)copy boot.jar,osgi.jar,runtime.jar,jface.jar to eimp/jface
5)download SWT Binary and Source from http://www.eclipse.org
6)unzip swt-xxx-win32.zip to eimp/swt/win32
7)unzip swt-xxx-linux-gtk.zip to eimp/swt/lnx-gtk
8)run ant dist-app-zip-linux-gtk for linux-gtk
9)run ant dist-app-zip-win32 for linux-gtk