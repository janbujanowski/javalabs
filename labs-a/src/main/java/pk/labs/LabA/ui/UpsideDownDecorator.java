package pk.labs.LabA.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;


public class UpsideDownDecorator {
	
	public static JFrame apply(final JFrame frame) {
		JFrame udFrame = new JFrame() {
			@Override
			protected JRootPane createRootPane() {
				return UpsideDownDecorator.createRootPane();
			}
			
			public JFrame originalFrame = frame;
		};
		
		udFrame.setContentPane(frame.getContentPane());
		udFrame.setJMenuBar(frame.getJMenuBar());
		udFrame.setPreferredSize(frame.getPreferredSize());
		udFrame.setSize(frame.getSize());
		udFrame.setDefaultCloseOperation(frame.getDefaultCloseOperation());
		udFrame.setTitle(frame.getTitle() + " — UpsideDown");
		
		RepaintManager.setCurrentManager(createRepaintManager(udFrame));
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(createEventQueue(udFrame));
		
		return udFrame;
	}

	private static JRootPane createRootPane() {
		JRootPane rp = new JRootPane() {

            public void paint(Graphics g) {
                BufferedImage im = new BufferedImage(this.getWidth(), this.getHeight(),
                        BufferedImage.TYPE_3BYTE_BGR);
                // Paint normally but on the image
                super.paint(im.getGraphics());

                // Reverse the image
                AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
                tx.translate(-im.getWidth(), -im.getHeight());
                AffineTransformOp op = new AffineTransformOp(tx,
                        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                im = op.filter(im, null);

                // Draw the reversed image on the screen
                g.drawImage(im, 0, 0, null);
            }
        };
        rp.setOpaque(true);
        return rp;
	}
	
	private static RepaintManager createRepaintManager(final JFrame f) {
    	return new RepaintManager() {
	        public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
	            schedulePaint();
	        }
	
	        public void addDirtyRegion(Window window, int x, int y, int w, int h) {
	            schedulePaint();
	        }
	
	        public void paintDirtyRegions() {
	            schedulePaint();
	        }
	
	        private void schedulePaint() {
	            SwingUtilities.invokeLater(new Runnable() {
	
	                @Override
	                public void run() {
	                	f.paint(f.getGraphics());
	                }
	            });
	        }
    	};
    }
	
	private static EventQueue createEventQueue(final JFrame f) {
		return new EventQueue() {
	        protected void dispatchEvent(AWTEvent event) {
	            if (event instanceof MouseEvent) {
	                MouseEvent me = (MouseEvent) event;
	                MouseEvent evt = new MouseEvent(
	                        me.getComponent(),
	                        me.getID(),
	                        me.getWhen(),
	                        me.getModifiers(),
	                        f.getWidth() - me.getX() + f.getInsets().right - f.getInsets().left,
	                        f.getHeight() - me.getY() + f.getInsets().top - f.getInsets().bottom,
	                        me.getClickCount(),
	                        false,
	                        me.getButton());
	                event = evt;
	            }
	            super.dispatchEvent(event);
	        }
	    };
	}
}
