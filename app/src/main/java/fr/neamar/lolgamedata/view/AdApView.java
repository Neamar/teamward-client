package fr.neamar.lolgamedata.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import fr.neamar.lolgamedata.R;

/**
 * Created by neamar on 17/04/16.
 */
public class AdApView extends View {
    final Paint apPaint = new Paint();
    final Paint adPaint = new Paint();

    private float ap = 5;
    private float ad = 5;

    public AdApView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaint();
    }

    public AdApView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaint();
    }

    public AdApView(Context context) {
        super(context);
        setPaint();
    }

    public void setPaint() {
        apPaint.setColor(getContext().getResources().getColor(R.color.colorAp));
        adPaint.setColor(getContext().getResources().getColor(R.color.colorAd));
    }

    @Override
    public void onDraw(Canvas canvas) {
        apPaint.setStrokeWidth(getWidth());
        adPaint.setStrokeWidth(getWidth());
        int halfWidth = getWidth() / 2;

        if (!isAp() && !isAd()) {
            canvas.drawLine(halfWidth, 0, halfWidth, getHeight() / 2, apPaint);
            canvas.drawLine(halfWidth, getHeight() / 2, halfWidth, getHeight(), adPaint);
        } else if (isAd()) {
            canvas.drawLine(halfWidth, 0, halfWidth, getHeight(), adPaint);
        } else if (isAp()) {
            canvas.drawLine(halfWidth, 0, halfWidth, getHeight(), apPaint);
        }
    }

    public boolean isAp() {
        return ap * .75 > ad;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public boolean isAd() {
        return ad * .75 > ap;
    }

    public void setAd(int ad) {
        this.ad = ad;
    }
}
