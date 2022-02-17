package com.azhe.init.service;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2022/1/10 11:56 上午
 * @since V2.0.0
 */
public interface IService {

    /**
     * 执行操作
     * @param type
     * @param phone
     * @return
     */
    default Integer operate(String type, String phone) {
        Integer result = 0;
        switch (type) {
            case "user":
                result = operateRole(phone);
                break;
            case "merchant":
                result = operateMerchant(phone);
                break;
            case "all":
                result = operateAll();
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 操作角色
     * @return
     */
    Integer operateRole(String phone);

    /**
     * 操作商户
     * @return
     */
    Integer operateMerchant(String phone);

    /**
     * 操作所有
     * @return
     */
    Integer operateAll();

}
