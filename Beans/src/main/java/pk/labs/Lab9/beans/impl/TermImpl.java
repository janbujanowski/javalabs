package pk.labs.Lab9.beans.impl;

import pk.labs.Lab9.beans.Term;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Wojtek on 2014-12-14.
 */
public class TermImpl implements Term {
    private Date begin;
    private int duration;

    public TermImpl() {}

    @Override
    public Date getBegin() {
        return begin;
    }

    @Override
    public void setBegin(Date begin) {
        this.begin=begin;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        if (duration > 0) this.duration=duration;
    }

    @Override
    public Date getEnd() {
        long d = duration*60000;
        long t= begin.getTime();
        return new Date(t+d);
    }
}
