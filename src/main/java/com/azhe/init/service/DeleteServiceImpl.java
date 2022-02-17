package com.azhe.init.service;

import com.azhe.init.dao.InitDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Description:
 *
 * @author Linzr
 * @version V2.0.0
 * @date 2022/1/10 11:56 上午
 * @since V2.0.0
 */
@Service("delete")
public class DeleteServiceImpl implements IService {

    Logger logger = Logger.getGlobal();

    @Resource
    private InitDAO initDAO;

    @Resource
    @Qualifier("threadPool")
    private ThreadPoolExecutor threadPool;

    @Override
    public Integer operateRole(String phone) {
        List<Long> roleIdList = initDAO.getRoleIdByUsername(phone, false);
        return deletePcMenu(roleIdList);
    }

    @Override
    public Integer operateMerchant(String phone) {
        List<Long> roleIdList = initDAO.findRoleByMerchantPhone(phone, false);
        return deletePcMenu(roleIdList);
    }

    @Override
    public Integer operateAll() {
        List<Long> roleIdList = initDAO.listRole(false);
        return deletePcMenu(roleIdList);
    }


    /**
     * 分批删除，10个角色为一批
     * @param roleIdList
     * @return
     */
    private Integer deletePcMenu(List<Long> roleIdList) {
        AtomicInteger count = new AtomicInteger(0);
        try {
            if (CollectionUtils.isEmpty(roleIdList)) {
                return 0;
            }
            // 10个一组
            int batchCount = roleIdList.size() % 10 == 0 ? roleIdList.size() / 10 : roleIdList.size() / 10 + 1;
            CountDownLatch countDownLatch = new CountDownLatch(batchCount);
            for (int i = 0; i < batchCount; i++) {
                List<Long> roleId = new ArrayList<>(10);
                for (int j = 0; j < 10 && 10 * i + j < roleIdList.size(); j++) {
                    roleId.add(roleIdList.get(10 * i + j));
                }
                threadPool.execute(() -> {
                    count.addAndGet(initDAO.deletePcMenuByRoleList(roleId));
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
        } catch (Exception e) {
            logger.info("异常===========>" + e.getMessage());
        }
        return count.get();
    }

}
