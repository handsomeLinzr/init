package com.azhe.init.vo;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2022/1/6 4:02 下午
 * @since V2.0.0
 */
public class ResourcesVO {

    private Long resourcesId;

    private Long systemId;

    private Long parentId;

    private String value;

    private String name;

    public Long getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(Long resourcesId) {
        this.resourcesId = resourcesId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
