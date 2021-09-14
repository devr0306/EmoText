package com.emotext.chatapp.Models.API;

import java.util.Date;

public class Message {
    /*
    id: {
    primaryKey: true,
    type: Sequelize.UUID,
    defaultValue: Sequelize.UUIDV1,
  },
  fromId: {
    type: DataTypes.STRING,
    allowNull: false,
    defaultValue: "",
  },
  text: {
    type: DataTypes.STRING,
    allowNull: false,
    defaultValue: "",
  },
  isRead: {
    type: DataTypes.BOOLEAN,
    defaultValue: false,
  },
  readBy: {
    type: DataTypes.ARRAY(DataTypes.STRING),
    defaultValue: [],
  },
  chatId: {
    type: DataTypes.STRING,
    allowNull: false,
    defaultValue: "",
  },
  isGroupChat: {
     */

    String id, fromId, text, isRead, chatId;
    String[] readBy;
    String createdAt;
    boolean isGroupChat;

    Date date;
    String dateString;
    String time;
    String year;

    public Message(String id, String fromId, String text, String isRead, String[] readBy, String chatId, String createdAt, boolean isGroupChat) {
        this.id = id;
        this.fromId = fromId;
        this.text = text;
        this.isRead = isRead;
        this.readBy = readBy;
        this.chatId = chatId;
        this.createdAt = createdAt;
        this.isGroupChat = isGroupChat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String[] getReadBy() {
        return readBy;
    }

    public void setReadBy(String[] readBy) {
        this.readBy = readBy;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }

    public void setTime(String time){this.time = time;}

    public String getTime(){return time;}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
