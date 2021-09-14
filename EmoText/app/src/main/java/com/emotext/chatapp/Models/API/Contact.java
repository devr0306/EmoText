package com.emotext.chatapp.Models.API;

public class Contact {

    /*id: {
    primaryKey: true,
    type: Sequelize.UUID,
    defaultValue: Sequelize.UUIDV1,
  },
  userOneId: {
    type: DataTypes.STRING,
    allowNull: false,
    defaultValue: "",
  },
  userTwoId: {
    type: DataTypes.STRING,
    allowNull: false,
    defaultValue: "",
  },
  userOneName: {
    type: DataTypes.STRING,
    defaultValue: "",
  },
  userTwoName: {
    type: DataTypes.STRING,
    defaultValue: "",
  },*/

    String id;
    String userOneId;
    String userTwoId;
    String getUserOneName;
    String userTwoName;

    public Contact(String id, String userOneId, String userTwoId, String getUserOneName, String userTwoName) {
        this.id = id;
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
        this.getUserOneName = getUserOneName;
        this.userTwoName = userTwoName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserOneId() {
        return userOneId;
    }

    public void setUserOneId(String userOneId) {
        this.userOneId = userOneId;
    }

    public String getUserTwoId() {
        return userTwoId;
    }

    public void setUserTwoId(String userTwoId) {
        this.userTwoId = userTwoId;
    }

    public String getGetUserOneName() {
        return getUserOneName;
    }

    public void setGetUserOneName(String getUserOneName) {
        this.getUserOneName = getUserOneName;
    }

    public String getUserTwoName() {
        return userTwoName;
    }

    public void setUserTwoName(String userTwoName) {
        this.userTwoName = userTwoName;
    }
}
