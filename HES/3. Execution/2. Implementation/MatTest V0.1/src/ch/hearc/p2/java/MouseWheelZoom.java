package ch.hearc.p2.java;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Enumeration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnBehaviorPost;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;

public class MouseWheelZoom extends MouseBehavior {

	double z_factor = .1;
	Vector3d translation = new Vector3d();

	private MouseBehaviorCallback callback = null;

	/**
	 * Creates a zoom behavior given the transform group.
	 * 
	 * @param transformGroup
	 *            The transformGroup to operate on.
	 */
	public MouseWheelZoom(TransformGroup transformGroup) {
		super(transformGroup);
	}

	/**
	 * Creates a default mouse zoom behavior.
	 **/
	public MouseWheelZoom() {
		super(0);
	}

	/**
	 * Creates a zoom behavior. Note that this behavior still needs a transform
	 * group to work on (use setTransformGroup(tg)) and the transform group must
	 * add this behavior.
	 * 
	 * @param flags
	 */
	public MouseWheelZoom(int flags) {
		super(flags);
	}

	/**
	 * Creates a zoom behavior that uses AWT listeners and behavior posts rather
	 * than WakeupOnAWTEvent. The behavior is added to the specified Component.
	 * A null component can be passed to specify the behavior should use
	 * listeners. Components can then be added to the behavior with the
	 * addListener(Component c) method.
	 * 
	 * @param c
	 *            The Component to add the MouseListener and MouseMotionListener
	 *            to.
	 * @since Java 3D 1.3.2
	 */
	public MouseWheelZoom(Component c) {
		super(c, 0);
	}

	/**
	 * Creates a zoom behavior that uses AWT listeners and behavior posts rather
	 * than WakeupOnAWTEvent. The behaviors is added to the specified Component
	 * and works on the given TransformGroup.
	 * 
	 * @param c
	 *            The Component to add the MouseListener and MouseMotionListener
	 *            to. A null component can be passed to specify the behavior
	 *            should use listeners. Components can then be added to the
	 *            behavior with the addListener(Component c) method.
	 * @param transformGroup
	 *            The TransformGroup to operate on.
	 * @since Java 3D 1.3.2
	 */
	public MouseWheelZoom(Component c, TransformGroup transformGroup) {
		super(c, transformGroup);
	}

	/**
	 * Creates a zoom behavior that uses AWT listeners and behavior posts rather
	 * than WakeupOnAWTEvent. The behavior is added to the specified Component.
	 * A null component can be passed to specify the behavior should use
	 * listeners. Components can then be added to the behavior with the
	 * addListener(Component c) method. Note that this behavior still needs a
	 * transform group to work on (use setTransformGroup(tg)) and the transform
	 * group must add this behavior.
	 * 
	 * @param flags
	 *            interesting flags (wakeup conditions).
	 * @since Java 3D 1.3.2
	 */
	public MouseWheelZoom(Component c, int flags) {
		super(c, flags);
	}

	public void initialize() {
		super.initialize();
		if ((flags & INVERT_INPUT) == INVERT_INPUT) {
			z_factor *= -1;
			invert = true;
		}
	}

	/**
	 * Return the y-axis movement multipler.
	 **/
	public double getFactor() {
		return z_factor;
	}

	/**
	 * Set the wheel units movement multipler with factor.
	 **/
	public void setFactor(double factor) {
		z_factor = factor;
	}

	public void processStimulus(Enumeration criteria) {
		WakeupCriterion wakeup;
		AWTEvent[] events;
		MouseEvent evt;

		while (criteria.hasMoreElements()) {
			wakeup = (WakeupCriterion) criteria.nextElement();
			if (wakeup instanceof WakeupOnAWTEvent) {
				events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
				if (events.length > 0) {
					evt = (MouseEvent) events[events.length - 1];
					doProcess(evt);
				}
			}

			else if (wakeup instanceof WakeupOnBehaviorPost) {
				while (true) {
					synchronized (mouseq) {
						if (mouseq.isEmpty())
							break;
						evt = (MouseEvent) mouseq.remove(0);
						// consolidate MOUSE_WHEEL events
						while ((evt.getID() == MouseEvent.MOUSE_WHEEL)
								&& !mouseq.isEmpty()
								&& (((MouseEvent) mouseq.get(0)).getID() == MouseEvent.MOUSE_WHEEL)) {
							evt = (MouseEvent) mouseq.remove(0);
						}
					}
					doProcess(evt);
				}
			}

		}
		wakeupOn(mouseCriterion);
	}

	void doProcess(MouseEvent evt) {
		int units = 0;

		processMouseEvent(evt);

		if ((evt.getID() == MouseEvent.MOUSE_WHEEL)) {
			MouseWheelEvent wheelEvent = (MouseWheelEvent) evt;
			if (wheelEvent.getScrollType() == wheelEvent.WHEEL_UNIT_SCROLL) {
				units = wheelEvent.getUnitsToScroll();
			}

			if (!reset) {
				transformGroup.getTransform(currXform);

				translation.z = units * z_factor;

				transformX.set(translation);

				if (invert) {
					currXform.mul(currXform, transformX);
				} else {
					currXform.mul(transformX, currXform);
				}

				transformGroup.setTransform(currXform);

				transformChanged(currXform);

				if (callback != null)
					callback.transformChanged(MouseBehaviorCallback.ZOOM,
							currXform);

			} else {
				reset = false;
			}
		}
	}

	/**
	 * Users can overload this method which is called every time the Behavior
	 * updates the transform
	 *
	 * Default implementation does nothing
	 */
	public void transformChanged(Transform3D transform) {
	}

	/**
	 * The transformChanged method in the callback class will be called every
	 * time the transform is updated
	 */
	public void setupCallback(MouseBehaviorCallback callback) {
		this.callback = callback;
	}
}
