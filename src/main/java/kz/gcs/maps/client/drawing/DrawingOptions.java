package kz.gcs.maps.client.drawing;

import java.io.Serializable;

/**
 * @author Igor Korotkov (igor@ikorotkov.com)

 */
public class DrawingOptions implements Serializable {

    private static final long serialVersionUID = -806817086700404391L;

    protected PolygonOptions polygonOptions;
    protected CircleOptions circleOptions;
    protected OverlayType initialDrawingMode;
    protected boolean enableDrawingControl;
    protected DrawingControlOptions drawingControlOptions;

    public DrawingOptions() {
    }

    public PolygonOptions getPolygonOptions() {
        return polygonOptions;
    }

    public void setPolygonOptions(PolygonOptions polygonOptions) {
        this.polygonOptions = polygonOptions;
    }

    public CircleOptions getCircleOptions() {
        return circleOptions;
    }

    public void setCircleOptions(CircleOptions circleOptions) {
        this.circleOptions = circleOptions;
    }

    public OverlayType getInitialDrawingMode() {
        return initialDrawingMode;
    }

    public void setInitialDrawingMode(OverlayType initialDrawingMode) {
        this.initialDrawingMode = initialDrawingMode;
    }

    public boolean isEnableDrawingControl() {
        return enableDrawingControl;
    }

    public void setEnableDrawingControl(boolean enableDrawingControl) {
        this.enableDrawingControl = enableDrawingControl;
    }

    public DrawingControlOptions getDrawingControlOptions() {
        return drawingControlOptions;
    }

    public void setDrawingControlOptions(DrawingControlOptions drawingControlOptions) {
        this.drawingControlOptions = drawingControlOptions;
    }
}
