package me.mw.workoutisgood.VO;

public class workoutInfoVO {

    private String name;
    private String type;
    private String imgURL;
    private String event;
    private boolean isComplete;
    private boolean[] checkBoxes;

    public boolean[] getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(boolean[] checkBoxes) {
        this.checkBoxes = checkBoxes;
    }


    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
