/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 *
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package chrriis.dj.nativeswing.swtimpl;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import chrriis.common.Utils;

/**
 * @author Christopher Deckers
 */
public class ModalDialogUtils {

  public static void showModalDialog(Window window, final NativeComponent nativeComponent, final Runnable runnable) {
    final JDialog dialog;
    if(Utils.IS_JAVA_6_OR_GREATER) {
      dialog = new JDialog(window, ModalityType.APPLICATION_MODAL);
    } else {
      if(window instanceof Dialog) {
        dialog = new JDialog((Dialog)window, true);
      } else {
        dialog = new JDialog((Frame)window, true);
      }
    }
    dialog.setUndecorated(true);
    dialog.setSize(0, 0);
    dialog.setLocationRelativeTo(window);
    dialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        dialog.getContentPane().add(nativeComponent.createEmbeddableComponent(new HashMap<Object, Object>()), BorderLayout.CENTER);
        nativeComponent.initializeNativePeer();
        try {
          runnable.run();
        } finally {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              dialog.dispose();
            }
          });
        }
      }
    });
    dialog.setVisible(true);
  }

}
