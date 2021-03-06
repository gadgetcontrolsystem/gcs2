package kz.gcs.maps.client.drawing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)

 */
public class DrawingControlOptions implements Serializable {

    private static final long serialVersionUID = 9101754209698792392L;

    protected ControlPosition position;
    protected List<OverlayType> drawingModes;

    public DrawingControlOptions() {
    }

    public DrawingControlOptions(ControlPosition position, List<OverlayType> drawingModes) {
        this.position = position;
        this.drawingModes = drawingModes != null ? drawingModes : new ArrayList<OverlayType>();
    }

    public ControlPosition getPosition() {
        return position;
    }

    public void setPosition(ControlPosition position) {
        this.position = position;
    }

    public List<OverlayType> getDrawingModes() {
        return drawingModes;
    }

    public void setDrawingModes(List<OverlayType> drawingModes) {
        this.drawingModes = drawingModes;
    }
}
