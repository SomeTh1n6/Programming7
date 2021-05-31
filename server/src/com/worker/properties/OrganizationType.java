package com.worker.properties;

import java.io.Serializable;
import java.util.Arrays;

public enum OrganizationType implements Serializable {
    COMMERCIAL("Коммерческая"),
    PRIVATE_LIMITED_COMPANY("Част. комп. с огр. ответственностью"),
    OPEN_JOINT_STOCK_COMPANY("Открытое акционерное общество"),
    UNDEFINED("-");

    private String organizationType;

    OrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**@return тип организации
     * */
    public String getOrganizationType(){
        return organizationType;
    }

    public static OrganizationType getEnum(String value) {

        OrganizationType organizationType = Arrays.stream(OrganizationType.values()).
                filter(m -> m.organizationType.equals(value)).
                findAny().orElse(null);
        return organizationType;
    }
}
