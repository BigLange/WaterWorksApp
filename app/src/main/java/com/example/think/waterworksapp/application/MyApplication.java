package com.example.think.waterworksapp.application;

import android.app.Application;

import com.example.think.waterworksapp.bean.Session;
import com.example.think.waterworksapp.bean.TeamMsgBean;

/**
 * Created by Think on 2016/8/11.
 */
public class MyApplication extends Application {
    private Session session;
    private TeamMsgBean teamMsgBean;

    public void setSession(Session session){
        this.session = session;
    }

    public Session getSession(){
        return session;
    }

    public TeamMsgBean getTeamMsgBean() {
        return teamMsgBean;
    }

    public void setTeamMsgBean(TeamMsgBean teamMsgBean) {
        this.teamMsgBean = teamMsgBean;
    }
}
