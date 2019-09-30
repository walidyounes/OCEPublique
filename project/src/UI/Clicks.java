/*
 * Copyright (c) 2019.  Younes Walid, IRIT, University of Toulouse
 */

package UI;

import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.MouseManager;

import java.awt.event.MouseEvent;

public class Clicks implements MouseManager {

    /**
     * The view this manager operates upon.
     */
    protected View view;


    /**
     * The graph to modify according to the view actions.
     */
    protected GraphicGraph graph;


    protected GraphicElement element;

    @Override
    public void init(GraphicGraph graphicGraph, View view) {
        this.graph = graphicGraph;
        this.view = view;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    @Override
    public void release() {
        view.removeMouseListener(this);
        view.removeMouseMotionListener(this);
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        element = view.findNodeOrSpriteAt(e.getX(), e.getY());
        if(element != null){
            System.out.println("Node = " +element.label);
           // element.setAttribute("ui.style", "fill-color: rgb("+r.nextInt(256)+","+r.nextInt(256)+","+r.nextInt(256)+");");
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  {@code MOUSE_DRAGGED} events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * {@code MOUSE_DRAGGED} events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
