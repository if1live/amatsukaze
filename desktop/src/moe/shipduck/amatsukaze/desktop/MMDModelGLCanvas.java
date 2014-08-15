package moe.shipduck.amatsukaze.desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.yzwlab.javammd.model.MMDModel;

/**
 * A minimal program that draws with JOGL in a Swing JFrame using the AWT
 * GLCanvas.
 * 
 * @author Wade Walker
 */
@SuppressWarnings("deprecation")
public class MMDModelGLCanvas {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) throws Exception {
		GLProfile glprofile = GLProfile.getDefault();
		GLCapabilities glcapabilities = new GLCapabilities(glprofile);
		JPanel panel = new JPanel(new FlowLayout());
		long baseTime = System.currentTimeMillis();
		MMDModel model = new MMDModel();
		
		String pmd = "./test.pmd";
		String vmd = "./test.vmd";

		GLCanvas glcanvas = new GLCanvas(glcapabilities);
		model.setScale(1.0f);
		File f = new File(pmd);
		MMDDrawer drawer = new MMDDrawer(f.getParentFile(), glcanvas, baseTime);
		drawer.add(model);

		glcanvas.addGLEventListener(drawer);
		glcanvas.setSize(new Dimension((int) (320 * 1.5), (int) (240 * 1.5)));
		panel.add(glcanvas);
		final JFrame jframe = new JFrame("MMD on JOGL");
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				jframe.dispose();
				System.exit(0);
			}
		});

		jframe.getContentPane().add(panel, BorderLayout.CENTER);
		jframe.setSize(640, 480);
		jframe.setVisible(true);
	}

}