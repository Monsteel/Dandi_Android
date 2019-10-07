package org.techtown.schooler.ChannelEvents;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.techtown.schooler.NavigationFragment.MainFragment;
import org.techtown.schooler.R;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class EventDecorator implements DayViewDecorator {


    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates, MainFragment context) {

        this.color = color;
        this.dates = new HashSet<>(dates);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.addSpan(new DotSpan(5, color)); // 날자밑에 점
    }
}
