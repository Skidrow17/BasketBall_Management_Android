package com.uowm.skidrow.eok.model;

public class TeamCategoryGroup {

    private int categoryId;
    private String categoryName;
    private int groupId;
    private String groupName;

    public TeamCategoryGroup(int categoryId, String categoryName, int groupId, String groupName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }
}
