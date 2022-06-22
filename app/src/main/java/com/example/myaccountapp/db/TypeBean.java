package com.example.myaccountapp.db;

//表示收入或者支出具体类型的类
public class TypeBean {
    int id;
    String typename;    //类型名称
    int imageId;        //未被选中的图片id
    int simgaeId;       //被选中的图片id
    int kind;           //收入 1   支出 0

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSimgaeId() {
        return simgaeId;
    }

    public void setSimgaeId(int simgaeId) {
        this.simgaeId = simgaeId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public TypeBean() {
    }

    public TypeBean(int id, String typename, int imageId, int simgaeId, int kind) {
        this.id = id;
        this.typename = typename;
        this.imageId = imageId;
        this.simgaeId = simgaeId;
        this.kind = kind;
    }
}
