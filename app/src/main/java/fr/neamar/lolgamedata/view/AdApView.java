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
    Paint apPaint = new Paint();
    Paint adPaint = new Paint();

    private float ap = 5;
    private float ad = 5;
    private float apdSum = 10;

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
        float ratio = ap / apdSum;
        apPaint.setStrokeWidth(getWidth());
        adPaint.setStrokeWidth(getWidth());

        int halfWidth = getWidth() / 2;
        canvas.drawLine(halfWidth, 0, halfWidth , getHeight() * ratio, apPaint);
        canvas.drawLine(halfWidth , getHeight() * ratio, halfWidth , getHeight(), adPaint);
    }

    public void setAp(int ap) {
        this.ap = ap;
        apdSum = ap + ad;
    }

    public void setAd(int ad) {
        this.ad = ad;
        apdSum = ap + ad;
    }
}
