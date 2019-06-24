package com.xmcc.repository;
import com.xmcc.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {

    Page<OrderMaster> findOrderMastersByBuyerOpenid(String openid, Pageable pageable);
    List<OrderMaster>  findAllByBuyerOpenid(String openId);
}
