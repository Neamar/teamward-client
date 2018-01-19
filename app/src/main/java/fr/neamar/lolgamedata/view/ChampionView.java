package fr.neamar.lolgamedata.view;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Champion;
import fr.neamar.lolgamedata.pojo.Team;

/**
 * Custom FrameLayout view containing an ImageView. Used to display a champion and their according border.
 * Supports multiple border types. For that, check {@link BorderMode}
 */
public class ChampionView extends FrameLayout {

    private Champion champion;
    private Team team;
    private @BorderMode int borderMode = BorderMode.NONE;

    private ImageView innerImageView;

    private int borderWidth;

    public ChampionView(Context context) {
        super(context);
        init(null);
    }

    public ChampionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChampionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //start creating border

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ChampionView,
                0, 0);

        try {
            borderWidth = a.getDimensionPixelOffset(R.styleable.ChampionView_borderWidth, getResources().getDimensionPixelOffset(R.dimen.champion_view_default_border));
        } finally {
            a.recycle();
        }

        LayoutInflater.from(getContext()).inflate(R.layout.view_champion, this);
        innerImageView = ((ImageView) getChildAt(0));
        innerImageView.setImageResource(R.drawable.default_champion);
    }

    public void setBorderMode(@BorderMode int borderMode) {
        this.borderMode = borderMode;

        ViewGroup.MarginLayoutParams params = ((MarginLayoutParams) innerImageView.getLayoutParams());
        if (borderMode != BorderMode.NONE) {
            params.setMargins(borderWidth, borderWidth, borderWidth, borderWidth);
        } else {
            params.setMargins(0, 0, 0, 0);
        }

        drawBorder();
    }

    public void setTeam(Team team) {
        this.team = team;

        drawBorder();
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
        ImageLoader.getInstance().displayImage(champion.imageUrl, innerImageView);
        drawBorder();
    }

    private void drawApAd() {
        if (champion != null) {
            if (!champion.isAp() && !champion.isAd()) {
                setBackgroundResource(R.drawable.ap_ad_border);
            } else {
                setBackgroundResource(champion.isAd() ? R.color.colorAd : R.color.colorAp);
            }
        }
    }

    private void drawTeam() {
        if (team != null) {
            setBackgroundColor(getResources().getColor(team.teamId == 100 ? R.color.blueTeam : R.color.redTeam));
        }
    }

    private void drawBorder() {
        switch (borderMode) {
            case BorderMode.AP_AD:
                drawApAd();
                break;
            case BorderMode.TEAM:
                drawTeam();
                break;
            case BorderMode.BOTH:
                //not implemented yet
            case BorderMode.NONE:
            default:
                //do nothing
        }
    }

    public void setImageResource(int imageResource) {
        innerImageView.setImageResource(imageResource);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BorderMode.NONE, BorderMode.AP_AD, BorderMode.TEAM, BorderMode.BOTH})
    public @interface BorderMode {
        /**
         * No border will be shown.
         */
        int NONE = -1;
        /**
         * Border with colours according to champion's damage type.
         */
        int AP_AD = 0;
        /**
         * Border with color of team.
         */
        int TEAM = 1;
        /**
         * Both ap/ad and team's border will be displayed.
         */
        int BOTH = 2; //not supported yet
    }
}
