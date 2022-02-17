package com.azhe.init.service;

import com.azhe.init.dao.InitDAO;
import com.azhe.init.vo.ResourcesVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.Optional.*;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2022/1/6 4:20 下午
 * @since V2.0.0
 */
@Service("init")
public class InitServiceImpl implements IService, InitializingBean {

    // 主账号角色
    private List<Long> mainAccountRole;

    Logger logger = Logger.getGlobal();

    @Resource
    private InitDAO initDAO;

    @Resource
    @Qualifier("threadPool")
    private ThreadPoolExecutor threadPool;

    /**
     * 设置主账号角色
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        mainAccountRole = initDAO.getMainAccountRoleId();
    }

    @Override
    public Integer operateRole(String phone) {
        // 获取所有的pc菜单资源
        List<ResourcesVO> pcMenuList = initDAO.getPcMenuList();
        // 获取所有的pc菜单一级目录
        Map<String, Long> catalogMap = initDAO.getCatalogList().stream().collect(
                Collectors.toMap(ResourcesVO::getValue, ResourcesVO::getResourcesId));

        // 获取需要初始化的角色id
        List<Long> roleIdList = initDAO.getRoleIdByUsername(phone, true);
        initPcMenu(pcMenuList, catalogMap, roleIdList);
        return roleIdList.size();
    }

    @Override
    public Integer operateMerchant(String phone) {
        // 获取所有的pc菜单权限
        List<ResourcesVO> pcMenuList = initDAO.getPcMenuList();
        // 获取所有的pc菜单一级目录
        Map<String, Long> catalogMap = initDAO.getCatalogList().stream().collect(
                Collectors.toMap(ResourcesVO::getValue, ResourcesVO::getResourcesId));
        // 获取需要初始化的角色id
        List<Long> roleList = initDAO.findRoleByMerchantPhone(phone, true);
        initPcMenu(pcMenuList, catalogMap, roleList);
        return roleList.size();
    }

    @Override
    public Integer operateAll() {
        // 获取所有的pc菜单资源
        List<ResourcesVO> pcMenuList = initDAO.getPcMenuList();
        // 获取所有的pc菜单一级目录
        Map<String, Long> catalogMap = initDAO.getCatalogList().stream().collect(
                Collectors.toMap(ResourcesVO::getValue, ResourcesVO::getResourcesId));
        // 获取需要初始化的角色id
        List<Long> roleIdList = initDAO.listRole(true).stream().distinct().collect(Collectors.toList());

        initPcMenu(pcMenuList, catalogMap, roleIdList);
        return roleIdList.size();
    }

    /**
     * 初始化角色
     */
    private void initPcMenu(List<ResourcesVO> pcMenuList, Map<String, Long> catalogMap, List<Long> roleIdList){
        Map<Long, Integer> map = new ConcurrentHashMap<>();
        AtomicInteger index = new AtomicInteger(0);
        try {
            // 分批执行，4个角色为一组,一条线程执行
            int batchCount = roleIdList.size() % 4 == 0 ? roleIdList.size() / 4 : roleIdList.size() / 4 + 1;
            List<List<Long>> resultData = new ArrayList<>(batchCount);

            CountDownLatch countDownLatch = new CountDownLatch(batchCount);
            // 组成4个为一组的List
            for (int i = 0; i < batchCount; i++) {
                List<Long> roleList = new ArrayList<>(4);
                // 主线程分配资源
                for (int j = 0; j < 4 && 4 * i + j < roleIdList.size(); j++) {
                    Long id = roleIdList.get(4 * i + j);
                    roleList.add(id);
                }
                resultData.add(roleList);
            }
            // 执行线程
            for (int i = 0; i < batchCount; i++) {
                threadPool.execute(() -> {
                    try {
                        int nowIndex = index.getAndIncrement();
                        List<Object[]> batchData = new ArrayList<>();
                        if (nowIndex < batchCount) {
                            List<Long> idList = resultData.get(nowIndex);
                            idList.forEach(id->{
                                // 获取当前角色的旧菜单资源
                                List<ResourcesVO> resourcesList = ofNullable(initDAO.getOleMenuByRoleId(id)).orElse(new ArrayList<>(0))
                                        .stream().filter(r -> r.getValue().startsWith("m_") || r.getValue().startsWith("w_"))
                                        .collect(Collectors.toList());
                                List<ResourcesVO> pcList = new ArrayList<>(pcMenuList.size());
                                pcList.addAll(pcMenuList);
                                batchData.addAll(getPcResourceByRole(id, resourcesList, pcList, catalogMap));
                            });
                            logger.info("批量新增数据：" + printBatchData(batchData));
                            initDAO.batchInsertRoleResources(batchData);
                        }
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }
            countDownLatch.await();
            logger.info("执行完毕");
        } catch (Exception e) {
            logger.info("异常===========>" + e.getMessage());
        }
    }

    /**
     * 获取对应的pc菜单权限
     * @param oldMenuList 旧菜单权限
     * @param pcMenu 新的pc菜单权限
     * @return
     */
    private List<Object[]> getPcResourceByRole(Long adRoleId, List<ResourcesVO> oldMenuList, List<ResourcesVO> pcMenu, Map<String, Long> catalogMap) {
        List<Long> initResourcesIds = getRoleInitResourcesIds(adRoleId, oldMenuList, pcMenu, catalogMap).stream().distinct().collect(Collectors.toList());
        return of(initResourcesIds).orElse(new ArrayList<>(0)).stream().map(id -> new Object[]{id, adRoleId}).collect(Collectors.toList());
    }

    /**
     * 业务逻辑
     * @param oldMenu
     * @param allPcResources
     * @return
     */
    private List<Long> getRoleInitResourcesIds(Long adRoleId, List<ResourcesVO> oldMenu, List<ResourcesVO> allPcResources, Map<String, Long> catalogMap) {

        List<Long> result = new ArrayList<>();

        return result;
    }

    /**
     * 添加对应的权限资源id
     * @param valueList
     * @param resourceIds
     * @param pcResources
     */
    private void addResources(List<String> valueList, List<Long> resourceIds, List<ResourcesVO> pcResources) {
        List<ResourcesVO> collect = pcResources.stream().distinct().filter(pcResource -> valueList.contains(pcResource.getValue())).collect(Collectors.toList());
        resourceIds.addAll(collect.stream().map(ResourcesVO::getResourcesId).collect(Collectors.toList()));
        pcResources.removeAll(collect);
    }

    private String printBatchData(List<Object[]> batchData) {
        if (CollectionUtils.isEmpty(batchData)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object[] batchDatum : batchData) {

            sb.append("[").append(batchDatum[0]).append(",").append(batchDatum[1]).append("] ");
        }
        return sb.toString();
    }

}
