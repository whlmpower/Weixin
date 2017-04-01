package com.weixin.qr;

/**
 * Created by luc on 16/12/16.
 */
public class QRData {

    private String action_name = "QR_LIMIT_SCENE";

    private ActionInfo action_info;

    public QRData(int scen_id) {
        this.action_info = new ActionInfo(new Scene(scen_id));
    }

    public ActionInfo getAction_info() {
        return action_info;
    }

    public void setAction_info(ActionInfo action_info) {
        this.action_info = action_info;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }
}

class ActionInfo{
    private Scene scene;

    public ActionInfo(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}

class Scene{
    private int scene_id;

    public Scene(int scene_id) {
        this.scene_id = scene_id;
    }

    public int getScene_id() {
        return scene_id;
    }

    public void setScene_id(int scene_id) {
        this.scene_id = scene_id;
    }
}
