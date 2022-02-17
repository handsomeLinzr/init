package com.azhe.init.dao;

import com.azhe.init.vo.ResourcesVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2022/1/7 3:44 下午
 * @since V2.0.0
 */
@Repository
public class InitDAO {

    public InitDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;
    Logger logger = Logger.getGlobal();

    private String getTableName() {
        return "ad_role_resources";
    }

    public List<Long> getMainAccountRoleId() {
        return new ArrayList<>(0);
    }

    /**
     * 获取所有的新pc资源
     * @return
     */
    public List<ResourcesVO> getPcMenuList() {
        return new ArrayList<>(0);
    }

    /**
     * 获取所有的新pc资源一级目录
     * @return
     */
    public List<ResourcesVO> getCatalogList() {
        return new ArrayList<>(0);
    }

    /**
     * 根据子账号查询角色id列表
     * @param flag 是否需要过滤已经初始化的菜单(初始化时需要排除掉已经初始化过的角色，删除时则不需要)
     * @param phone
     * @return
     */
    public List<Long> getRoleIdByUsername(String phone, boolean flag) {
        return new ArrayList<>(0);
    }

    /**
     * 获取商户的所有角色
     * @param flag 是否需要过滤已经初始化的菜单(初始化时需要排除掉已经初始化过的角色，删除时则不需要)
     * @return
     */
    public List<Long> findRoleByMerchantPhone(String phone, boolean flag) {
        return new ArrayList<>(0);
    }

    /**
     * 获取所有需要初始化pc菜单的角色
     * @param flag 是否需要过滤已经初始化的菜单(初始化时需要排除掉已经初始化过的角色，删除时则不需要)
     * @return
     */
    public List<Long> listRole(boolean flag) {
        return new ArrayList<>(0);
    }

    /**
     * 根据角色查询pc菜单数量
     * @param adRoleId 角色id
     * @return
     */
    public int countPcMenu(Long adRoleId) {
        return 0;
    }

    /**
     * 根据角色ID获取角色旧菜单权限
     * @param adRoleId
     * @return
     */
    public List<ResourcesVO> getOleMenuByRoleId(Long adRoleId) {
        return new ArrayList<>(0);
    }
    /**
     * 批量保存
     * @param list
     */
    public void batchInsertRoleResources(List<Object[]> list) {

    }

    /**
     * 根据角色删除pc权限数据
     */
    public Integer deletePcMenuByRoleList(List<Long> roleIdList) {
        return 0;
    }

}
