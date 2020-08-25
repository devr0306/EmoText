package com.example.chatapp.Models.API;

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
    boolean isGroupChat;

    public Message(String id, String fromId, String text, String isRead, String[] readBy, String chatId, boolean isGroupChat) {
        this.id = id;
        this.fromId = fromId;
        this.text = text;
        this.isRead = isRead;
        this.readBy = readBy;
        this.chatId = chatId;
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

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }
}
